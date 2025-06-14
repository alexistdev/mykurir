import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-modalprovince',
  templateUrl: './modalprovince.component.html',
  styleUrls: ['./modalprovince.component.css']
})
export class ModalprovinceComponent implements OnInit{
  @Input()
  show: boolean = false;

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
  private emailSub?: Subscription;

  constructor(private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.userForm = this.fb.group({
      id: [this.formData?.id || null],
      name: [this.formData.name || '', [Validators.required,Validators.maxLength(100)]]
    });
  }

  // ngOnChanges(changes: SimpleChanges): void {
  //   if (changes['formData'] && this.userForm) {
  //     this.userForm.reset({
  //       id: [this.formData?.id || null],
  //       name: this.formData?.name || ''
  //     });
  //   }
  // }


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
    this.userForm.reset({
      id:'',
      name: ''
    });
  }

}
