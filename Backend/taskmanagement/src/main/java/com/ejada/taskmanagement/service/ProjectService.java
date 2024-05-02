package com.ejada.taskmanagement.service;

import java.util.List;
import java.util.Optional;

import com.ejada.taskmanagement.exception.BusinessException;
import com.ejada.taskmanagement.model.Project;
import com.ejada.taskmanagement.model.Task;
import com.ejada.taskmanagement.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProjectService implements IProjectService{

	private final ProjectRepository projectRepo;
	
	@Override
	public List<Project> getAllProjects() {
		// TODO Auto-generated method stub
		try {
			// put your validations before calling DAO
            return projectRepo.findAll();
		}catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	@Override
	public Project getProjectById(Long id) {
		// TODO Auto-generated method stub
		try {
			// put your validations before calling DAO
			Optional<Project> project = projectRepo.findById(id);
			return project.get();
		}catch(Exception e) {
			System.err.println(e.getMessage());
			return null;

		}
	}

	@Override
	public Project updateProject(Project project) {
		try {

			if(project.getName().isEmpty()) throw new BusinessException("Name is required");

			project = projectRepo.save(project);
			return project;
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean deleteProject(Project proj) {
		// TODO Auto-generated method stub
		try {
			// put your validations before calling DAO
			projectRepo.delete(proj);
			return true;
		}catch(Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}



	@Override
	public Project createNewProject(Project proj) {
		// TODO Auto-generated method stub

		// team of business give us the validation
		// then testing team test the validations
		try {
			// put your validations before calling DAO
			return projectRepo.save(proj);
		}catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}


}
