import {Component, OnInit} from '@angular/core';
import {Province} from "../models/province.model";
import {Payload} from "../response/payload";
import {ProvinceService} from "../service/province.service";

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


  constructor(private provinceService: ProvinceService) {
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

  doSaveData(){

  }

  closeModal() {
    this.showModal = false;
  }
}
