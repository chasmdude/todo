package com.secfix.todos.apis.dtos;

import com.secfix.todos.database.models.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserDto {
  
    private Integer id;
    private String name;
    private String email;
    private Boolean isActive;

    public UserDto(UserInfo user) {
        this.setId(user.getId());
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setIsActive(user.getIsActive());
    }
}
