import {Component, OnInit} from '@angular/core';
import {Regency} from "../models/regency.model";
import {District} from "../models/district.model";
import {RegencyService} from "../service/regency.service";
import {DistrictService} from "../service/district.service";
import {Apiresponse} from "../response/apiresponse";
import {Regencyrequest} from "../request/regencyrequest.model";
import {Districtrequest} from "../request/districtrequest.model";
declare var PNotify: any;
@Component({
  selector: 'app-masterdistrict',
  templateUrl: './masterdistrict.component.html',
  styleUrls: ['./masterdistrict.component.css']
})
export class MasterdistrictComponent implements OnInit {
    regencies: Regency[] = [];
    districts: District[] = [];

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
    selectedDistrictId: number | undefined = 0;

    constructor(private regencyService: RegencyService, private districtService: DistrictService) {
    }

  ngOnInit(): void {
    this.loadRegency();
    this.loadData(this.pageNumber);
  }

  loadRegency(){
      const sortBy = 'id';
      const direction = 'desc';
      this.regencyService.getRegency(0, 0, sortBy, direction).subscribe((data) => {
        this.regencies = data.payload.content.map(regency =>({
          ...regency,
          name:this.capitalizeWords(regency.name)
        }));
      });
  }

  openModal(type: 'form' | 'confirm', data?: any, districtId? :number) {
    this.selectedDistrictId = districtId;
    this.currentModalType = type;
    this.showModal = true;
    if(type === 'form') {
      this.currentFormData = data || {};
    } else {
      this.currentConfirmationText = data || 'Are you sure you want to proceed?';
    }
  }

  openEditModal(district: District) {
    this.showModal = true;
    this.currentModalType = 'form';
    this.currentFormData = {
      id: district.id,
      name: district.name,
      regencyId: district.regency?.id ?? null
    };
    this.currentConfirmationText = '';
    this.currentEditMode = true;
  }

  loadData(page: number, size: number = 10): void {
    this.pageNumber = page;
    this.pageSize = size;
    this.keyword = this.searchQuery;
    const sortBy = 'id';
    const direction = 'desc';
    const isFiltering = this.keyword !== "";
    const request$ = isFiltering
      ? this.districtService.getDistrictByFilter(this.keyword,this.pageNumber,this.pageSize, sortBy,direction)
      : this.districtService.getDistrict(this.pageNumber,this.pageSize, sortBy,direction);

    request$.subscribe({
      next:(data)=> this.updateDistrictPageData(data),
      error:(err) =>console.error(err)
    })
  }

  private updateDistrictPageData(data: Apiresponse<District>):void {
    this.pageNumber = data.payload.pageable.pageNumber;
    this.totalPages = data.payload.totalPages;
    this.pageSize =  data.payload.pageable.pageSize;
    this.districts = data.payload.content.map(district => ({
      ...district,
      name:this.capitalizeWords(district.name),
      regency: {
        ...district.regency,
        name:this.capitalizeWords(district.regency.name)
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
    if(this.selectedDistrictId){
      this.districtService.deleteDistrict(this.selectedDistrictId).subscribe({
        next:() => {
          this.PNotifyMessage('warning','The district has been deleted!');
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
  }

  doSaveData(formValue : Districtrequest & { id?: number } ){
    const request: Districtrequest &  { id?: number } = {
      name: formValue.name,
      regencyId : formValue.regencyId,
      id: formValue.id
    };

    console.log(request);
    const action$ = request.id
      ? this.districtService.updateDistrict(request)
      : this.districtService.saveDistrict(request);

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

  onSearchChange(searchTerm: string) {
    if(this.pageNumber > 0){
      this.pageNumber = 0;
    }

    this.searchQuery = searchTerm.toLowerCase();
    this.loadData(this.pageNumber, this.pageSize);
  }

  onPageSizeChange() {
    this.loadData(0, this.pageSize);
  }

  onPageChanged(page: number) {
    this.pageNumber = page;
    this.loadData(this.pageNumber, this.pageSize);
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

  closeModal() {
    this.showModal = false;
  }
}
