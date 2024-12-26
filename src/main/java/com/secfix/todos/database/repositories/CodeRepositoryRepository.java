package com.secfix.todos.database.repositories;

import com.secfix.todos.database.models.CodeRepository;
import com.secfix.todos.enums.CodeRepositoryStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;




public interface CodeRepositoryRepository extends JpaRepository<CodeRepository, Integer> {
    List<CodeRepository> findByStatus(CodeRepositoryStatus status);
}
