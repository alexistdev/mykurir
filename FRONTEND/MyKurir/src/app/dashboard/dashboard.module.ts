import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import {RouterModule, Routes} from "@angular/router";
import { NotificationComponent } from './notification/notification.component';
import { HeaderComponent } from './header/header.component';
import { MasteruserComponent } from './masteruser/masteruser.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { ModalsComponent } from './modals/modals.component';

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
    MasteruserComponent,
    ModalsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ]
})
export class DashboardModule { }
