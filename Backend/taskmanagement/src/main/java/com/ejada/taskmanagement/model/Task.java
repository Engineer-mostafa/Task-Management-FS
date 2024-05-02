package com.ejada.taskmanagement.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ejada.taskmanagement.enums.TaskCategoryEnum;
import com.ejada.taskmanagement.enums.TaskStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@Entity
public class Task {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	@Column(name="name", nullable = false)
	private String name;

	@Column(name="description")
	private String description;
	
	@Column(name="category")
	@Enumerated(EnumType.STRING)
	private TaskCategoryEnum taskCategory;
	
	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private TaskStatusEnum taskStatus;
	
	@Column(name="deadline")
	@Temporal(TemporalType.DATE)
	private Date deadline;

	private Long userId;

	private Long project_id;

}
