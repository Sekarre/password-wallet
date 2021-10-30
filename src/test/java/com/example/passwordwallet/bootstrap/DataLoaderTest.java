package com.example.passwordwallet.bootstrap;

import com.example.passwordwallet.auth.repositories.UserRepository;
import com.example.passwordwallet.domain.User;
import com.example.passwordwallet.factories.UserMockFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DataLoaderTest {

    @Mock
    UserRepository userRepository;

    DataLoader dataLoader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        dataLoader = new DataLoader(userRepository);
    }

    @Test
    void should_save_users_into_database() {
        //given
        User user = UserMockFactory.buildDefaultUserMock();
        when(userRepository.save(any())).thenReturn(user);

        //when
        dataLoader.createUsers();

        //then
        verify(userRepository, times(3)).save(any());
    }
}