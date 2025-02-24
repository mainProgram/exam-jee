import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfesseurAddUpdateComponent } from './professeur-add-update.component';

describe('ProfesseurAddUpdateComponent', () => {
  let component: ProfesseurAddUpdateComponent;
  let fixture: ComponentFixture<ProfesseurAddUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfesseurAddUpdateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfesseurAddUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
