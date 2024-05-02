package com.ejada.taskmanagement.service;

import java.util.List;

import com.ejada.taskmanagement.model.Project;


public interface IProjectService {

	List<Project> getAllProjects();
	Project getProjectById(Long id);

	Project updateProject(Project project);

	boolean deleteProject(Project proj);

	public Project createNewProject(Project proj);



}
