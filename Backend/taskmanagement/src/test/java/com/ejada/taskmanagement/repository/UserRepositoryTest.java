package com.ejada.taskmanagement.repository;

import com.ejada.taskmanagement.enums.RoleEnum;
import com.ejada.taskmanagement.model.Project;
import com.ejada.taskmanagement.model.Role;
import com.ejada.taskmanagement.model.User;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void saveUser(){
        // Arrange
//        User testUser = new User();
//        testUser.setEmail("amTestUser@gmail.com");
//        testUser.setName("amTestUser");
//        testUser.setPassword("amTestUserPassword");
//        testUser.setRole(new Role());
//        // Act
//        User savedUser = userRepository.save(testUser);
//
//        // Assert
//        assertNotNull(savedUser);
//        assertThat(savedUser.getId()).isNotEqualTo(null);

        Project testProject = new Project();
        testProject.setName("amTestProject");
        testProject.setDescription("amTestProjectDescription");
        testProject.setManager_id(2L);

        Project savedProject = projectRepository.save(testProject);

        assertNotNull(savedProject);
        assertThat(savedProject.getId()).isNotEqualTo(null);


    }
}
