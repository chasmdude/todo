package com.secfix.todos.database.models;

import com.secfix.todos.apis.dtos.requests.UserCreateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_info")
@Data
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_active", columnDefinition = "boolean DEFAULT true")
    private Boolean isActive;

    public UserInfo() {
    }

    public UserInfo(UserCreateRequest createRequest) {
        this.setName(createRequest.getName());
        this.setEmail(createRequest.getEmail());
        this.setIsActive(createRequest.getIsActive() == null? true: createRequest.getIsActive());
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Email: %s", this.getName(), this.getEmail());
    }
}
