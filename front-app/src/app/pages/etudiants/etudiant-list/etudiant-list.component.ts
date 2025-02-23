import {AfterViewInit, Component, inject, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {
  MatCell, MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef, MatTable, MatTableDataSource
} from '@angular/material/table';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatIcon} from '@angular/material/icon';
import {MatButton, MatMiniFabButton} from '@angular/material/button';
import {FormatStatusPipe} from '../../../shared/pipes/format-status.pipe';
import {MatToolbar} from '@angular/material/toolbar';
import {MatInput} from '@angular/material/input';
import {MatSort} from '@angular/material/sort';
import {MatDialog} from '@angular/material/dialog';
import {EtudiantService} from '../../../core/services/etudiant.service';
import {TranslateMatPaginatorService} from '../../../core/services/translate-mat-paginator.service';
import {Router} from '@angular/router';
import {LoaderService} from '../../../core/services/loader.service';
import {finalize, take} from 'rxjs';
import {CommonModule} from '@angular/common';
import {MatCard} from '@angular/material/card';

@Component({
  selector: 'app-etudiant-list',
  standalone: true,
  imports: [
    CommonModule,
    MatPaginator,
    MatCell,
    MatHeaderCell,
    MatColumnDef,
    MatFormField,
    MatIcon,
    MatButton,
    MatRow,
    MatHeaderRowDef,
    MatHeaderRow,
    MatRowDef,
    MatHeaderCellDef,
    MatCellDef,
    MatTable,
    FormatStatusPipe,
    MatToolbar,
    MatInput,
    MatMiniFabButton,
    MatCard,
    MatLabel
  ],
  templateUrl: './etudiant-list.component.html',
  styleUrl: './etudiant-list.component.css'
})
export class EtudiantListComponent implements OnInit, AfterViewInit {
  router = inject(Router);
  etudiantService = inject(EtudiantService);
  loaderService = inject(LoaderService);
  loading = inject(LoaderService).loading;
  dialog= inject(MatDialog);
  translateMatPaginatorService= inject(TranslateMatPaginatorService);

  @ViewChild(MatSort) sort!: MatSort;
  displayedColumns: string[] = [
    'firstname',
    'lastname',
    'email',
    'registrationNumber',
    'phoneNumber',
    'details',
  ];
  // @ts-ignore
  dataSource: MatTableDataSource<OrganisationDTO> = new MatTableDataSource([]);
  filterValues: any = {
    name: '',
    status: '',
    type: '',
  };
  @ViewChild(MatPaginator) paginator: MatPaginator | undefined;
  length = 0;
  pageSize = 10
  constructor() {
    this.loaderService.showLoader()
    this.etudiantService.fetchEtudiants().pipe(
      take(1),
      finalize(() => this.loaderService.hideLoader())
    ).subscribe();
  }

  get etudiants() {
    console.log(this.etudiantService.resources())
    return this.etudiantService.resources;
  }

  getOrganisations() {
    this.etudiantService.fetchEtudiants().subscribe((data) => {
      this.dataSource = new MatTableDataSource<any>(data);
      this.dataSource.filterPredicate = this.createFilterPredicate();
      this.length = data.length
      if (this.paginator) {
        this.dataSource.paginator = this.paginator;
        this.translateMatPaginatorService.translateMatPaginator(this.paginator);
      }
    });
  }

  createFilterPredicate() {
    return (data: any, filter: string): boolean => {
      const searchTerms = JSON.parse(filter) as typeof this.filterValues;

      const nameMatches = data.name.toLowerCase().includes(searchTerms.name.toLowerCase());

      const typeDisplay = data.type?.[0]?.coding?.[0]?.display?.toLowerCase() || '';
      const typeMatches = typeDisplay.includes(searchTerms.type.toLowerCase());

      const statusMatches = searchTerms.status === '' ||
        data.active?.toString() === searchTerms.status;

      return nameMatches && typeMatches && statusMatches;
    };
  }

  getColorForStatus(active: boolean): string {
    return active ? '#4caf50' : '#f44336';
  }

  applyFilter(field: 'name' | 'type' | 'status', event: any) {
    if (field === 'status') {
      this.filterValues.status = event.value;
    } else {
      this.filterValues[field] = (event.value || '').trim();
    }

    // Update the filter string
    this.dataSource.filter = JSON.stringify(this.filterValues);

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  ngAfterViewInit(): void {
  }

  ngOnInit(): void {
  }
}

