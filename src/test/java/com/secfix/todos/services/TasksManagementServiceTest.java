package com.secfix.todos.services;

import com.secfix.todos.apis.dtos.TaskDto;
import com.secfix.todos.apis.dtos.requests.TaskUpdateRequest;
import com.secfix.todos.database.models.Task;
import com.secfix.todos.database.models.UserInfo;
import com.secfix.todos.database.repositories.TaskRepository;
import com.secfix.todos.enums.TaskPriority;
import com.secfix.todos.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TasksManagementServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UsersManagementService usersManagementService;

    @Mock
    private CodeRepositoriesManagementService codeRepositoriesManagementService;

    @InjectMocks
    private TasksManagementService tasksManagementService;

    private Task task;
    private UserInfo oldOwner;
    private UserInfo newOwner;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserInfo owner = new UserInfo();
        owner.setId(1);
        task = new Task();
        task.setId(1);
        task.setName("Test Task");
        task.setDescription("Test Description");
        task.setPriority(TaskPriority.HIGH);
        task.setStatus(TaskStatus.PENDING);
        task.setOwner(owner);

        oldOwner = new UserInfo();
        oldOwner.setId(1);

        newOwner = new UserInfo();
        newOwner.setId(2);

        task1 = new Task();
        task1.setId(1);
        task1.setName("Task 1");
        task1.setOwner(oldOwner);
        task1.setPriority(TaskPriority.MEDIUM);
        task1.setStatus(TaskStatus.PENDING);

        task2 = new Task();
        task2.setId(2);
        task2.setName("Task 2");
        task2.setOwner(oldOwner);
        task2.setPriority(TaskPriority.MEDIUM);
        task2.setStatus(TaskStatus.PENDING);
    }

    @Test
    void updateTaskDescriptionToEmpty() {
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setDescription("");

        TaskDto updatedTask = tasksManagementService.updateTask(1, request);

        assertEquals("", updatedTask.getDescription());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testGetTasksByOwner() {
        when(taskRepository.findByOwner(oldOwner)).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = tasksManagementService.getTasksByOwner(oldOwner);

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findByOwner(oldOwner);
    }

    @Test
    void testBatchUpdateTaskOwner() {
        when(taskRepository.findByOwner(oldOwner)).thenReturn(Arrays.asList(task1, task2));

        tasksManagementService.batchUpdateTaskOwner(oldOwner, newOwner);

        assertEquals(newOwner, task1.getOwner());
        assertEquals(newOwner, task2.getOwner());
        verify(taskRepository, times(1)).findByOwner(oldOwner);
        verify(taskRepository, times(1)).saveAll(Arrays.asList(task1, task2));
    }
}