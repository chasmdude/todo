package com.secfix.todos.apis.controllers;

import com.secfix.todos.database.models.UserInfo;
import com.secfix.todos.services.TasksManagementService;
import com.secfix.todos.services.UsersManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UsersManagementControllerTest {

    @InjectMocks
    private UsersManagementController userController;

    @Mock
    private UsersManagementService usersManagementService;

    @Mock
    private TasksManagementService tasksManagementService;

    @Mock
    private HttpServletRequest request;

    private UserInfo userToDelete;

    private UserInfo newAssignee;

    @BeforeEach
    void setUp() {
        userToDelete = new UserInfo();
        userToDelete.setId(1);
        userToDelete.setName("UserToDelete");

        newAssignee = new UserInfo();
        newAssignee.setId(2);
        newAssignee.setName("NewAssignee");
    }

    @Test
    void testDeleteUserAndReassignTasks_Success() {
        Mockito.when(usersManagementService.getUserById(1)).thenReturn(userToDelete);
        Mockito.when(usersManagementService.getUserById(2)).thenReturn(newAssignee);

        ResponseEntity<?> response = userController.deleteUser(request, 1, 2);

        Mockito.verify(usersManagementService).getUserById(1);
        Mockito.verify(usersManagementService).getUserById(2);
        Mockito.verify(tasksManagementService).batchUpdateTaskOwner(userToDelete, newAssignee);
        Mockito.verify(usersManagementService).deleteUser(1);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteUserAndReassignTasks_InvalidUser() {
        Mockito.when(usersManagementService.getUserById(1)).thenReturn(null);

        ResponseEntity<?> response = userController.deleteUser(request, 1, 2);

        Mockito.verify(usersManagementService).getUserById(1);
        Mockito.verifyNoInteractions(tasksManagementService);
        Mockito.verify(usersManagementService, Mockito.never()).deleteUser(1);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Invalid user or assignee", response.getBody());
    }

    @Test
    void testDeleteUserAndReassignTasks_InvalidAssignee() {
        Mockito.when(usersManagementService.getUserById(1)).thenReturn(userToDelete);
        Mockito.when(usersManagementService.getUserById(2)).thenReturn(null);

        ResponseEntity<?> response = userController.deleteUser(request, 1, 2);

        Mockito.verify(usersManagementService).getUserById(1);
        Mockito.verify(usersManagementService).getUserById(2);
        Mockito.verifyNoInteractions(tasksManagementService);
        Mockito.verify(usersManagementService, Mockito.never()).deleteUser(1);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Invalid user or assignee", response.getBody());
    }
}