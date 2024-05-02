package com.ejada.taskmanagement.service;

import com.ejada.taskmanagement.enums.RoleEnum;
import com.ejada.taskmanagement.model.Role;
import com.ejada.taskmanagement.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class RoleService implements IRoleService {

	private RoleRepository roleRepo;


	@Override
	public Role getRoleByName(RoleEnum roleName) {
		// TODO Auto-generated method stub
		try {
			// put your validations before calling DAO
            return roleRepo.findByName(roleName);
		}catch(Exception e) {
			System.err.println(e.getMessage());
			return null;

		}
	}


}
