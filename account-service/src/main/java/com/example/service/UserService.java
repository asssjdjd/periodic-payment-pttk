package com.example.service;

import com.example.dto.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public UserResponse getUserById(String id);
}
