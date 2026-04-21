package com.example.service.Impl;

import com.example.dto.response.CustomerResponse;
import com.example.dto.response.UserResponse;
import com.example.exception.ExceptionCode;
import com.example.exception.ResourceException;
import com.example.model.Customer;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getUserById(String id) {
        log.info("[UserServiceImpl] : Thực hiện tìm người dùng theo id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[UserServiceImpl] : Không tìm thấy khách hàng với id: {}", id);
                    return new ResourceException(
                            ExceptionCode.USER_NOT_FOUND.getCode(),
                            ExceptionCode.USER_NOT_FOUND.getMessage()
                    );
                });

        return UserResponse.fromEntity(user);
    }
}
