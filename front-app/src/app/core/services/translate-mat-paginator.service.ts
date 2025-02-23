import { Injectable } from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';

@Injectable({
  providedIn: 'root'
})
export class TranslateMatPaginatorService {

  constructor() { }

  translateMatPaginator(paginator: MatPaginator) {
    if(paginator){
      paginator._intl.firstPageLabel = 'Première page';
      paginator._intl.itemsPerPageLabel = 'Éléments par page';
      paginator._intl.lastPageLabel = 'Dernière page';
      paginator._intl.nextPageLabel = 'Page suivante';
      paginator._intl.previousPageLabel = 'Page précédente';
    }
  }
}
