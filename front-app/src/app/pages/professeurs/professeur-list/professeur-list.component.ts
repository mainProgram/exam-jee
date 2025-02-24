import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { ProfesseurService } from '../../../core/services/professeur.service';
import { TranslateMatPaginatorService } from '../../../core/services/translate-mat-paginator.service';
import { LoaderService } from '../../../core/services/loader.service';
import { finalize, take } from 'rxjs';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import Swal from 'sweetalert2';
import { ProfesseurAddUpdateComponent } from '../professeur-add-update/professeur-add-update.component';
import { Professeur } from '../../../core/models/ProfesseurModel';

@Component({
  selector: 'app-professeur-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatPaginatorModule
  ],
  templateUrl: './professeur-list.component.html',
  styleUrls: ['./professeur-list.component.css']
})
export class ProfesseurListComponent implements OnInit {
  professeurService = inject(ProfesseurService);
  loaderService = inject(LoaderService);
  dialog = inject(MatDialog);
  translateMatPaginatorService = inject(TranslateMatPaginatorService);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  displayedColumns: string[] = ['firstname', 'lastname', 'email', 'phoneNumber', 'details'];
  dataSource = new MatTableDataSource<Professeur>([]);
  length = 0;
  pageSize = 10;

  constructor() {
    this.loaderService.showLoader();
    this.professeurService.fetchProfesseurs()
      .pipe(
        take(1),
        finalize(() => this.loaderService.hideLoader())
      )
      .subscribe();
  }

  getProfesseurs() {
    this.professeurService.fetchProfesseurs().subscribe((data) => {
      this.dataSource = new MatTableDataSource<Professeur>(data);
      this.length = data.length;
      this.dataSource.paginator = this.paginator;
      this.translateMatPaginatorService.translateMatPaginator(this.paginator);
    });
  }

  deleteProfesseur(id: string) {
    Swal.fire({
      title: 'Suppression',
      text: 'Voulez-vous vraiment supprimer le professeur ?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Supprimer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.professeurService.deleteProfesseur(id).subscribe(() => {
          this.getProfesseurs();
          Swal.fire('Succès', 'Suppression réussie !', 'success');
        });
      }
    });
  }

  add() {
    const dialogRef = this.dialog.open(ProfesseurAddUpdateComponent, { width: '600px' });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        Swal.fire('Enregistrement effectué !');
        this.getProfesseurs();
      }
    });
  }

  edit(professeur: Professeur) {
    const dialogRef = this.dialog.open(ProfesseurAddUpdateComponent, { width: '600px', data: professeur });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        Swal.fire('Enregistrement effectué !');
        this.getProfesseurs();
      }
    });
  }

  ngOnInit(): void {
    this.getProfesseurs();
  }
}