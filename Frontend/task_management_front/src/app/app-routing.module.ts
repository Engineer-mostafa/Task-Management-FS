import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { MainlayoutComponent } from './components/mainlayout/mainlayout.component';
import { ProjectListComponent } from './components/project/project-list/project-list.component';
import { ProjectDetailsComponent } from './components/project/project-details/project-details.component';
import { TaskListComponent } from './components/task/task-list/task-list.component';
import { TaskDetailsComponent } from './components/task/task-details/task-details.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { authGuard } from './Guards/auth.guard';
import { NotfoundComponent } from './components/notfound/notfound.component';

const routes: Routes = [ // first match win ( order is important )
 
  {
    path: '' ,
    component: MainlayoutComponent,
    canActivateChild: [authGuard],
    children:[
      {
        path: '' , // default path
        redirectTo: 'home',
        pathMatch: 'full' // all path after domain
      },
      {
        path: 'home' ,
        component: HomeComponent
      },
      {
        path: 'projects',
        component: ProjectListComponent
      },
      {
        path: 'projects/:pid',
        component: ProjectDetailsComponent
      },
      {
        path: 'tasks',
        component: TaskListComponent,
      },
      {
        path: 'tasks/:tid',
        component: TaskDetailsComponent
      },
    ]
  },
 
  {
    path: 'Login' , // wild card path
    component: LoginComponent,
  },
  {
    path: 'signup' , // wild card path
    component: SignupComponent,
  },
  {
    path: '**' , // wild card path
    component: NotfoundComponent,
  },
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
