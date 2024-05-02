package com.ejada.taskmanagement.service;

import java.util.List;

import com.ejada.taskmanagement.model.User;

public interface IUserService {
	
	User login(String email , String password);
	List<User> getAllUsers();
	User getUserById(Long id);
	User signup(User user);

}
