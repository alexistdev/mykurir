import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MasterprovinceComponent } from './masterprovince.component';

describe('MasterprovinceComponent', () => {
  let component: MasterprovinceComponent;
  let fixture: ComponentFixture<MasterprovinceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MasterprovinceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MasterprovinceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
