import {Payload} from "./payload";

export interface Apiresponse {
  status: boolean;
  messages: string[];
  payload: Payload;
}
