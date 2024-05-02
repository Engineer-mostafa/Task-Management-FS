import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/project.service';
import { IProject } from '../../models/iproject';
import { Response } from '../../models/response';
import { ITask } from '../../models/itask';
import { TaskService } from '../../services/task.service';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit{

  isAdmin:boolean = false;




  constructor( private router: Router,private userService: UserService , private jwtService:JwtHelperService){

  }
  ngOnInit(): void { // before the page loads
    if(!this.userService.isUserLogged) this.router.navigateByUrl('/Login');

    this.isAdmin = this.jwtService.decodeToken(localStorage.getItem('access_token')!).role == 'ADMIN';
  }
 



}
