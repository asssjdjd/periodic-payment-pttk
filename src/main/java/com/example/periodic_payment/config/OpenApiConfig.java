package com.example.periodic_payment.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@OpenAPIDefinition(
//        info = @Info(
//                title = "User Management API",
//                version = "1.0",
//                description = "Hệ thống quản lý người dùng nội bộ",
//                contact = @Contact(name = "Backend Team", email = "admin@company.com")
//        )
//)
public class OpenApiConfig extends RuntimeException {
    public OpenApiConfig(String message) {
        super(message);
    }
}
