package com.example.periodic_payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PeriodicPaymentApplication {
	public static void main(String[] args) {
		SpringApplication.run(PeriodicPaymentApplication.class, args);
	}
}


