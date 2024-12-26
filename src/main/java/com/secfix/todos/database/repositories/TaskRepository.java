package com.secfix.todos.database.repositories;

import com.secfix.todos.database.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Integer> {
    
}
