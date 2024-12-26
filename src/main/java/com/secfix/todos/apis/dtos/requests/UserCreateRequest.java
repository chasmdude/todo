package com.secfix.todos.apis.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreateRequest {

    @NotNull (message = "Name can't be null")
    @NotEmpty (message = "Name can't be empty")
    private String name;

    @NotNull (message = "Email can't be null")
    @NotEmpty (message = "Email can't be empty")
    private String email;
    private Boolean isActive;

    @Override
    public String toString() {
        return String.format("Name: %s, Email: %s", this.getName(), this.getEmail());
    }
}
