package com.secfix.todos.database.models;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserInfoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenEmailIsValid_thenNoConstraintViolations() {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("username@domain.tld");

        Set<ConstraintViolation<UserInfo>> violations = validator.validate(userInfo);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenEmailIsInvalid_thenConstraintViolations() {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("invalid-email");

        Set<ConstraintViolation<UserInfo>> violations = validator.validate(userInfo);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Invalid email format")));
    }
}