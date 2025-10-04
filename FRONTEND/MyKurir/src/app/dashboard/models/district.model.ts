export interface District {
  id: number;
  name: string;
  regency: {
    id: number,
    name: string,
  }
  createdDate: Date;
  modifiedDate: Date;
}
