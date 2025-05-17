import {Component, Renderer2} from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

  constructor(private renderer: Renderer2) {}

  toggleLargeSidebar() {
    const htmlElement = document.documentElement; // <html> tag
    if (htmlElement.classList.contains('sidebar-left-collapsed')) {
      this.renderer.removeClass(htmlElement, 'sidebar-left-collapsed');
    } else {
      this.renderer.addClass(htmlElement, 'sidebar-left-collapsed');
    }
  }

}
