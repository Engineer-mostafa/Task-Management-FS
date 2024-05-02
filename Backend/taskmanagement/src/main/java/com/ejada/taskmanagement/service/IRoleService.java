package com.ejada.taskmanagement.service;

import com.ejada.taskmanagement.enums.RoleEnum;
import com.ejada.taskmanagement.model.Role;

public interface IRoleService {
	
	Role getRoleByName(RoleEnum roleName);

}
