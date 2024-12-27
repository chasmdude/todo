package com.secfix.todos.database.repositories;

import com.secfix.todos.database.models.Task;
import com.secfix.todos.database.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByOwner(UserInfo owner);
}