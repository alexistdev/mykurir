import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard/dashboard.component";
import {LoginComponent} from "./login/login/login.component";
import {AuthGuard} from "./auth.guard";

const routes: Routes = [

  { path: '', redirectTo: '/login', pathMatch: 'full'},
  { path:'login',
    loadChildren:() => import('./login/login.module').then(m=> m.LoginModule) ,
    canActivate: [AuthGuard],
    data: { login: true }
  },

  { path: 'admin',
    loadChildren:() => import('./dashboard/dashboard.module').then(m=>m.DashboardModule),
    canActivate: [AuthGuard]
  },
  { path: '**', loadChildren:()=>import('./notfound/notfound.module').then(m=>m.NotfoundModule) },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
