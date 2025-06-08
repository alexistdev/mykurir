import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {debounceTime, Subscription} from "rxjs";
import {filter} from "rxjs/operators";


@Component({
  selector: 'app-modals',
  templateUrl: './modals.component.html',
  styleUrls: ['./modals.component.css']
})
export class ModalsComponent implements OnInit, OnChanges, OnDestroy {

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
      fullName: [this.formData.fullName || '', Validators.required],
      email: [
        this.formData.email || '',
        [Validators.required, Validators.email]
      ],
      password: [
        this.formData.password || '',
        this.isEditMode ? [Validators.minLength(6)] : [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnChanges(changes: SimpleChanges): void {

    if (changes['formData'] && this.userForm) {
      this.userForm.reset({
        id: [this.formData?.id || null],
        fullName: this.formData?.fullName || '',
        email: this.formData?.email || '',
        password: this.formData?.password || ''
      });
    }

    if (this.userForm) {
      const passwordCtrl = this.userForm.get('password');

      if (passwordCtrl) {
        if (this.isEditMode) {
          passwordCtrl.setValidators([Validators.minLength(6)]);
        } else {
          passwordCtrl.setValidators([Validators.required, Validators.minLength(6)]);
        }
        passwordCtrl.updateValueAndValidity();
      }


      if (this.emailSub) {
        this.emailSub.unsubscribe();
      }

      this.emailSub = this.userForm.get('email')?.valueChanges
        .pipe(debounceTime(300))
        .subscribe((email: string) => {
          if (this.isEditMode) {
            if (email !== this.originalEmail) {
              this.emailChanged.emit(email);
            } else {
              this.emailChanged.emit('ORIGINAL_EMAIL_NO_VALIDATION');
            }
          } else {
            this.emailChanged.emit(email);
          }
        });
    }
  }

  ngOnDestroy(): void {
    if (this.emailSub) {
      this.emailSub.unsubscribe();
    }
  }

  validateAndConfirm() {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }
    if (this.validateEmail === false) {
      this.userForm.markAllAsTouched();
      return;
    }
    this.onConfirm();
  }

  deleteConfirm():void {
    this.confirmDelete.emit();
  }

  onClose() {
    this.close.emit();
  }

  onConfirm() {
    this.formSubmit.emit(this.userForm.value);
    this.clearForm();
  }

  clearForm() {
    this.userForm.reset({
      id:'',
      fullName: '',
      email: '',
      password: ''
    });
  }

}
