import {Component, OnInit} from '@angular/core';
import {User} from "../models/user.model";
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
    this.provinceService.getProvince().subscribe({
      next:(data) => {
        // console.log('Province data:', data);
        this.updateProvincePageData(data)
      },
      error: (err) => console.error(err)
    });

    // const request$ = isFiltering
    //   ? this.provinceService.getUsersByFilter(this.keyword, this.pageNumber, this.pageSize, sortBy, direction)
    //   : this.provinceService.getUsers(this.pageNumber, this.pageSize, sortBy, direction);

    // request$.subscribe({
    //   next: (data) => this.updateUserPageData(data),
    //   error: (err) => console.error(err)
    // });
  }

  private updateProvincePageData(data: Apiresponse<Province>): void {
    this.payload = data.payload;
    this.pageNumber = this.payload.pageable.pageNumber;
    this.totalPages = this.payload.totalPages;
    this.pageSize = this.payload.pageable.pageSize;
    this.provinces = this.payload.content;
    this.totalData = this.payload.totalElements;
    console.log(this.provinces);
    // this.province = this.payload.content.map(province => ({
    //   ...province,
    //   fullName: this.capitalizeWords(user.fullName)
    // }));
    // this.totalData = this.payload.totalElements;
  }
}
