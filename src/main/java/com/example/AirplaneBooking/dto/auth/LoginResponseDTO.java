package com.example.AirplaneBooking.dto.auth;

import com.example.AirplaneBooking.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private UserDTO user;
    private String message;
}