import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import { Apiresponse } from "../response/apiresponse";


@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8082/v1/api/users/get_all_users';


  constructor(private http:HttpClient) { }

  getUsers(page:number,size:number,sortBy:string, direction:string):Observable<Apiresponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });
    const userName = 'alexistdev@gmail.com';
    const userPw = '325339';
    // @ts-ignore
    return this.http.get<Apiresponse>(this.apiUrl+'?page='+page+'&size='+size+'&sortBy='+sortBy+'&direction='+direction, {'email': userName , 'password' : userPw},{ headers: headers,
      withCredentials: true });
  }

}
