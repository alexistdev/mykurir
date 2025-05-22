import {AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';
import { NavigationEnd, Router} from "@angular/router";
import { filter } from "rxjs/operators";


@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements AfterViewInit, OnInit {
  @ViewChild('nanoContent', {static: false }) nanoContent!: ElementRef<HTMLDivElement>;

  isMasterDataOpen = false;

  constructor(private renderer: Renderer2, private router:Router) {
  }

  ngOnInit(): void {
    if (this.router.url.startsWith('/admin/data-user')) {
      this.isMasterDataOpen = true;
    } else {
      this.isMasterDataOpen = false;
    }

    this.router.events
      .pipe(
        filter((event): event is NavigationEnd => event instanceof NavigationEnd)
      )
      .subscribe(event => {
        if(event.urlAfterRedirects.startsWith('/admin/data-user')) {
          this.isMasterDataOpen = true;
        } else {
          this.isMasterDataOpen = false;
        }
      });
  }

  toggleLargeSidebar() {
    const htmlElement = document.documentElement;
    if (htmlElement.classList.contains('sidebar-left-collapsed')) {
      this.renderer.removeClass(htmlElement, 'sidebar-left-collapsed');
    } else {
      this.renderer.addClass(htmlElement, 'sidebar-left-collapsed');
    }
  }

  toggleMasterData() {
    this.isMasterDataOpen = !this.isMasterDataOpen;
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
