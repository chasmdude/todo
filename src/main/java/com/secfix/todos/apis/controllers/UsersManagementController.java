package com.secfix.todos.apis.controllers;

import com.secfix.todos.apis.dtos.UserDto;
import com.secfix.todos.apis.dtos.requests.UserCreateRequest;
import com.secfix.todos.apis.dtos.requests.UserUpdateRequest;
import com.secfix.todos.database.models.UserInfo;
import com.secfix.todos.exceptions.ApiServiceCallException;
import com.secfix.todos.services.TasksManagementService;
import com.secfix.todos.services.UsersManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
@RequestMapping("/user")
public class UsersManagementController {
    private Logger logger = LoggerFactory.getLogger(UsersManagementController.class);

    @Autowired
    private UsersManagementService usersManagementService;

    private TasksManagementService tasksManagementService;

    @Operation(summary = "Get Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns Users list", content = {
                   @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "An error has occurred ", content = @Content)
    })
    @GetMapping("")
    public ResponseEntity<?> getUsers(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(this.usersManagementService.getUsers());
        } catch (ApiServiceCallException apiex) {
            return new ResponseEntity<>(apiex.getMessage(), apiex.getHttpStatus());
        } catch (Exception ex) {
            logger.error("Error occured while getting users", ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get User by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the User", content = {
                    @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "An error has occurred ", content = @Content)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(HttpServletRequest request,
            @Parameter(description = "User id") @PathVariable Integer userId) {

        try {
            return ResponseEntity.ok(this.usersManagementService.getUserByIdInDto(userId));
        } catch (ApiServiceCallException apiex) {
            return new ResponseEntity<>(apiex.getMessage(), apiex.getHttpStatus());
        } catch (Exception ex) {
            logger.error("Error occured while getting user <{}>", userId, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the created User", content = {
                    @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "An error has occurred ", content = @Content)
    })
    @PostMapping("")
    public ResponseEntity<?> createUser(HttpServletRequest request,
            @Parameter(description = "Create user request data") @Valid @RequestBody UserCreateRequest createRequest) {

        try {
            return ResponseEntity
                    .ok(this.usersManagementService.createUser(createRequest));
        } catch (ApiServiceCallException apiex) {
            return new ResponseEntity<>(apiex.getMessage(), apiex.getHttpStatus());
        } catch (Exception ex) {
            logger.error("Error occured while creating user",
                    ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update User with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the updated User", content = {
                    @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "An error has occurred ", content = @Content)
    })
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(HttpServletRequest request,
            @Parameter(description = "User update request data") @Valid @RequestBody UserUpdateRequest updateRequest,
            @Parameter(description = "User id") @PathVariable Integer userId) {

        try {
            return ResponseEntity.ok(this.usersManagementService.updateUser(userId, updateRequest));
        } catch (ApiServiceCallException apiex) {
            return new ResponseEntity<>(apiex.getMessage(), apiex.getHttpStatus());
        } catch (Exception ex) {
            logger.error("Error occured while updating user <{}>", userId, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete User with id and reassign their tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted and tasks reassigned", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid new assignee or user id", content = @Content),
            @ApiResponse(responseCode = "500", description = "An error has occurred", content = @Content)
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(HttpServletRequest request,
                                        @Parameter(description = "User id of the user to delete") @PathVariable Integer userId,
                                        @Parameter(description = "User id of the new assignee for tasks") @RequestParam Integer newAssigneeId) {
        try {
            // Fetch the user to be deleted and the new assignee
            UserInfo userToDelete = usersManagementService.getUserById(userId);
            UserInfo newAssignee = usersManagementService.getUserById(newAssigneeId);

            // Check if both users are valid
            if (userToDelete == null || newAssignee == null) {
                return new ResponseEntity<>("Invalid user or assignee", HttpStatus.BAD_REQUEST);
            }

            // Reassign tasks of the user to be deleted to the new assignee
            tasksManagementService.batchUpdateTaskOwner(userToDelete, newAssignee);

            // Delete the user
            usersManagementService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ApiServiceCallException apiex) {
            return new ResponseEntity<>(apiex.getMessage(), apiex.getHttpStatus());
        } catch (Exception ex) {
            logger.error("Error occured while deleting user <{}>",
                    userId,
                    ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
