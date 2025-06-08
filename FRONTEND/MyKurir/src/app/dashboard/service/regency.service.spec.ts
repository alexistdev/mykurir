import { TestBed } from '@angular/core/testing';

import { RegencyService } from './regency.service';

describe('RegencyService', () => {
  let service: RegencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegencyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
