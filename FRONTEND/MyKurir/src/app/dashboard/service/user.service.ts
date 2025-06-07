import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import { Apiresponse } from "../response/apiresponse";
import {Userrequest} from "../request/userrequest.model";


@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl1 = 'http://localhost:8082/v1/api/users/get_all_users';
  private apiUrl2 = 'http://localhost:8082/v1/api/users/get_user_by_filter';
  private apiDeleteUser =  'http://localhost:8082/v1/api/users';
  private apiRegister = 'http://localhost:8082/v1/api/auth/register';
  private apiValidateEmail = 'http://localhost:8082/v1/api/users/validate_email';


  constructor(private http:HttpClient) { }

  getUsers(page:number,size:number,sortBy:string, direction:string):Observable<Apiresponse> {
    return this.http.get<Apiresponse>(
      `${this.apiUrl1}?page=${page}&size=${size}&sortBy=${sortBy}&direction=${direction}`
    );
  }

  getUsersByFilter(keyword:string, page:number,size:number,sortBy:string, direction:string):Observable<Apiresponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    return this.http.get<Apiresponse>(this.apiUrl2+'?filter='+keyword+'&page='+page+'&size='+size+'&sortBy='+sortBy+'&direction='+direction, { headers: headers,
      withCredentials: true });
  }

  saveUser(request: Userrequest):Observable<Apiresponse>{
    return this.http.post<Apiresponse>(
      `${this.apiRegister}`,
      request,
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    );
  }

  updateUser(request: Userrequest,userId:number):Observable<Apiresponse>{
    return this.http.put<Apiresponse>(
      `${this.apiRegister}`+'/'+`${userId}`,
      request,
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    );
  }

  validateEmail(email: string): Observable<any> {
    return this.http.get<Apiresponse>(
      `${this.apiValidateEmail}?email=${email}`
    );
  }

  deleteUser(userId:number):Observable<Apiresponse>{
    return this.http.delete<Apiresponse>(
      `${this.apiDeleteUser}`+'/'+`${userId}`,
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    );
  }



}
