package com.ejada.taskmanagement.service;

import java.util.List;
import java.util.Optional;

import com.ejada.taskmanagement.model.Project;
import com.ejada.taskmanagement.model.Task;
import com.ejada.taskmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import com.ejada.taskmanagement.model.User;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService implements IUserService {

	
	private final UserRepository userRepo;


	@Override
	public User login(String email, String password) {
		try {
			// put your validations before calling DAO
			 String salt = BCrypt.gensalt();

		        // Hash the password with the salt
		    
			User user = userRepo.findByEmail(email);
			if(user != null) {
				 if (BCrypt.checkpw(password, user.getPassword())) {
						return user;

			        } else {
			           return null;
			        }
			}
			return null;
		}catch(Exception e) {
			System.err.println(e.getMessage());
			return null;

		}
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		try {
			// put your validations before calling DAO
            return userRepo.findAll();
		}catch(Exception e) {
			System.err.println(e.getMessage());
			return null;

		}
	}

	@Override
	public User getUserById(Long id) {
		// TODO Auto-generated method stub
		try {
			// put your validations before calling DAO
			Optional<User> user = userRepo.findById(id);
			return user.get();
		}catch(Exception e) {
			System.err.println(e.getMessage());
			return null;

		}
	}

	@Override
	public User signup(User user) {
		// TODO Auto-generated method stub
		try {
			// put your validations before calling DAO
			 String salt = BCrypt.gensalt();
		        // Hash the password with the salt
		    String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
		    user.setPassword(hashedPassword);
			user = userRepo.save(user);
			return user;
		}catch(Exception e) {
			System.err.println(e.getMessage());
			return null;

		}
	}



}
