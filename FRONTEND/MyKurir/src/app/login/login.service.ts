import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, Observer} from "rxjs";
import {LocalStorageService} from "../utils/local-storage.service";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor( private http:HttpClient,
               private localStorageService: LocalStorageService
  ) { }

  AuthLogin(userName: string, userPw: string): Observable<boolean> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    return new Observable((observer: Observer<any>) => {
      this.http.post<any>('http://localhost:8082/v1/api/auth/login', {'email': userName , 'password' : userPw},{ headers: headers,
        withCredentials: true })
        .subscribe({
          next: (res) => {
            if(!res || !res.payload){
              observer.next({ success: false });
              return;
            }
            let stringifiedData = JSON.stringify(res);
            let parsedJson = JSON.parse(stringifiedData);
            let data = parsedJson.payload;
            this.localStorageService.setItem("userId",data.id);
            this.localStorageService.setItem("role",data.role);
            this.localStorageService.setItem("email",data.email);
            this.localStorageService.setItem("keyPs", userPw);
            observer.next({ success: true, role: data.role });
          },
          error: (e) => {
            observer.next({ success: false });
            console.log(e)
          },
        });
    });
  }

}
