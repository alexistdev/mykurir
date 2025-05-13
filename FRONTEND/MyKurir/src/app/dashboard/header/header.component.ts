import {Component, Renderer2} from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  constructor(private renderer: Renderer2
  ) {}

  toggleSidebar() {
    const html = document.documentElement;
    if (html.classList.contains('sidebar-left-opened')) {
      this.renderer.removeClass(html, 'sidebar-left-opened');
    } else {
      this.renderer.addClass(html, 'sidebar-left-opened');
    }
  }

}
