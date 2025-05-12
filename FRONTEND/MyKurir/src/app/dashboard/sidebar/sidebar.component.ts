import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';


@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements AfterViewInit {
  @ViewChild('nanoContent', {static: false }) nanoContent!: ElementRef<HTMLDivElement>;

  isEcommerceOpen = false;

  toggleEcommerce(event: Event) {
    event.preventDefault();
    this.isEcommerceOpen = !this.isEcommerceOpen;
  }

  ngAfterViewInit(): void {
    if (typeof localStorage !== 'undefined') {
      const stored = localStorage.getItem('sidebar-left-position');
      if (stored !== null && this.nanoContent) {
        this.nanoContent.nativeElement.scrollTop = Number(stored);
      }
    }
  }

  onScroll(): void {
    if (typeof localStorage !== 'undefined' && this.nanoContent) {
      localStorage.setItem(
        'sidebar-left-position',
        this.nanoContent.nativeElement.scrollTop.toString()
      );
    }
  }

}
