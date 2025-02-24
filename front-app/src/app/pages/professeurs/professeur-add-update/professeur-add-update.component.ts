import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { ProfesseurService } from '../../../core/services/professeur.service';
import { Professeur } from '../../../core/models/ProfesseurModel';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-professeur-add-update',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatToolbarModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './professeur-add-update.component.html',
  styleUrls: ['./professeur-add-update.component.css']
})
export class ProfesseurAddUpdateComponent implements OnInit {
  form!: FormGroup;
  errorMessage = '';
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private professeurService: ProfesseurService,
    private dialogRef: MatDialogRef<ProfesseurAddUpdateComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Professeur
  ) {}

  ngOnInit(): void {
    this.initForm();
    if (this.data?.id) {
      this.populateForm();
    }
  }

  initForm() {
    this.form = this.fb.group({
      userName: ['', Validators.required],
      password: ['', Validators.required],
      firstname: ['', Validators.required],
      lastName: ['', Validators.required],
      emailId: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required],
      address: ['', Validators.required]
    });
  }

  populateForm() {
    this.form.patchValue({
      userName: this.data.user.userName,
      password: this.data.user.password,
      firstname: this.data.user.firstname,
      lastName: this.data.user.lastName,
      emailId: this.data.user.emailId,
      phoneNumber: this.data.phoneNumber,
      address: this.data.address
    });
  }

  onSubmit() {
    if (this.form.invalid) return;
    this.isLoading = true;

    const body: Professeur = {
      user: {
        userName: this.form.get('userName')?.value,
        password: this.form.get('password')?.value,
        firstname: this.form.get('firstname')?.value,
        lastName: this.form.get('lastName')?.value,
        emailId: this.form.get('emailId')?.value
      },
      phoneNumber: this.form.get('phoneNumber')?.value,
      address: this.form.get('address')?.value,
      id: this.data?.id
    };

    if (this.data?.id) {
      this.professeurService.updateProfesseur(this.data.id, body).subscribe({
        next: () => {
          this.isLoading = false;
          this.dialogRef.close(true);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = err?.error?.message || 'Erreur lors de la mise à jour';
        }
      });
    } else {
      this.professeurService.addProfesseur(body).subscribe({
        next: () => {
          this.isLoading = false;
          this.dialogRef.close(true);
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = err?.error?.message || 'Erreur lors de l’ajout';
        }
      });
    }
  }
}