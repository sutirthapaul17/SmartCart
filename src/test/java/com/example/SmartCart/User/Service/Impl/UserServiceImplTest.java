package com.example.SmartCart.User.Service.Impl;

import com.example.SmartCart.Mapper.User.UserMapper;
import com.example.SmartCart.User.Dto.User.CreateUserDto;
import com.example.SmartCart.User.Dto.User.UserResponseDto;
import com.example.SmartCart.User.Entity.User;
import com.example.SmartCart.User.Repository.UserRepo;
import com.example.SmartCart.User.enums.Gender;
import com.example.SmartCart.User.enums.UserRole;
import com.example.SmartCart.User.enums.UserStatus;
import com.example.SmartCart.common.Exception.ConflictException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private CreateUserDto createUserDto;

    @BeforeEach
    void setUp() {

        createUserDto = new CreateUserDto(
                "Sutirtha",
                "Paul",
                "sutirtha.test@gmail.com",
                "9876543210",
                "password123",
                LocalDate.of(2002, 5, 15),
                Gender.MALE,
                List.of()
        );
    }

    @Test
    void shouldCreateCustomerSuccessfully() {

        // Arrange
        User user = new User();
        user.setFirstName("Sutirtha");
        user.setLastName("Paul");
        user.setEmail("sutirtha.test@gmail.com");
        user.setPhoneNumber("9876543210");

        when(userRepo.existsByEmail(createUserDto.email()))
                .thenReturn(false);

        when(userRepo.existsByPhoneNumber(createUserDto.phoneNumber()))
                .thenReturn(false);

        when(userMapper.toUser(createUserDto))
                .thenReturn(user);

        when(passwordEncoder.encode(createUserDto.password()))
                .thenReturn("encodedPassword");

        when(userRepo.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDto expectedResponse = mock(UserResponseDto.class);

        when(userMapper.toUserResponseDto(user))
                .thenReturn(expectedResponse);

        // Act
        UserResponseDto actualResponse =
                userService.createUser(createUserDto);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepo).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("Sutirtha", savedUser.getFirstName());
        assertEquals("sutirtha.test@gmail.com", savedUser.getEmail());

        assertEquals("encodedPassword", savedUser.getPassword());

        assertTrue(savedUser.getRoles().contains(UserRole.CUSTOMER));

        assertEquals(UserStatus.ACTIVE, savedUser.getStatus());

        assertFalse(savedUser.getDeleted());
        assertTrue(savedUser.getEmailVerified());

        verify(userRepo).existsByEmail(createUserDto.email());
        verify(userRepo).existsByPhoneNumber(createUserDto.phoneNumber());

        verify(passwordEncoder).encode(createUserDto.password());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        // Arrange
        when(userRepo.existsByEmail(createUserDto.email()))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                ConflictException.class,
                () -> userService.createUser(createUserDto)
        );

        verify(userRepo, never()).save(any(User.class));

        verify(userMapper, never()).toUser(any());

        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldThrowExceptionWhenPhoneNumberAlreadyExists() {

        // Arrange
        when(userRepo.existsByEmail(createUserDto.email()))
                .thenReturn(false);

        when(userRepo.existsByPhoneNumber(createUserDto.phoneNumber()))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                ConflictException.class,
                () -> userService.createUser(createUserDto)
        );

        verify(userRepo, never()).save(any(User.class));

        verify(userMapper, never()).toUser(any());

        verifyNoInteractions(passwordEncoder);
    }
}
