package com.secfix.todos.database.repositories;

import com.secfix.todos.database.models.Task;
import com.secfix.todos.database.models.UserInfo;
import com.secfix.todos.enums.TaskPriority;
import com.secfix.todos.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    private UserInfo owner;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        owner = new UserInfo();
        owner.setName("Test User");
        owner.setEmail("testuser@example.com");
        userInfoRepository.save(owner);

        task1 = new Task();
        task1.setName("Task 1");
        task1.setOwner(owner);
        task1.setPriority(TaskPriority.MEDIUM); // Set default priority
        task1.setStatus(TaskStatus.PENDING); // Set default status
        taskRepository.save(task1);

        task2 = new Task();
        task2.setName("Task 2");
        task2.setOwner(owner);
        task2.setPriority(TaskPriority.MEDIUM); // Set default priority
        task2.setStatus(TaskStatus.PENDING); // Set default status
        taskRepository.save(task2);
    }

    @Test
    void testFindByOwner_Success() {
        List<Task> tasks = taskRepository.findByOwner(owner);
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void testFindByOwner_NoTasksFound() {
        UserInfo newOwner = new UserInfo();
        newOwner.setName("New User");
        newOwner.setEmail("newuser@example.com");
        userInfoRepository.save(newOwner);

        List<Task> tasks = taskRepository.findByOwner(newOwner);
        assertTrue(tasks.isEmpty());
    }
}