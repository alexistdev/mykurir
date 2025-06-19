import {Component, OnInit} from '@angular/core';
import {Payload} from "../response/payload";
import {Province} from "../models/province.model";
import {ProvinceService} from "../service/province.service";
import {Apiresponse} from "../response/apiresponse";
import {Userrequest} from "../request/userrequest.model";
import {Provincerequest} from "../request/provincerequest.model";
declare var PNotify: any;
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

  public showModal = false;
  public currentModalType: 'form' | 'confirm' = 'confirm';
  public currentFormData: any = {};
  public currentConfirmationText = '';

  currentEditMode:boolean = false;
  selectedProvinceId:number | undefined = 0;


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

  onPageChanged(page: number) {
    this.pageNumber = page;
    this.loadData(this.pageNumber, this.pageSize);
  }

  openModal(type: 'form' | 'confirm', data?: any,provinceId?: number) {
    this.selectedProvinceId = provinceId;
    this.currentModalType = type;
    this.showModal = true;
    if (type === 'form') {
      this.currentFormData = data || {};
    } else {
      this.currentConfirmationText = data || 'Are you sure you want to proceed?';
    }
  }

  closeModal() {
    this.showModal = false;
  }

  doSaveData(formValue: Provincerequest  & { id?: number }){
    const request: Provincerequest & { id?: number } = {
      name: formValue.name,
      id: formValue.id
    };


      this.provinceService.saveProvince(request).subscribe({
        next: () => {
          this.PNotifyMessage('success','The province has been saved!');
          this.closeModal();
          this.loadData(this.pageNumber, this.pageSize);
        },
        error: (err) => {
          let errorMsg = 'There is an error please contact Administrator!';
          if (err && err.error && err.error.messages && Array.isArray(err.error.messages)) {
            errorMsg = err.error.messages[0];
          }
          this.PNotifyMessage('error',errorMsg);
          this.closeModal();
          this.loadData(this.pageNumber, this.pageSize);
        }
      });

  }

  openEditModal(province: any) {
    this.showModal = true;
    this.currentModalType = 'form';
    this.currentFormData = { ...province };
    this.currentConfirmationText = '';
    this.currentEditMode = true;
  }

  PNotifyMessage(type:string, text: string):void{
    switch(type){
      case 'success':
        new PNotify({
          title: 'Success',
          text: text,
          type: 'success'
        });
        break;
      case 'warning':
        new PNotify({
          title: 'Success',
          text: text,
          type: 'warning'
        });
        break;
      default:
        new PNotify({
          title: 'Error !',
          text: text,
          type: 'error'
        });
    }
  }

  onDeleteConfirm(){
    if (this.selectedProvinceId) {
      this.provinceService.deleteProvince(this.selectedProvinceId).subscribe({
        next: () => {
          this.PNotifyMessage('warning','The province has been deleted!');
          this.closeModal();
          this.loadData(this.pageNumber, this.pageSize);
        },
        error: () => {
          this.PNotifyMessage('error','There is an error please contact Administrator!');
          this.closeModal();
          this.loadData(this.pageNumber, this.pageSize);
        }
      })
    }
    this.closeModal();
  }

}
