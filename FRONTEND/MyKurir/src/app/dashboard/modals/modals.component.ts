import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-modals',
  templateUrl: './modals.component.html',
  styleUrls: ['./modals.component.css']
})
export class ModalsComponent {

  @Input() show: boolean = false;
  @Input() modalType: 'form' | 'confirm' = 'confirm'; // or use an enum for more options
  @Input() formData: any;
  @Input() confirmationText: string = '';
  @Output() close = new EventEmitter<void>();
  @Output() confirm = new EventEmitter<void>();


  onClose() {
    this.close.emit(); // Notify parent to hide modal
  }

  onConfirm() {
    this.confirm.emit();
  }

}
