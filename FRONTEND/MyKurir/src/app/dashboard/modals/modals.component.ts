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
  modalType: 'form' | 'confirm' = 'confirm';

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


  userForm !: FormGroup;
  private emailSub?: Subscription;


  constructor(private fb: FormBuilder) {
  }

  ngOnInit(): void {

    this.userForm = this.fb.group({
      fullName: [this.formData.fullName || '', Validators.required],
      email: [
        this.formData.email || '',
        [Validators.required, Validators.email]
      ],
      password: [this.formData.password || '', [Validators.required, Validators.minLength(6)]]
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
    const emailControl = this.userForm.get('email');

    if (this.userForm) {
      if (this.emailSub) {
        this.emailSub.unsubscribe();
      }
      // @ts-ignore
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

  onClose() {
    this.close.emit();
  }

  onConfirm() {
    this.formSubmit.emit(this.userForm.value);
    this.clearForm();
  }

  clearForm() {
    this.userForm.reset({
      fullName: '',
      email: '',
      password: ''
    });
  }

}
