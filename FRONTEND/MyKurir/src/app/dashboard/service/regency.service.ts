import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Regencyrequest} from "../request/regencyrequest.model";
import {Observable} from "rxjs";
import {Apiresponse} from "../response/apiresponse";
import {Regency} from "../models/regency.model";

@Injectable({
  providedIn: 'root'
})
export class RegencyService {

  private apiRegency: string = 'http://localhost:8082/v1/api/region/regency';

  constructor(private http:HttpClient) { }

  saveRegency(request: Regencyrequest):Observable<Apiresponse<Regency>>{
    return this.http.post<Apiresponse<Regency>>(
      `${this.apiRegency}`,
      request,
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    );
  }

  getRegency(page:number,size:number,sortBy:string, direction:string):Observable<Apiresponse<Regency>>{
    return this.http.get<Apiresponse<Regency>>(
      `${this.apiRegency}?page=${page}&size=${size}&sortBy=${sortBy}&direction=${direction}`
    );
  }
}
