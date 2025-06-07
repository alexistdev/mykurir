import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MasterdistrictComponent } from './masterdistrict.component';

describe('MasterdistrictComponent', () => {
  let component: MasterdistrictComponent;
  let fixture: ComponentFixture<MasterdistrictComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MasterdistrictComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MasterdistrictComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
