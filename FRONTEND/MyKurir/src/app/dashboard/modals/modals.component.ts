import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {debounceTime} from "rxjs";
import {filter} from "rxjs/operators";


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
  validateEmail: boolean | null = null;

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


  userForm !: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      fullName: [this.formData.fullName || '', Validators.required],
      email: [this.formData.email || '', [Validators.required, Validators.email]],
      password: [this.formData.password || '', [Validators.required, Validators.minLength(6)]]
    });

    this.userForm.get('email')?.statusChanges
      .pipe(
        debounceTime(300),
        filter(status => status === 'VALID')
      )
      .subscribe(() => {
        const email = this.userForm.get('email')?.value;
        this.emailChanged.emit(email);
      });
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
    if(this.validateEmail === false ){
      this.userForm.markAllAsTouched();
      return;
    }
    this.onConfirm();
  }

  onClose() {
    this.close.emit();
  }

  onConfirm() {
    this.formSubmit.emit(this.userForm.value);
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
