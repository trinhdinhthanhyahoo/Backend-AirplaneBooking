package com.example.AirplaneBooking.service;

import com.example.AirplaneBooking.dto.auth.LoginRequestDTO;
import com.example.AirplaneBooking.dto.auth.LoginResponseDTO;
import com.example.AirplaneBooking.dto.user.CreateUserDTO;
import com.example.AirplaneBooking.dto.user.UserDTO;
import com.example.AirplaneBooking.model.entity.User;
import com.example.AirplaneBooking.repository.UserRepository;

import com.example.AirplaneBooking.exception.ResourceNotFoundException;
import com.example.AirplaneBooking.exception.UnauthorizedException;
import com.example.AirplaneBooking.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDTO create(CreateUserDTO createDTO) {
        if (userRepository.existsByUsername(createDTO.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmail(createDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = modelMapper.map(createDTO, User.class);
        user.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public UserDTO update(UUID id, CreateUserDTO updateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if new username is already taken by another user
        if (!user.getUsername().equals(updateDTO.getUsername()) &&
                userRepository.existsByUsername(updateDTO.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        // Check if new email is already taken by another user
        if (!user.getEmail().equals(updateDTO.getEmail()) &&
                userRepository.existsByEmail(updateDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        modelMapper.map(updateDTO, user);
        if (updateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        user = userRepository.save(user);
        return modelMapper.map(user, UserDTO.class);
    }

    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return new LoginResponseDTO(
                modelMapper.map(user, UserDTO.class),
                "Login successful");
    }
}