import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModaldistrictComponent } from './modaldistrict.component';

describe('ModaldistrictComponent', () => {
  let component: ModaldistrictComponent;
  let fixture: ComponentFixture<ModaldistrictComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModaldistrictComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModaldistrictComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
