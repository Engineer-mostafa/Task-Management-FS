package com.ejada.taskmanagement.controller;

import com.ejada.taskmanagement.enums.RoleEnum;
import com.ejada.taskmanagement.model.Task;
import com.ejada.taskmanagement.model.User;
import com.ejada.taskmanagement.response.ResponseHandler;
import com.ejada.taskmanagement.service.TaskService;
import com.ejada.taskmanagement.service.UserService;
import com.ejada.taskmanagement.util.ExtractJWT;
import com.ejada.taskmanagement.util.SecurityProperties;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/task")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;
    private final ExtractJWT jwt;
    private final UserService userService;




    @GetMapping("/tasks")
    /**
     * return all tasks
     * */
    public ResponseEntity<?> getAllTasks(@RequestHeader(value = "Authorization") String accessToken) {


        Map<String, String> response = new HashMap<>();

        if (!jwt.checkIfTokenIsExpired(accessToken)) {
            User user = userService.getUserById(jwt.getUserIdFromToken(accessToken));

            if(user.getRole().getName().equals(RoleEnum.ADMIN)){
                try {
                    List<Task> tasks = taskService.getAllTasks();
                    return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, tasks);
                } catch (Exception e) {
                    return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
                }
            }

            return ResponseHandler.generateResponse("you are not the admin", HttpStatus.UNAUTHORIZED, response);

            }

        return ResponseHandler.generateResponse("token is expired please login again", HttpStatus.UNAUTHORIZED, response);

    }

    @PostMapping("/create")
    /**
     * return all tasks
     * */
    public ResponseEntity<?> createTask(@RequestHeader(value = "Authorization") String accessToken,
                                            @RequestBody Task newTask) {


        Map<String, String> response = new HashMap<>();

        if (!jwt.checkIfTokenIsExpired(accessToken)) {
            User user = userService.getUserById(jwt.getUserIdFromToken(accessToken));

            if(user.getRole().getName().equals(RoleEnum.ADMIN)){
                try {
                    Task task = taskService.createNewTask(newTask);
                    return ResponseHandler.generateResponse("Successfully create task!", HttpStatus.OK, task);
                } catch (Exception e) {
                    return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
                }
            }

            return ResponseHandler.generateResponse("you are not the admin", HttpStatus.UNAUTHORIZED, response);

        }

        return ResponseHandler.generateResponse("token is expired please login again", HttpStatus.UNAUTHORIZED, response);

    }



    @GetMapping("/userTasks")
    /**
     * return all tasks
     * */
    public ResponseEntity<?> getMyTasks(@RequestHeader(value = "Authorization") String accessToken,
                                        @RequestParam Long user_Id) {


        Map<String, String> response = new HashMap<>();

        if (!jwt.checkIfTokenIsExpired(accessToken)){

            if(user_Id.equals(jwt.getUserIdFromToken(accessToken))){
                try {
                    List<Task> tasks = taskService.getTasksByUserId(user_Id);
                    return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, tasks);
                } catch (Exception e) {
                    return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
                }
            }
            return ResponseHandler.generateResponse("this may be spam request please try again later", HttpStatus.UNAUTHORIZED, response);

        }

        return ResponseHandler.generateResponse("token is expired please login again", HttpStatus.UNAUTHORIZED, response);

    }

    @PutMapping("/{taskId}")
    /**
     * return all tasks
     * */
    public ResponseEntity<?> editTask(@RequestHeader(value = "Authorization") String accessToken,
                                        @RequestParam Long user_Id,
                                        @PathVariable Long taskId,
                                        @RequestBody Task updateTask) {


        Map<String, String> response = new HashMap<>();

        if (!jwt.checkIfTokenIsExpired(accessToken)){

            User user = userService.getUserById(jwt.getUserIdFromToken(accessToken));
            if(user_Id.equals(jwt.getUserIdFromToken(accessToken)) || user.getRole().getName().equals(RoleEnum.ADMIN)){
                Task task = taskService.getTasksById(taskId);
                if(task == null) return ResponseHandler.generateResponse("task not found", HttpStatus.NOT_FOUND, null);

                if(task.getUserId().equals(user_Id) || user.getRole().getName().equals(RoleEnum.ADMIN)){
                    try {
                        task = taskService.updateTask(updateTask);
                        return ResponseHandler.generateResponse("Successfully updated!", HttpStatus.OK, task);

                    } catch (Exception e) {
                        return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
                    }
                }
                return ResponseHandler.generateResponse("this task is not assigned to this user", HttpStatus.BAD_REQUEST, response);

            }
            return ResponseHandler.generateResponse("this may be spam request please try again later", HttpStatus.UNAUTHORIZED, response);

        }

        return ResponseHandler.generateResponse("token is expired please login again", HttpStatus.UNAUTHORIZED, response);

    }



    @DeleteMapping("/delete/{taskId}")
    /**
     * return all tasks
     * */
    public ResponseEntity<?> deleteTask( @RequestHeader(value = "Authorization") String accessToken,
                                         @PathVariable Long taskId) {


        Map<String, String> response = new HashMap<>();

        if (!jwt.checkIfTokenIsExpired(accessToken)) {
            User user = userService.getUserById(jwt.getUserIdFromToken(accessToken));

            if(user.getRole().getName().equals(RoleEnum.ADMIN)){
                try {
                    Task task = taskService.getTasksById(taskId);
                    if(task == null) return ResponseHandler.generateResponse("task not found", HttpStatus.NOT_FOUND, null);

                    if (taskService.deleteTask(task))
                        return ResponseHandler.generateResponse("Successfully deleted!", HttpStatus.OK, null);
                    else
                        return ResponseHandler.generateResponse("Failed to delete! please try again later", HttpStatus.INTERNAL_SERVER_ERROR, null);
                } catch (Exception e) {
                    return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
                }
            }

            return ResponseHandler.generateResponse("you are not the admin", HttpStatus.UNAUTHORIZED, response);

        }

        return ResponseHandler.generateResponse("token is expired please login again", HttpStatus.UNAUTHORIZED, response);

    }

}
