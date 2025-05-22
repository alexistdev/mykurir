import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {LoginService} from "../login.service";
import {Router} from "@angular/router";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup = this.formBuilder.group({});

  loginError:boolean = false;

  constructor(
              private router: Router,
              private formBuilder: FormBuilder,
              private loginService: LoginService) {
  }

 ngOnInit() {
    this.loginForm = this.formBuilder.group({
      emailUsername: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  doLogin(){

    if(this.loginForm.valid){
      this.loginForm.disable();
      this.loginService.AuthLogin(this.loginForm.controls['emailUsername'].value,this.loginForm.controls['password'].value).subscribe({
        next: (res) => {
          if(res){
            this.loginError = false;
            this.router.navigate(['/admin']);
          }
          this.loginError = true;
        },
        error: (err) => {
          this.loginError = true;
          this.loginForm.disable();
        },
      });
      this.loginError = false;
    } else {
      this.doReset();
      this.loginError = true;
    }


  }

  doReset(){
    this.loginForm.reset();
    this.loginError=false;
    this.loginForm.enable();
  }

}
