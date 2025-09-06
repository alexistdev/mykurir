import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Province} from "../../../models/province.model";

@Component({
  selector: 'app-modalregency',
  templateUrl: './modalregency.component.html',
  styleUrls: ['./modalregency.component.css']
})
export class ModalregencyComponent implements OnInit, OnChanges {
  @Input()
  show: boolean = false;

  @Input() provinces: Province[] = [];


  @Input()
  modalType: 'form' | 'confirm' | undefined;

  @Input()
  validateEmail: boolean | null = null;

  @Input()
  isEditMode: boolean = false;

  @Input()
  originalEmail: string = '';

  @Input()
  formData: any;

  @Input()
  confirmationText: string = '';

  @Output()
  close = new EventEmitter<void>();

  @Output()
  emailChanged = new EventEmitter<string>();

  @Output()
  formSubmit = new EventEmitter<any>();

  @Output()
  confirmDelete = new EventEmitter<void>();

  userForm !: FormGroup;

  constructor(private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.userForm = this.fb.group({
      id: this.formData?.id || null,
      name: [this.formData.name || '', [Validators.required,Validators.maxLength(100)]],
      provinceId: [this.formData?.provinceId !== undefined && this.formData?.provinceId !== null
        ? Number(this.formData.provinceId)
        : null, Validators.required]
    });

  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['formData'] && this.userForm) {
      this.userForm.reset({
        id: this.formData?.id || null,
        name: this.formData?.name || '',
        provinceId: this.formData?.provinceId !== undefined && this.formData?.provinceId !== null
          ? Number(this.formData.provinceId)
          : null
      });
    }
    // //reset default
    // if(changes['show'] && this.userForm) {
    //   this.userForm.patchValue({ provinceId: null })
    // }

    if(!this.show) {
      this.clearForm();
    }
  }

  onClose() {
    this.close.emit();
  }

  validateAndConfirm() {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }
    this.onConfirm();
  }

  onConfirm() {
    this.formSubmit.emit(this.userForm.value);
    this.clearForm();
  }

  clearForm() {
    if(this.userForm){
      this.userForm.reset({
        id:'',
        name: '',
        provinceId: null
      });
    }
  }

  deleteConfirm():void {
    this.confirmDelete.emit();
  }
}
