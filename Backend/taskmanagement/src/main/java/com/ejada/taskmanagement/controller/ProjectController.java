package com.ejada.taskmanagement.controller;

import com.ejada.taskmanagement.enums.RoleEnum;
import com.ejada.taskmanagement.model.Project;
import com.ejada.taskmanagement.model.Task;
import com.ejada.taskmanagement.model.User;
import com.ejada.taskmanagement.response.ResponseHandler;
import com.ejada.taskmanagement.service.ProjectService;
import com.ejada.taskmanagement.service.UserService;
import com.ejada.taskmanagement.util.ExtractJWT;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/project")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {

    private final ProjectService projectService;
    private final ExtractJWT jwt;
    private final UserService userService;

    @GetMapping("/projects")
    /**
     * return all projects
     * */
    public ResponseEntity<?> getAllProjects(@RequestHeader(value = "Authorization") String accessToken) {
        Map<String, String> response = new HashMap<>();

        if (!jwt.checkIfTokenIsExpired(accessToken)) {
            User user = userService.getUserById(jwt.getUserIdFromToken(accessToken));

            if(user.getRole().getName().equals(RoleEnum.ADMIN)){
                try {
                    List<Project> projects = projectService.getAllProjects();
                    return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, projects);
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
    public ResponseEntity<?> createProject(@RequestHeader(value = "Authorization") String accessToken,
                                        @RequestBody Project newProject) {


        Map<String, String> response = new HashMap<>();

        if (!jwt.checkIfTokenIsExpired(accessToken)) {
            User user = userService.getUserById(jwt.getUserIdFromToken(accessToken));

            if(user.getRole().getName().equals(RoleEnum.ADMIN)){
                try {
                    Project project = projectService.createNewProject(newProject);
                    return ResponseHandler.generateResponse("Successfully create task!", HttpStatus.OK, project);
                } catch (Exception e) {
                    return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
                }
            }

            return ResponseHandler.generateResponse("you are not the admin", HttpStatus.UNAUTHORIZED, response);

        }

        return ResponseHandler.generateResponse("token is expired please login again", HttpStatus.UNAUTHORIZED, response);

    }


    @PutMapping("/{projectId}")
    /**
     * return all tasks
     * */
    public ResponseEntity<?> editTask(@RequestHeader(value = "Authorization") String accessToken,
                                      @PathVariable Long projectId,
                                      @RequestBody Project updateProject) {


        Map<String, String> response = new HashMap<>();

        if (!jwt.checkIfTokenIsExpired(accessToken)){

            User user = userService.getUserById(jwt.getUserIdFromToken(accessToken));
            if(user.getRole().getName().equals(RoleEnum.ADMIN)){
                Project project = projectService.getProjectById(projectId);
                if(project == null) return ResponseHandler.generateResponse("project not found", HttpStatus.NOT_FOUND, null);
                    try {
                        project = projectService.updateProject(updateProject);
                        return ResponseHandler.generateResponse("Successfully updated!", HttpStatus.OK, project);

                    } catch (Exception e) {
                        return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
                    }


            }
            return ResponseHandler.generateResponse("this may be spam request please try again later", HttpStatus.UNAUTHORIZED, response);

        }

        return ResponseHandler.generateResponse("token is expired please login again", HttpStatus.UNAUTHORIZED, response);

    }



    @DeleteMapping("/delete/{projectId}")
    /**
     * return all tasks
     * */
    public ResponseEntity<?> deleteTask( @RequestHeader(value = "Authorization") String accessToken,
                                         @PathVariable Long projectId) {


        Map<String, String> response = new HashMap<>();

        if (!jwt.checkIfTokenIsExpired(accessToken)) {
            User user = userService.getUserById(jwt.getUserIdFromToken(accessToken));

            if(user.getRole().getName().equals(RoleEnum.ADMIN)){
                try {
                    Project project = projectService.getProjectById(projectId);
                    if(project == null) return ResponseHandler.generateResponse("project not found", HttpStatus.NOT_FOUND, null);

                    if (projectService.deleteProject(project))
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
