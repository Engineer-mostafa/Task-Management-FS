package com.ejada.taskmanagement.controller;


import com.ejada.taskmanagement.enums.RoleEnum;
import com.ejada.taskmanagement.model.Role;
import com.ejada.taskmanagement.model.User;
import com.ejada.taskmanagement.response.ResponseHandler;
import com.ejada.taskmanagement.service.RoleService;
import com.ejada.taskmanagement.service.UserService;
import com.ejada.taskmanagement.util.ExtractJWT;
import com.ejada.taskmanagement.util.SecurityProperties;
import com.ejada.taskmanagement.viewmodel.LoginFormUser;
import com.ejada.taskmanagement.viewmodel.SignupFormUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")

public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final ExtractJWT jwt;
    private final SecurityProperties secProperties;


    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {

        try {
            List<User> users = userService.getAllUsers();
            return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, users);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }


    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignupFormUser signupFormUser , HttpServletRequest request){

        User user;
        ObjectMapper mapper = new ObjectMapper();
        user = mapper.convertValue(signupFormUser , User.class);
        Role role = roleService.getRoleByName(RoleEnum.NORMAL); // get role from
        user.setRole(role);
        user = userService.signup(user);
        Map<String, Object> response = new HashMap<>();
        if(user != null ){

            String access_token = jwt.creatAccessToken(String.valueOf(user.getId()) , user.getName(),user.getRole().getName() ,false , request);
            String refresh_token = jwt.createRefreshToken(String.valueOf(user.getId()) , user.getName() , request);

            response = jwt.getTokensAsJson(access_token , refresh_token);
            response.put("user" , user);
            return ResponseHandler.generateResponse("Successfully Signup!", HttpStatus.OK, response);
        }

        return ResponseHandler.generateResponse("this user values are invalid check them again!", HttpStatus.BAD_REQUEST, response);


    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginFormUser lfUser , HttpServletRequest request){


        User user = userService.login(lfUser.getEmail() , lfUser.getPassword());
        Map<String, Object> response = new HashMap<>();
        if(user != null){
            String access_token = jwt.creatAccessToken(String.valueOf(user.getId()) , user.getName(),user.getRole().getName() ,false , request);
            String refresh_token = jwt.createRefreshToken(String.valueOf(user.getId()) , user.getName() , request);

            response = jwt.getTokensAsJson(access_token , refresh_token);
            response.put("user" , user);
            return ResponseHandler.generateResponse("Successfully Login!", HttpStatus.OK, response);

        }

        return ResponseHandler.generateResponse("this user wasn't found", HttpStatus.NOT_FOUND, response);

    }



    @GetMapping("/secure/refreshAccessToken")
    public ResponseEntity<?> refreshAccessToken(
            @RequestHeader(value = "Authorization") String refreshToken ,
            HttpServletRequest request
    ){

        Map<String, String> response = new HashMap<>();
        if(!jwt.checkIfTokenIsExpired(refreshToken)) {
            Long userId = jwt.refreshAccessToken(refreshToken);
            User user = userService.getUserById(userId);
            String access_token = jwt.creatAccessToken(String.valueOf(user.getId()) , user.getName(),user.getRole().getName() ,false , request);
            String rtoken = refreshToken.substring((secProperties.getBearer() + " ").length());

            response.put("access_token" , access_token);
            response.put("refresh_token" , rtoken);
            return ResponseHandler.generateResponse("Successfully refresh Token!", HttpStatus.OK, response);


        }

        return ResponseHandler.generateResponse("refresh token also expired please login again", HttpStatus.UNAUTHORIZED, response);


    }
}
