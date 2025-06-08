import {Component, OnInit} from '@angular/core';
import {Payload} from "../response/payload";
import {Province} from "../models/province.model";
import {ProvinceService} from "../service/province.service";
import {Apiresponse} from "../response/apiresponse";

@Component({
  selector: 'app-masterprovince',
  templateUrl: './masterprovince.component.html',
  styleUrls: ['./masterprovince.component.css']
})
export class MasterprovinceComponent implements OnInit {
  provinces: Province[] = [];
  payload ?: Payload<Province>;
  totalData: number = 0;
  pageNumber: number = 0;
  totalPages: number = 0;
  pageSize: number = 0;
  keyword: string = "";
  searchQuery: string = '';

  protected readonly Number = Number;

  constructor(private provinceService: ProvinceService) {
  }

  ngOnInit(): void {
    this.loadData(this.pageNumber);
  }

  loadData(page: number, size: number = 10): void {
    this.pageNumber = page;
    this.pageSize = size;
    this.keyword = this.searchQuery;
    const sortBy = 'id';
    const direction = 'desc';
    const isFiltering = this.keyword !== "";

    const request$ = isFiltering
      ? this.provinceService.getProvinceByFilter(this.keyword, this.pageNumber, this.pageSize, sortBy, direction)
      : this.provinceService.getProvince(this.pageNumber, this.pageSize, sortBy, direction);

    request$.subscribe({
      next: (data) => this.updateProvincePageData(data),
      error: (err) => console.error(err)
    });
  }

  private updateProvincePageData(data: Apiresponse<Province>): void {
    this.payload = data.payload;
    this.pageNumber = this.payload.pageable.pageNumber;
    this.totalPages = this.payload.totalPages;
    this.pageSize = this.payload.pageable.pageSize;
    this.provinces = this.payload.content.map(province =>({
      ...province,
      name:this.capitalizeWords(province.name)
    }));
    this.totalData = this.payload.totalElements;
  }

  onSearchChange(searchTerm: string) {
    if(this.pageNumber > 0){
      this.pageNumber = 0;
    }

    this.searchQuery = searchTerm.toLowerCase();
    this.loadData(this.pageNumber, this.pageSize);
  }

  getDisplayedPages(): (number | string)[] {
    const pages: (number | string)[] = [];
    const total = this.totalPages;
    const current = this.pageNumber;

    if (total <= 10) {
      for (let i = 1; i <= total; i++) {
        pages.push(i);
      }
    } else if (current <= 9) {
      pages.push(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, '...');
    } else if (current >= total - 2) {
      pages.push('...', total - 5, total - 4, total - 3, total - 2, total - 1, total);
    } else {
      pages.push('...', current, current + 1, current + 2, current + 3, current + 4, '...');
    }

    return pages;
  }

  isNumber(value: any): boolean {
    return typeof value === 'number';
  }

  onPageSizeChange() {
    this.loadData(0, this.pageSize);
  }

  capitalizeWords(input: string): string {
    return input.split(' ').map(word => {
      return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
    }).join(' ');
  }
}
