import { Component, OnDestroy, OnInit } from '@angular/core';
import { ITask } from 'src/app/models/itask';
import { TaskService } from 'src/app/services/task.service';
import { Response } from 'src/app/models/response';
import { JwtHelperService } from '@auth0/angular-jwt';
import { MessageService } from 'primeng/api';
import { UserService } from 'src/app/services/user.service';
import { IUser } from 'src/app/models/iUser';
import { ProjectService } from 'src/app/services/project.service';
import { IProject } from 'src/app/models/iproject';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss']
})
export class TaskListComponent implements OnDestroy, OnInit {

  tasks: ITask[] = [];
  response!: Response;
  task: ITask = {} as ITask;
  taskDialog: boolean = false;
  deleteTaskDialog: boolean = false;
  taskCategories: Object[] = [];
  taskStatus: Object[] = [];
  submitted: boolean = false;
  isAdmin: boolean = false;
  users: Object[] = [];
  tempUsersArray:IUser[]  = [];
  tempProjectsArray:IProject[] = [];
  projects: Object[] = [];

  constructor( private taskService: TaskService ,
     private jwtService: JwtHelperService, 
     private messageService: MessageService,
     private userService: UserService,
     private projectSerice: ProjectService){

  if(this.jwtService.decodeToken(localStorage.getItem('access_token')!).role == 'ADMIN'){
    this.taskService.getAllTasks().subscribe(
      tasks => {
        this.response = tasks;
        this.tasks = this.response.data as unknown as ITask[];
      }
    )
  }
else{
  this.taskService.getMyTasks().subscribe(
    tasks => {
      this.response = tasks;
      this.tasks = this.response.data as unknown as ITask[];
    }
  )
}
  }
  ngOnInit(): void {
    this.isAdmin = this.jwtService.decodeToken(localStorage.getItem('access_token')!).role == 'ADMIN';

  if(this.isAdmin){
    this.userService.getAllUsers().subscribe(
      res => {
        this.response = res;
        this.tempUsersArray = this.response.data as unknown as IUser[];
        this.tempUsersArray.forEach(user=>{
          this.users.push({label: user.name, value: user.id})
        })
      }
    );

    this.projectSerice.getAllProducts().subscribe(
      res => {
        this.response = res;
        this.tempProjectsArray = this.response.data as unknown as IProject[];
        this.tempProjectsArray.forEach(proj=>{
        this.projects.push({label: proj.name, value: proj.id})
        })
      }
    )
  }
    // ToDo,
	// DONE
  this.taskStatus = [
    { label: 'ToDo', value: 'ToDo' },
    { label: 'DONE', value: 'DONE' },
  
];  

//  ENTRY,
// 	INTERMEDIATE,
// 	ADVANCED,
// 	EXPERT
this.taskCategories = [
  { label: 'ENTRY', value: 'ENTRY' },
  { label: 'INTERMEDIATE', value: 'INTERMEDIATE' },
  { label: 'ADVANCED', value: 'ADVANCED' },
  { label: 'EXPERT', value: 'EXPERT' },

];  
}
  ngOnDestroy(): void {
    this.tasks = [];
  }

  hideDialog() {
    this.taskDialog = false;
    this.submitted = false;
}
openNew() {
  this.task = {} as ITask;
  this.submitted = false;
  this.taskDialog = true;
}
saveTask(taskId:number) {
    this.submitted = true;
    this.taskDialog = false;
    if(taskId > 0){
      console.log("update Task taskId= " + taskId);

      this.taskService.editTask( this.task).subscribe(
        res => {
          this.response = res;
          this.task = this.response.data as unknown as ITask;
          let indexToUpdate = this.tasks.findIndex(proj => proj.id === this.task.id);
          this.tasks[indexToUpdate] = this.task;
          this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Task updated', life: 3000 });
          
        }
      )
    }
    else{
      console.log("create New Task taskId= " + taskId);

      this.taskService.saveTask( this.task).subscribe(
        res => {
          this.response = res;
          this.task = this.response.data as unknown as ITask;
          this.tasks.push(this.task);
          this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Task Added', life: 3000 });
          
        }
      )
    }
    
}

  editTask(selectedTask: ITask) {
    this.task = { ...selectedTask };
    this.task.deadline = new Date(selectedTask.deadline)
    this.taskDialog = true;
}

deleteTask(selectedTask: ITask) {
    this.deleteTaskDialog = true;
    this.task = { ...selectedTask };
}

confirmDelete() {
  this.deleteTaskDialog = false;
  this.taskService.deleteTask(this.task.id).subscribe(
    res => {
      this.response = res;
      let indexTodelete= this.tasks.findIndex(task => task.id === this.task.id);
      // remove the task by its index from task attay
      this.tasks.splice(indexTodelete,1);
      this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Task Deleted', life: 3000 });
      
    }
  );
}


}
