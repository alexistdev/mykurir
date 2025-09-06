import {Component, OnInit} from '@angular/core';
import {Province} from "../models/province.model";
import {Payload} from "../response/payload";
import {ProvinceService} from "../service/province.service";
import {Regencyrequest} from "../request/regencyrequest.model";
import {RegencyService} from "../service/regency.service";
import {Regency} from "../models/regency.model";
import {Apiresponse} from "../response/apiresponse";
declare var PNotify: any;

@Component({
  selector: 'app-masterregency',
  templateUrl: './masterregency.component.html',
  styleUrls: ['./masterregency.component.css']
})
export class MasterregencyComponent implements OnInit {
  provinces: Province[] = [];
  regencies: Regency[] = [];
  // payload ?: Payload<any>;
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
  selectedRegencyId: number | undefined = 0;


  constructor(private provinceService: ProvinceService, private regencyService: RegencyService) {
  }

  ngOnInit(): void {
    this.loadProvince();
    this.loadData(this.pageNumber);
  }

  loadProvince(){
    const sortBy = 'id';
    const direction = 'desc';
    this.provinceService.getProvince(0, 0, sortBy, direction).subscribe((data) => {
      this.provinces = data.payload.content.map(province =>({
        ...province,
        name:this.capitalizeWords(province.name)
      }));
    });
  }

  loadData(page: number, size: number = 10): void {
    this.pageNumber = page;
    this.pageSize = size;
    this.keyword = this.searchQuery;
    const sortBy = 'id';
    const direction = 'desc';
    const isFiltering = this.keyword !== "";
    const request$ = isFiltering
      ? this.regencyService.getRegencyByFilter(this.keyword,this.pageNumber,this.pageSize, sortBy,direction)
      : this.regencyService.getRegency(this.pageNumber,this.pageSize, sortBy,direction);

    request$.subscribe({
      next:(data)=> this.updateRegencyPageData(data),
      error:(err) =>console.error(err)
    })
  }

  private updateRegencyPageData(data: Apiresponse<Regency>):void {
    this.pageNumber = data.payload.pageable.pageNumber;
    this.totalPages = data.payload.totalPages;
    this.pageSize =  data.payload.pageable.pageSize;
    this.regencies = data.payload.content.map(regency => ({
      ...regency,
        name:this.capitalizeWords(regency.name),
        province: {
            ...regency.province,
          name:this.capitalizeWords(regency.province.name)
        }
    }))
    this.totalData = data.payload.totalElements;
  }

  capitalizeWords(input: string): string {
    return input.split(' ').map(word => {
      return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
    }).join(' ');
  }



  onDeleteConfirm(){
    if(this.selectedRegencyId){
     this.regencyService.deleteRegency(this.selectedRegencyId).subscribe({
       next: () => {
         this.PNotifyMessage('warning','The regency has been deleted!');
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

  openModal(type: 'form' | 'confirm', data?: any, regencyId? :number) {
    this.selectedRegencyId = regencyId;
    this.currentModalType = type;
    this.showModal = true;
    if(type === 'form') {
      this.currentFormData = data || {};
    } else {
      this.currentConfirmationText = data || 'Are you sure you want to proceed?';
    }
  }

  openEditModal(regency: Regency) {
    this.showModal = true;
    this.currentModalType = 'form';
    this.currentFormData = {
      id: regency.id,
      name: regency.name,
      provinceId: regency.province?.id ?? null
    };
    this.currentConfirmationText = '';
    this.currentEditMode = true;
  }

  doSaveData(formValue : Regencyrequest & { id?: number } ){
    const request: Regencyrequest &  { id?: number } = {
      name: formValue.name,
      provinceId : formValue.provinceId,
      id: formValue.id
    };

    const action$ = request.id
      ? this.regencyService.updateRegency(request)
      : this.regencyService.saveRegency(request);

    console.log(request);

    action$.subscribe({
      next: () => {
        this.PNotifyMessage(
          'success',
          `The regency has been ${request.id ? 'updated' : 'saved'}!`
        );
        this.loadData(this.pageNumber, this.pageSize);
        this.closeModal();
      },
      error: (err) => {
        let errorMessage = 'There is an error please contact Administrator!';
        // if(err && err.error && err.error.messages && Array.isArray(err.error.messages)){
        //   errorMessage = err.error.messages[0];
        // }
        this.PNotifyMessage('error', errorMessage);
        this.closeModal();
        this.loadData(this.pageNumber,this.pageSize);
      }
    })
    this.closeModal();
  }

  closeModal() {
    this.showModal = false;
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

  onPageSizeChange() {
    this.loadData(0, this.pageSize);
  }

  onSearchChange(searchTerm: string) {
    if(this.pageNumber > 0){
      this.pageNumber = 0;
    }

    this.searchQuery = searchTerm.toLowerCase();
    this.loadData(this.pageNumber, this.pageSize);
  }

  onPageChanged(page: number) {
    this.pageNumber = page;
    this.loadData(this.pageNumber, this.pageSize);
  }
}
