import { Injectable } from '@angular/core';
import { ResourceService } from './resource.service';
import { Professeur } from "../models/ProfesseurModel"
import { map, tap } from 'rxjs';

const baseUrl = 'http://localhost:8090/api/v1/professeurs'; // API

@Injectable({
  providedIn: 'root'
})
export class ProfesseurService extends ResourceService<Professeur> {
  
  fetchProfesseurs() {
    return this.http
      .get<Professeur[]>(baseUrl)
      .pipe(
        map((response) => response as Professeur[]),
        tap(this.setResources.bind(this))
      );
  }

  addProfesseur(professeur: Professeur) {
    return this.http
      .post<Professeur>(baseUrl, professeur)
      .pipe(
        tap((newProfesseur) => this.upsertResource(newProfesseur))
      );
  }

  getProfesseur(id: string) {
    return this.http.get<Professeur>(`${baseUrl}/${id}`);
  }

  deleteProfesseur(id: string) {
    return this.http
      .delete<Professeur>(`${baseUrl}/${id}`)
      .pipe(tap(() => this.removeResource(id)));
  }

  updateProfesseur(id: string, professeur: Professeur) {
    return this.http
      .put<Professeur>(`${baseUrl}/${id}`, professeur)
      .pipe(
        tap((updatedProfesseur) => this.upsertResource(updatedProfesseur))
      );
  }
}