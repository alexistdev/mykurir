import {User} from "../models/user.model";
import {Pageable} from "./pageable";
import {SortData} from "./sort-data";

export interface Payload<T> {
  content: T[];
  pageable: Pageable;
  last: boolean;
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  sort: SortData;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}
