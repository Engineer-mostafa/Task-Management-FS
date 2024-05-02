import { Component, OnInit } from '@angular/core';
import { IProject } from 'src/app/models/iproject';
import { ProjectService } from 'src/app/services/project.service';
import { Response } from 'src/app/models/response';
import { MessageService } from 'primeng/api';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserService } from 'src/app/services/user.service';
import { IUser } from 'src/app/models/iUser';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.scss']
})
export class ProjectListComponent implements OnInit{

  projects: IProject[] = [];
  project: IProject = {} as IProject;
  projectDialog: boolean = false;
  deleteProjectDialog: boolean = false;
  response!: Response;
  projectCategories: Object[] = [];
  submitted: boolean = false;
  users: Object[] = [];
  tempUsersArray:IUser[]  = [];
  tempProjectsArray:IProject[] = [];

  constructor(private projectService: ProjectService,
      private messageService: MessageService,
      private userService: UserService){

    this.projectService.getAllProducts().subscribe(
      projs =>{
      this.response = projs;
      this.projects = this.response.data as unknown as IProject[];
    });

  }
  ngOnInit(): void {

    this.userService.getAllUsers().subscribe(
      res => {
        this.response = res;
        this.tempUsersArray = this.response.data as unknown as IUser[];
        this.tempUsersArray.forEach(user=>{
          this.users.push({label: user.name, value: user.id})
        })
      }
    );
  // DEVELOPMENT,
	// DESIGN,
	// MARKETING,
	// SALES,
	// SUPPORT
    this.projectCategories = [
      { label: 'DEVELOPMENT', value: 'DEVELOPMENT' },
      { label: 'DESIGN', value: 'DESIGN' },
      { label: 'SUPPORT', value: 'SUPPORT' },
      { label: 'SALES', value: 'SALES' },
      { label: 'MARKETING', value: 'MARKETING' }
  ];  
  }

  hideDialog() {
    this.projectDialog = false;
    this.submitted = false;
}



openNew() {
  this.project = {} as IProject;
  this.submitted = false;
  this.projectDialog = true;
}
  editProject(selectedProject: IProject) {
    this.project = { ...selectedProject };
    this.projectDialog = true;
    
}

deleteProject(selectedProject: IProject) {
    this.deleteProjectDialog = true;
    this.project = { ...selectedProject };
}

   

confirmDelete() {
  this.deleteProjectDialog = false;
  this.projectService.deleteProject(this.project.id).subscribe(
    res => {
      this.response = res;
      let indexTodelete= this.projects.findIndex(proj => proj.id === this.project.id);
      // remove the task by its index from task attay
      this.projects.splice(indexTodelete,1);
      this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Project Deleted', life: 3000 });
      
    }
  );
}

saveProject(projectId:number) {
  this.submitted = true;
  this.projectDialog = false;
  if(projectId > 0){
    console.log("update Project projectId= " + projectId);

    this.projectService.editProject( this.project).subscribe(
      res => {
        this.response = res;
        this.project = this.response.data as unknown as IProject;
        let indexToUpdate = this.projects.findIndex(proj => proj.id === this.project.id);
        this.projects[indexToUpdate] = this.project;
        this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Proejct updated', life: 3000 });
        
      }
    )
  }
  else{
    console.log("create New Project projectId= " + projectId);

    this.projectService.saveProject(this.project).subscribe(
      res => {
        this.response = res;
        this.project = this.response.data as unknown as IProject;
        this.projects.push(this.project);
        this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Proejct Created', life: 3000 });
        
      }
    )
  }
  
}

}
