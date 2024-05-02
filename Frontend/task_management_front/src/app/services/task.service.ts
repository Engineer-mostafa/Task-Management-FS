import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { Response } from '../models/response';
import { UserService } from './user.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { ITask } from '../models/itask';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  httpOptions;

  constructor(private httpClient: HttpClient, private userService: UserService , private jwtService: JwtHelperService) {
    this.httpOptions = {
      headers: new HttpHeaders({
        'content-type': 'application/json',
       'Access-Control-Allow-Origin': '*'
      })
    }
  }

  
  getAllTasks(): Observable<Response> {

    return this.httpClient.get<Response>(`${environment.API_URL + environment.Tasks}/tasks`);
  }

  getMyTasks(): Observable<Response> {

    let queryParams = new HttpParams();
    console.log("Id From Token= " + this.jwtService.decodeToken(localStorage.getItem('access_token')!).id)
    queryParams = queryParams.append("user_Id",this.jwtService.decodeToken(localStorage.getItem('access_token')!).id);
    return this.httpClient.get<Response>(`${environment.API_URL + environment.Tasks}/` + "userTasks" , {params: queryParams});
  }

  editTask(task: ITask): Observable<Response> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("user_Id",this.jwtService.decodeToken(localStorage.getItem('access_token')!).id);
    
    return this.httpClient.put<Response>(`${environment.API_URL + environment.Tasks}/`+ task.id , task , {params:queryParams});
  }

  saveTask(task: ITask): Observable<Response> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("user_Id",this.jwtService.decodeToken(localStorage.getItem('access_token')!).id);
    
    return this.httpClient.post<Response>(`${environment.API_URL + environment.Tasks}/create`, task , {params:queryParams});
  }
  
  getTaskById(tId: number): Observable<Response> {
    return this.httpClient.get<Response>(`${environment.API_URL + environment.Tasks}/${tId}`);
  }

  deleteTask(tId: number): Observable<Response> {
    return this.httpClient.delete<Response>(`${environment.API_URL + environment.Tasks}/delete/${tId}`);
  }
}
