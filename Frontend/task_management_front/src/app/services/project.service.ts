import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { Response } from '../models/response';
import { IProject } from '../models/iproject';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  constructor(private httpClient: HttpClient , private jwtService: JwtHelperService) {
  }

  
  getAllProducts(): Observable<Response> {
    return this.httpClient.get<Response>(`${environment.API_URL + environment.Projects}/projects` );
  }

  
  getProductById(pId: number): Observable<Response> {
    return this.httpClient.get<Response>(`${environment.API_URL + environment.Projects}/projects/${pId}`);
  }

  editProject(project: IProject):Observable<Response> {
    return this.httpClient.put<Response>(`${environment.API_URL + environment.Projects}/`+ project.id , project);
  }

  saveProject(project: IProject): Observable<Response> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("user_Id",this.jwtService.decodeToken(localStorage.getItem('access_token')!).id);
    
    return this.httpClient.post<Response>(`${environment.API_URL + environment.Projects}/create`, project , {params:queryParams});
  }
  
  

  deleteProject(pId: number): Observable<Response> {
    return this.httpClient.delete<Response>(`${environment.API_URL + environment.Projects}/delete/${pId}`);
  }
}
