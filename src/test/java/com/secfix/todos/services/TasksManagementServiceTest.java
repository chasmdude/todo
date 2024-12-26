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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
}