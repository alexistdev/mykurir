import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { RouterModule, Routes } from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {LoginService} from "./login.service";
import {HttpClientModule} from "@angular/common/http";


const routes: Routes = [
  { path: '', component: LoginComponent }  // Default route for this module
];

@NgModule({
  declarations: [
    LoginComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [
    LoginService
  ]
})
export class LoginModule { }
