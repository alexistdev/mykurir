import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MasterregencyComponent } from './masterregency.component';

describe('MasterregencyComponent', () => {
  let component: MasterregencyComponent;
  let fixture: ComponentFixture<MasterregencyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MasterregencyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MasterregencyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
