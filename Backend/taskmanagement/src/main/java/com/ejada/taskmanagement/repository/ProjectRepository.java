package com.ejada.taskmanagement.repository;

import com.ejada.taskmanagement.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {


}
