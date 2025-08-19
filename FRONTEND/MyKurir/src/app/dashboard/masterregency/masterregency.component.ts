import {Component, OnInit} from '@angular/core';
import {Province} from "../models/province.model";
import {Payload} from "../response/payload";
import {ProvinceService} from "../service/province.service";
import {Regencyrequest} from "../request/regencyrequest.model";
import {RegencyService} from "../service/regency.service";
declare var PNotify: any;

@Component({
  selector: 'app-masterregency',
  templateUrl: './masterregency.component.html',
  styleUrls: ['./masterregency.component.css']
})
export class MasterregencyComponent implements OnInit {
  provinces: Province[] = [];
  payload ?: Payload<Province>;

  public showModal = false;
  public currentModalType: 'form' | 'confirm' = 'confirm';
  public currentFormData: any = {};
  public currentConfirmationText = '';


  constructor(private provinceService: ProvinceService, private regencyService: RegencyService) {
  }

  ngOnInit(): void {
    this.loadProvince();
  }

  loadProvince(){
    const sortBy = 'id';
    const direction = 'desc';
    this.provinceService.getProvince(0, 0, sortBy, direction).subscribe((payload) => {
      this.provinces = payload.payload.content.map(province =>({
        ...province,
        name:this.capitalizeWords(province.name)
      }));
    });
  }

  capitalizeWords(input: string): string {
    return input.split(' ').map(word => {
      return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
    }).join(' ');
  }

  onDeleteConfirm(){

  }

  openModal(type: 'form' | 'confirm') {
    this.showModal = true;
    this.currentModalType = type;
  }

  doSaveData(formValue : Regencyrequest & { id?: number } ){
    const request: Regencyrequest &  { id?: number } = {
      name: formValue.name,
      provinceId : formValue.provinceId,
      id: formValue.id
    };

    this.regencyService.saveRegency(request).subscribe({
      next: () => {
        this.PNotifyMessage('success','The regency has been saved!');
        this.closeModal();
        // this.loadData(this.pageNumber, this.pageSize);
      },
      error: (err) => {
        let errorMsg = 'There is an error please contact Administrator!';
        if (err && err.error && err.error.messages && Array.isArray(err.error.messages)) {
          errorMsg = err.error.messages[0];
        }
        this.PNotifyMessage('error',errorMsg);
        this.closeModal();
        // this.loadData(this.pageNumber, this.pageSize);
      }
    });

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
}
