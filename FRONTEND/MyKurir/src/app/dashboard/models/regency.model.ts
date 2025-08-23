export interface Regency {
  id: number;
  name: string;
  province: {
    id: number,
    name: string,
  }
  createdDate: Date;
  modifiedDate: Date;
}
