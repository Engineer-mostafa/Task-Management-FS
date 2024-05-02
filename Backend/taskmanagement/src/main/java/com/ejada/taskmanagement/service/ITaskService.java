package com.ejada.taskmanagement.service;

import java.util.List;

import com.ejada.taskmanagement.exception.BusinessException;
import com.ejada.taskmanagement.model.Task;

public interface ITaskService {
	List<Task> getTasksByUserId(Long userId);
	Task updateTask(Task task);
	List<Task> getAllTasks();
	boolean deleteTask(Task task);
	Task editTask(Task task) throws BusinessException;
	Task createNewTask(Task task);
	Task getTasksById(Long id);

}
