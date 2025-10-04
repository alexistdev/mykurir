import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Province} from "../../../models/province.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Regency} from "../../../models/regency.model";

@Component({
  selector: 'app-modaldistrict',
  templateUrl: './modaldistrict.component.html',
  styleUrls: ['./modaldistrict.component.css']
})
export class ModaldistrictComponent implements OnInit, OnChanges {
  @Input()
  show: boolean = false;

  @Input() regencies: Regency[] = [];


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
      regencyId: [this.formData?.regencyId !== undefined && this.formData?.regencyId !== null
        ? Number(this.formData.regencyId)
        : null, Validators.required]
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['formData'] && this.userForm) {
      this.userForm.reset({
        id: this.formData?.id || null,
        name: this.formData?.name || '',
        regencyId: this.formData?.regencyId !== undefined && this.formData?.regencyId !== null
          ? Number(this.formData.regencyId)
          : null
      });
    }

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
        regencyId: null
      });
    }
  }

  deleteConfirm():void {
    this.confirmDelete.emit();
  }



}
