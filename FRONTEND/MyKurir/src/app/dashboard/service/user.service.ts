import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import { Apiresponse } from "../response/apiresponse";
import {Userrequest} from "../request/userrequest.model";
import {User} from "../models/user.model";


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

  getUsers(page:number,size:number,sortBy:string, direction:string):Observable<Apiresponse<User>> {
    return this.http.get<Apiresponse<User>>(
      `${this.apiUrl1}?page=${page}&size=${size}&sortBy=${sortBy}&direction=${direction}`
    );
  }

  getUsersByFilter(keyword:string, page:number,size:number,sortBy:string, direction:string):Observable<Apiresponse<User>> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    return this.http.get<Apiresponse<User>>(this.apiUrl2+'?filter='+keyword+'&page='+page+'&size='+size+'&sortBy='+sortBy+'&direction='+direction, { headers: headers,
      withCredentials: true });
  }

  saveUser(request: Userrequest):Observable<Apiresponse<User>>{
    return this.http.post<Apiresponse<User>>(
      `${this.apiRegister}`,
      request,
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    );
  }

  updateUser(request: Userrequest,userId:number):Observable<Apiresponse<User>>{
    return this.http.put<Apiresponse<User>>(
      `${this.apiRegister}`+'/'+`${userId}`,
      request,
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    );
  }

  validateEmail(email: string): Observable<any> {
    return this.http.get<Apiresponse<User>>(
      `${this.apiValidateEmail}?email=${email}`
    );
  }

  deleteUser(userId:number):Observable<Apiresponse<User>>{
    return this.http.delete<Apiresponse<User>>(
      `${this.apiDeleteUser}`+'/'+`${userId}`,
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    );
  }



}
