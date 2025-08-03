import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalregencyComponent } from './modalregency.component';

describe('ModalregencyComponent', () => {
  let component: ModalregencyComponent;
  let fixture: ComponentFixture<ModalregencyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalregencyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalregencyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
