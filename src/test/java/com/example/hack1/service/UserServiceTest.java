package com.example.hack1.service;

import com.example.hack1.dto.UserDto;
import com.example.hack1.model.User;
import com.example.hack1.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService service;

    @Test
    void create() {
        User saved = new User(8L,3L,"x@y.com");
        when(repo.save(any())).thenReturn(saved);

        UserDto out = service.create(new UserDto(null,3L,"x@y.com"));
        assertThat(out.getId()).isEqualTo(8L);
        verify(repo).save(any(User.class));
    }

    // Completa con listByCompany, update...
}