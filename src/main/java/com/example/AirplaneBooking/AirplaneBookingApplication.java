package com.example.AirplaneBooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = "com.example.AirplaneBooking")
public class AirplaneBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirplaneBookingApplication.class, args);
    }
}
