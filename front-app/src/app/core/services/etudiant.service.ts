import { Injectable } from '@angular/core';
import {ResourceService} from './resource.service';
import {Etudiant} from '../models/EtudiantModel';
import {map, tap} from 'rxjs';
let baseUrl = "http://localhost:8090/api/v1/etudiants"

@Injectable({
  providedIn: 'root'
})
export class EtudiantService extends ResourceService<Etudiant>{

  fetchEtudiants() {
    return this.http
      .get<Etudiant[]>(baseUrl)
      .pipe(
        map((response) => response as Etudiant[]),
        tap(this.setResources.bind(this))
      );
  }

  addEtudiant(etudiant: Etudiant) {
    return this.http
      .post<Etudiant>(baseUrl, etudiant)
      .pipe(
        tap((newEtudiant) => this.upsertResource(newEtudiant))
      );
  }

  getEtudiant(id: number) {
    return this.http
      .get<Etudiant>(baseUrl+ "/"+ id)
  }

  deleteEtudiant(id: number) {
    return this.http
      .delete<Etudiant>(`${baseUrl}/${id}`)
      .pipe(tap(() => this.removeResource(id)));
  }

  updateEtudiant(id:number, etudiant: Etudiant) {
    return this.http
      .put<Etudiant>(`${baseUrl}/${id}`, etudiant)
      .pipe(
        tap((newEtudiant) => this.upsertResource(newEtudiant))
      );
  }
}

