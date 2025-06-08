import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Apiresponse} from "../response/apiresponse";
import {Province} from "../models/province.model";

@Injectable({
  providedIn: 'root'
})
export class ProvinceService {

  private apiProvince = 'http://localhost:8082/v1/api/region/province';

  constructor(private http:HttpClient) { }


  // getProvince(page:number,size:number,sortBy:string, direction:string):Observable<Apiresponse> {
  //   return this.http.get<Apiresponse>(
  //     `${this.apiUrl1}?page=${page}&size=${size}&sortBy=${sortBy}&direction=${direction}`
  //   );
  // }
  getProvince():Observable<Apiresponse<Province>> {
    return this.http.get<Apiresponse<Province>>(
      `${this.apiProvince}`
    );
  }
}
