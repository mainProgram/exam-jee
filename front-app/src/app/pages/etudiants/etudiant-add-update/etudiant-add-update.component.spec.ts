import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EtudiantAddUpdateComponent } from './etudiant-add-update.component';

describe('EtudiantAddUpdateComponent', () => {
  let component: EtudiantAddUpdateComponent;
  let fixture: ComponentFixture<EtudiantAddUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EtudiantAddUpdateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EtudiantAddUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
