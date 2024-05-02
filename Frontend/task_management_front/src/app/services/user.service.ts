import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ILoginResponse } from '../models/iLoginResponse';
import { Response } from '../models/response';
import { IUser } from '../models/iUser';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  httpOptions;
  body: any;
  private isLoggedSubject: BehaviorSubject<boolean>; // behavior to set initial value
  response!: Response;
  currUser!:IUser;
  loginResponse!: ILoginResponse;
  constructor(private httpClient: HttpClient , private router: Router) {

    this.httpOptions = {
      headers: new HttpHeaders({
        'content-type': 'application/json',
       'Access-Control-Allow-Origin': '*'
      })
    }

    this.isLoggedSubject = new BehaviorSubject<boolean>(false);

  }
 



  login(email: string , password: string){

    this.body = {
      "email": email,
      "password": password
  }
    this.httpClient.post<Response>(`${environment.API_URL}login` , this.body, this.httpOptions).subscribe(
      res => {
        console.log(res);
        this.response = res;
        this.loginResponse = this.response.data as unknown as ILoginResponse;
        this.currUser = this.loginResponse.user;
        localStorage.setItem('access_token', this.loginResponse.access_token as string);
        localStorage.setItem('refresh_token', this.loginResponse.refresh_token as string);
        this.isLoggedSubject.next(true);
        this.router.navigateByUrl('/home');

      }
    );

    // call the api
  }

  getAllUsers(): Observable<Response>{
    return this.httpClient.get<Response>(`${environment.API_URL}users`)
  }
  logout(){
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    this.isLoggedSubject.next(false);
  }

  get isUserLogged(){
     return localStorage.getItem('access_token') !== 'undefined' &&  localStorage.getItem('access_token')?  true: false;
  }

  isUserLoggedSubject(){
    return this.isLoggedSubject.asObservable();
  }

  get currUserFn(): IUser{
    return this.currUser;
  }
}
