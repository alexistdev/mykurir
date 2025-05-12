import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotfoundComponent } from './notfound/notfound.component';
import {RouterModule, Routes} from "@angular/router";
import {DashboardComponent} from "../dashboard/dashboard/dashboard.component";

const routes: Routes = [
  { path: '', component: NotfoundComponent }
]

@NgModule({
  declarations: [
    NotfoundComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class NotfoundModule { }
