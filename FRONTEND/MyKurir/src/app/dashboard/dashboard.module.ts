import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import {RouterModule, Routes} from "@angular/router";
import { NotificationComponent } from './notification/notification.component';
import { HeaderComponent } from './header/header.component';
import { MasteruserComponent } from './masteruser/masteruser.component';
import {FormsModule} from "@angular/forms";

const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'data-user', component: MasteruserComponent },
]

@NgModule({
  declarations: [
    DashboardComponent,
    SidebarComponent,
    NotificationComponent,
    HeaderComponent,
    MasteruserComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(routes)
  ]
})
export class DashboardModule { }
