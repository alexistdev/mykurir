import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Apiresponse} from "../response/apiresponse";
import {District} from "../models/district.model";
import {Districtrequest} from "../request/districtrequest.model";
import {Regency} from "../models/regency.model";

@Injectable({
  providedIn: 'root'
})
export class DistrictService {

  private apiDistrict = 'http://localhost:8082/v1/api/region/district';

  constructor(private http:HttpClient) { }

  saveDistrict(request: Districtrequest):Observable<Apiresponse<District>>{
    return this.http.post<Apiresponse<District>>(
      `${this.apiDistrict}`,
      request,
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    );
  }

  updateDistrict(request: Districtrequest):Observable<Apiresponse<District>>{
    return this.http.patch<Apiresponse<District>>(
      `${this.apiDistrict}`,
      request,
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    );
  }

  getDistrict(page:number,size:number,sortBy:string, direction:string):Observable<Apiresponse<District>>{
    return this.http.get<Apiresponse<District>>(
      `${this.apiDistrict}?page=${page}&size=${size}&sortBy=${sortBy}&direction=${direction}`
    );
  }

  getDistrictByFilter(keyword:string, page:number,size:number,sortBy:string, direction:string):Observable<Apiresponse<District>> {
    return this.http.get<Apiresponse<District>>(
      `${this.apiDistrict}/filter?filter=${keyword}&page=${page}&size=${size}&sortBy=${sortBy}&direction=${direction}`
    );
  }

  deleteDistrict(districtId:number):Observable<Apiresponse<District>>{
    return this.http.delete<Apiresponse<District>>(
      `${this.apiDistrict}`+'/'+`${districtId}`,
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    );
  }
}
