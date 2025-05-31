import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";


@Component({
  selector: 'app-modals',
  templateUrl: './modals.component.html',
  styleUrls: ['./modals.component.css']
})
export class ModalsComponent implements OnInit,OnChanges {

  @Input()
  show: boolean = false;

  @Input()
  modalType: 'form' | 'confirm' = 'confirm';

  @Input()
  formData: any;

  @Input()
  confirmationText: string = '';

  @Output()
  close = new EventEmitter<void>();

  @Output()
  confirm = new EventEmitter<void>();

  userForm !: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      fullName: [this.formData.fullName || '', Validators.required],
      email: [this.formData.email || '', [Validators.required, Validators.email]],
      password: [this.formData.password || '', [Validators.required, Validators.minLength(6)]]
    })
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['formData'] && this.userForm) {
      this.userForm.reset({
        fullName: this.formData?.fullName || '',
        email: this.formData?.email || '',
        password: this.formData?.password || ''
      });
    }
  }

  validateAndConfirm() {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }
    this.onConfirm();
  }

  onClose() {
    this.close.emit();
  }

  onConfirm() {
    this.confirm.emit();
    this.clearForm();
  }

  clearForm(){
    this.userForm.reset({
      fullName:'',
      email:'',
      password:''
    });
  }

}
