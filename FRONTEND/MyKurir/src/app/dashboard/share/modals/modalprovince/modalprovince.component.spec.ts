import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalprovinceComponent } from './modalprovince.component';

describe('ModalprovinceComponent', () => {
  let component: ModalprovinceComponent;
  let fixture: ComponentFixture<ModalprovinceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalprovinceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalprovinceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
