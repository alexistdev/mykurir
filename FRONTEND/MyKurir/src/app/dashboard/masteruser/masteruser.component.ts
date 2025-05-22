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

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getUsers().subscribe({
      next: (data) => {
        this.payload = data.payload;
        this.users = this.payload.content;
        this.totalData = this.payload.totalElements;
      },
      error: (err) => {
        console.error('Error fetching users', err);
      }
    });
  }
}
