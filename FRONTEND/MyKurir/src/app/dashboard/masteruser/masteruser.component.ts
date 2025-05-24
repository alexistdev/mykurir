import {Component, OnInit} from '@angular/core';
import {User} from "../models/user.model";
import {UserService} from "../service/user.service";
import {Payload} from "../response/payload";

@Component({
  selector: 'app-masteruser',
  templateUrl: './masteruser.component.html',
  styleUrls: ['./masteruser.component.css']
})
export class MasteruserComponent implements OnInit{

   users: User[] = [];
   payload ?: Payload;
   totalData: number = 0;
   pageNumber:number = 0;
   totalPages: number = 0;
   pageSize: number = 0;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadData(this.pageNumber);
  }

  loadData(page: number, size: number = 10){
    this.pageNumber = page;
    this.pageSize = size;
    this.userService.getUsers(this.pageNumber,this.pageSize ,'id','desc').subscribe({
      next: (data) => {
        this.payload = data.payload;
        this.pageNumber = this.payload.pageable.pageNumber;
        this.totalPages = this.payload.totalPages;
        this.pageSize = this.payload.pageable.pageSize;
        this.users = this.payload.content.map(user => ({
          ...user,
          fullName: this.capitalizeWords(user.fullName)
        }))
        this.totalData = this.payload.totalElements;
      },
      error: (err) => {
        console.error('Error fetching users', err);
      }
    });
  }

  capitalizeWords(input: string): string {
    return input.split(' ').map(word => {
      return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
    }).join(' ');
  }

  getDisplayedPages(): (number | string)[] {
    const pages: (number | string)[] = [];
    const total = this.totalPages;
    const current = this.pageNumber;

    if (total <= 10) {
      for (let i = 1; i <= total; i++) {
        pages.push(i);
      }
    } else if (current <= 9) {
      pages.push(1, 2, 3, 4,5,6,7,8,9,10, '...');
    } else if (current >= total - 2) {
      pages.push('...',total - 5,total - 4 , total - 3,total - 2, total - 1, total);
    } else {
      pages.push('...', current, current + 1, current + 2, current + 3, current + 4, '...');
    }

    return pages;
  }

  isNumber(value: any): boolean {
    return typeof value === 'number';
  }

  onPageSizeChange() {
    this.loadData(0, this.pageSize);
  }


  protected readonly Number = Number;
}
