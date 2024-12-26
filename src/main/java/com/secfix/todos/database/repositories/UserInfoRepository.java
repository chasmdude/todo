package com.secfix.todos.database.repositories;

import com.secfix.todos.database.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    
}
