import {Component, inject, Inject, OnInit} from '@angular/core';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MatError, MatFormField, MatFormFieldModule, MatLabel} from "@angular/material/form-field";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatToolbar} from "@angular/material/toolbar";
import {NgForOf, NgIf} from "@angular/common";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatOption, MatSelect} from "@angular/material/select";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {EtudiantService} from '../../../core/services/etudiant.service';
import {take} from 'rxjs';
import {Etudiant} from '../../../core/models/EtudiantModel';
import {LoaderComponent} from '../../../shared/components/loader/loader.component';

@Component({
  selector: 'app-etudiant-add-update',
  standalone: true,
  imports: [
    MatProgressSpinner,
    MatFormField,
    MatFormFieldModule,
    MatInputModule,
    MatLabel,
    MatError,
    MatToolbar,
    MatButtonModule,
    MatDialogModule,
    ReactiveFormsModule,
    NgIf,
    NgForOf,
    MatSelect,
    MatOption,
    LoaderComponent
  ],
  templateUrl: './etudiant-add-update.component.html',
  styleUrl: './etudiant-add-update.component.css'
})
export class EtudiantAddUpdateComponent implements OnInit{
  form!: FormGroup;
  errorMessage: string = "";
  isLoading= false

  constructor(
    private _etudiantService: EtudiantService,
    private fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: Etudiant,
    private dialogRef: MatDialogRef<EtudiantAddUpdateComponent>,
  ) {
    this._etudiantService.fetchEtudiants().pipe(
      take(1),
    ).subscribe();
  }


  ngOnInit(): void {
    this.initForm()
    if(this.data?.id)
      this.populateForm()
  }

  initForm(){
    this.form = this.fb.group({
      firstname: ['', Validators.required],
      lastName: ['', Validators.required],
      emailId: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      address: ['', Validators.required],
      password: [''],
      username: [''],
    });
  }

  populateForm(){
    this.form = this.fb.group({
      firstname: [this.data.user.firstname, Validators.required],
      lastName: [this.data.user.lastName, Validators.required],
      emailId: [this.data.user.emailId, Validators.required],
      phoneNumber: [this.data.phoneNumber, Validators.required],
      address: [this.data.address, Validators.required],
      password: [''],
      username: [''],
      archive: [''],
    });
  }
  onSubmit(){
    this.isLoading = true
    let body = {
      user: {
        userName: this.form.get("emailId")?.value,
        emailId: this.form.get("emailId")?.value,
        password: "passer123",
        firstname: this.form.get("firstname")?.value,
        lastName: this.form.get("lastName")?.value
      },
      phoneNumber: this.form.get("phoneNumber")?.value,
      address: this.form.get("address")?.value
    }
    if(this.data?.id) {
      this._etudiantService.updateEtudiant(this.data?.id, {...body, archive: 1}).subscribe({
        next: value => {
          console.log(value)
          this.isLoading = false
          this.errorMessage = "";
          this.dialogRef.close(true)
        },
        error: (error) => {
          console.log(error)
          this.isLoading = false
          this.errorMessage = error?.error?.error;
        }
      })
    }
    else{
      this._etudiantService.addEtudiant(body).subscribe({
        next: value => {
          this.errorMessage = "";
          this.isLoading = false
          console.log(value)
          this.dialogRef.close(true)
        },
        error: (error) => {
          console.log(error)
          this.isLoading = false
          this.errorMessage = error?.error?.error;
        }
      })
    }
  }
}

