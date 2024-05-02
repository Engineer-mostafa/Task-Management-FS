import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { ILoginResponse } from 'src/app/models/iLoginResponse';
import { Response } from 'src/app/models/response';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {


  isUserLogged!: boolean;
  response!: Response;

  loginResponse!: ILoginResponse;
  constructor(private userService: UserService , private router: Router){}

  ngOnInit() {
    this.isUserLogged = this.userService.isUserLogged;
    if(this.isUserLogged) this.router.navigateByUrl('/home');

  }

  login(email:string , password: any){
    this.userService.login(email , password);
   
  }
}
