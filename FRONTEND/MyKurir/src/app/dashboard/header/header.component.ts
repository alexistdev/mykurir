import {Component, Renderer2} from '@angular/core';
import {LocalStorageService} from "../../utils/local-storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  constructor(private renderer: Renderer2, private localStorageService:LocalStorageService,private router: Router
  ) {}

  toggleSidebar() {
    const html = document.documentElement;
    if (html.classList.contains('sidebar-left-opened')) {
      this.renderer.removeClass(html, 'sidebar-left-opened');
    } else {
      this.renderer.addClass(html, 'sidebar-left-opened');
    }
  }

  doSignOut():void{
    this.localStorageService.clearItem();
    this.router.navigate(['/']);
  }

}
