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

  loadData(page: number){
    this.pageNumber = page;
    this.userService.getUsers(this.pageNumber,10,'id','desc').subscribe({
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

}
