import {Component, OnInit} from '@angular/core';
import {User} from "../models/user.model";
import {UserService} from "../service/user.service";
import {Payload} from "../response/payload";
import {Userrequest} from "../request/userrequest.model";
import {Apiresponse} from "../response/apiresponse";

declare var PNotify: any;

@Component({
  selector: 'app-masteruser',
  templateUrl: './masteruser.component.html',
  styleUrls: ['./masteruser.component.css']
})
export class MasteruserComponent implements OnInit {

  users: User[] = [];
  payload ?: Payload;
  totalData: number = 0;
  pageNumber: number = 0;
  totalPages: number = 0;
  pageSize: number = 0;
  keyword: string = "";
  searchQuery: string = '';
  public showModal = false;
  public currentModalType: 'form' | 'confirm' = 'confirm';
  public currentFormData: any = {};
  public currentConfirmationText = '';
  validateEmail:boolean = true;
  currentEditMode:boolean = false;
  selectedUserId:number | undefined = 0;



  protected readonly Number = Number;

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.loadData(this.pageNumber);
  }

  onSearchChange(searchTerm: string) {
    if(this.pageNumber > 0){
      this.pageNumber = 0; //force to first page
    }

    this.searchQuery = searchTerm.toLowerCase();
    this.loadData(this.pageNumber, this.pageSize);
  }

  loadData(page: number, size: number = 10): void {
    this.pageNumber = page;
    this.pageSize = size;
    this.keyword = this.searchQuery;
    const sortBy = 'id';
    const direction = 'desc';
    const isFiltering = this.keyword !== "";

    const request$ = isFiltering
      ? this.userService.getUsersByFilter(this.keyword, this.pageNumber, this.pageSize, sortBy, direction)
      : this.userService.getUsers(this.pageNumber, this.pageSize, sortBy, direction);

    request$.subscribe({
      next: (data) => this.updateUserPageData(data),
      error: (err) => console.error(err)
    });
  }

  private updateUserPageData(data: Apiresponse): void {
    this.payload = data.payload;
    this.pageNumber = this.payload.pageable.pageNumber;
    this.totalPages = this.payload.totalPages;
    this.pageSize = this.payload.pageable.pageSize;
    this.users = this.payload.content.map(user => ({
      ...user,
      fullName: this.capitalizeWords(user.fullName)
    }));
    this.totalData = this.payload.totalElements;
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
      pages.push(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, '...');
    } else if (current >= total - 2) {
      pages.push('...', total - 5, total - 4, total - 3, total - 2, total - 1, total);
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

  openModal(type: 'form' | 'confirm', data?: any,userId?: number) {
    this.selectedUserId = userId;
    this.currentModalType = type;
    this.showModal = true;
    if (type === 'form') {
      this.currentFormData = data || {};
      this.validateEmail = true;
    } else {
      this.currentConfirmationText = data || 'Are you sure you want to proceed?';
    }
  }

  openEditModal(user: any) {
    this.showModal = true;
    this.currentModalType = 'form';
    this.currentFormData = { ...user };
    this.currentConfirmationText = '';
    this.validateEmail = true;
    this.currentEditMode = true;
  }

  closeModal() {
    this.showModal = false;
    this.validateEmail = true;
    this.currentEditMode = false;
  }

  doSaveData(formValue: Userrequest  & { id?: number }){
    const request: Userrequest & { id?: number } = {
      fullName: formValue.fullName,
      email: formValue.email,
      password: formValue.password,
      id: formValue.id
    };

    if(!this.currentEditMode){
      this.userService.saveUser(request).subscribe({
        next: () => {
          this.PNotifyMessage('success','The user has been saved!');
          this.closeModal();
          this.loadData(this.pageNumber, this.pageSize);
        },
        error: () => {
          this.PNotifyMessage('error','There is an error please contact Administrator!');
          this.closeModal();
          this.loadData(this.pageNumber, this.pageSize);
        }
      });
    } else {
      if (request.id == null) {
        this.PNotifyMessage('error', 'User ID is missing. Cannot update user.');
        return;
      }

      this.userService.updateUser(request,request.id).subscribe({
        next: () => {
          this.PNotifyMessage('success','The user has been updated!');
          this.closeModal();
          this.loadData(this.pageNumber, this.pageSize);
        },
        error: () => {
          this.PNotifyMessage('error','There is an error please contact Administrator!');
          this.closeModal();
          this.loadData(this.pageNumber, this.pageSize);
        }
      });
    }

  }

  PNotifyMessage(type:string, text: string):void{
    switch(type){
      case 'success':
        new PNotify({
          title: 'Success',
          text: text,
          type: 'success'
        });
        break;
      case 'warning':
        new PNotify({
          title: 'Success',
          text: text,
          type: 'warning'
        });
        break;
      default:
        new PNotify({
          title: 'Error !',
          text: text,
          type: 'error'
        });
    }
  }

  doValidateEmail(email: string){
    if (email === 'ORIGINAL_EMAIL_NO_VALIDATION') {
      this.validateEmail = true;
      return;
    }

    this.userService.validateEmail(email).subscribe({
      next: () => {
        this.validateEmail = true;
      },
      error: () => {
        this.validateEmail = false;
      }
    })
  }

  onDeleteConfirm(){
    if (this.selectedUserId) {
      this.userService.deleteUser(this.selectedUserId).subscribe({
        next: () => {
          this.PNotifyMessage('warning','The user has been deleted!');
          this.closeModal();
          this.loadData(this.pageNumber, this.pageSize);
        },
        error: () => {
          this.PNotifyMessage('error','There is an error please contact Administrator!');
          this.closeModal();
          this.loadData(this.pageNumber, this.pageSize);
        }
      })
    }

    this.closeModal();
  }


}
