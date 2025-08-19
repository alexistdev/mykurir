import {Province} from "./province.model";

export interface Regency {
  id: number;
  name: string;
  province: Province;
  createdDate: Date;
  modifiedDate: Date;
}
