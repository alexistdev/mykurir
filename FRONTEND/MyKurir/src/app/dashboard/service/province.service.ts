import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Apiresponse} from "../response/apiresponse";
import {Province} from "../models/province.model";
import {User} from "../models/user.model";
import {Userrequest} from "../request/userrequest.model";
import {Provincerequest} from "../request/provincerequest.model";

@Injectable({
  providedIn: 'root'
})
export class ProvinceService {

  private apiProvince = 'http://localhost:8082/v1/api/region/province';

  constructor(private http:HttpClient) { }

  getProvince(page:number,size:number,sortBy:string, direction:string):Observable<Apiresponse<Province>> {
    return this.http.get<Apiresponse<Province>>(
      `${this.apiProvince}?page=${page}&size=${size}&sortBy=${sortBy}&direction=${direction}`
    );
  }

  getProvinceByFilter(keyword:string, page:number,size:number,sortBy:string, direction:string):Observable<Apiresponse<Province>> {
    return this.http.get<Apiresponse<Province>>(
      `${this.apiProvince}/filter?filter=${keyword}&page=${page}&size=${size}&sortBy=${sortBy}&direction=${direction}`
    );
  }

  saveProvince(request: Provincerequest):Observable<Apiresponse<Province>>{
    return this.http.post<Apiresponse<Province>>(
      `${this.apiProvince}`,
      request,
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    );
  }
}
