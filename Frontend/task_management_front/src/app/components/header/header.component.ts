import { Component, OnInit } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  isAdmin:boolean = false;

  constructor(private userService: UserService , private jwtService:JwtHelperService){

  }
  ngOnInit(): void {
    this.isAdmin = this.jwtService.decodeToken(localStorage.getItem('access_token')!).role == 'ADMIN';
  }
  logout(){
    this.userService.logout();  
  }
  
}
