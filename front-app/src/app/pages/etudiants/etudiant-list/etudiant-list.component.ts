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
import {MatIcon, MatIconModule} from '@angular/material/icon';
import {MatButton, MatButtonModule, MatMiniFabButton} from '@angular/material/button';
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
import Swal from 'sweetalert2';
import {EtudiantAddUpdateComponent} from '../etudiant-add-update/etudiant-add-update.component';
import {Etudiant} from '../../../core/models/EtudiantModel';

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
    MatIconModule,
    MatButtonModule,
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
export class EtudiantListComponent implements OnInit {
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

  getEtudiants() {
    this.etudiantService.fetchEtudiants().subscribe((data) => {
      console.log(data)
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

  deleteEtudiant(id: string|undefined){
    Swal.fire({
      title: 'Suppression',
      text: 'Voulez-vous vraiment vous supprimer l\'étudiant ?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Supprimer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.value) {
        this.etudiantService.deleteEtudiant(id ?? "").subscribe({
          next: (data) => {
            this.getEtudiants()
            console.log(data)
            Swal.fire(
              'Succès',
              'Suppression réussie !',
              'success'
            )
          }
        })
      }
    })
  }

 add() {
   const dialogRef = this.dialog.open(EtudiantAddUpdateComponent, {
     width: '600px',
   });

   dialogRef.afterClosed().subscribe(result => {
     if (result) {
       Swal.fire("Enregistrement effectué !")
       this.getEtudiants()
     }
   });
 }

  edit(data: Etudiant) {
    const dialogRef = this.dialog.open(EtudiantAddUpdateComponent, {
      width: '600px',
      data: data
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        Swal.fire("Enregistrement effectué !")
        this.getEtudiants()
      }
    });
  }

  ngOnInit(): void {
    this.getEtudiants()
  }
}

