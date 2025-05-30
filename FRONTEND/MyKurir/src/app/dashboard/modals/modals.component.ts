import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-modals',
  templateUrl: './modals.component.html',
  styleUrls: ['./modals.component.css']
})
export class ModalsComponent {

  @Input()
  show = false;

  @Output()
  close = new EventEmitter<void>();

  onClose() {
    this.close.emit(); // Notify parent to hide modal
  }


}
