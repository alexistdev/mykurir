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
import { MasterprovinceComponent } from './masterprovince/masterprovince.component';
import { MasterregencyComponent } from './masterregency/masterregency.component';
import { MasterdistrictComponent } from './masterdistrict/masterdistrict.component';
import { PaginationComponent } from './share/pagination/pagination.component';
import { ModalprovinceComponent } from './share/modals/modalprovince/modalprovince.component';

const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'data-user', component: MasteruserComponent },
  { path: 'data-province', component: MasterprovinceComponent },
  { path: 'data-regency', component: MasterregencyComponent },
  { path: 'data-district', component: MasterdistrictComponent },
]

@NgModule({
  declarations: [
    DashboardComponent,
    SidebarComponent,
    NotificationComponent,
    HeaderComponent,
    MasteruserComponent,
    ModalsComponent,
    MasterprovinceComponent,
    MasterregencyComponent,
    MasterdistrictComponent,
    PaginationComponent,
    ModalprovinceComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ]
})
export class DashboardModule { }
