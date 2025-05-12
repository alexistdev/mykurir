import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {LoginModule} from "./login/login.module";
import {DashboardModule} from "./dashboard/dashboard.module";
import {NotfoundModule} from "./notfound/notfound.module";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    LoginModule,
    DashboardModule,
    NotfoundModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
