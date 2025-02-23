import { TestBed } from '@angular/core/testing';

import { TranslateMatPaginatorService } from './translate-mat-paginator.service';

describe('TranslateMatPaginatorService', () => {
  let service: TranslateMatPaginatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TranslateMatPaginatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
