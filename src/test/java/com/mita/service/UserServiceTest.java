package com.mita.service;

import com.mita.dto.UserDto;
import com.mita.dto.request.UserStatsDto;
import com.mita.entity.User;
import com.mita.repository.CategoryRepository;
import com.mita.repository.ItemRepository;
import com.mita.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UploadService uploadService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setUsername("testUser");
        user.setAvatar("old-avatar.png");
        user.setAbout("about me");
    }

    @Test
    void getProfile_shouldReturnUserDto() {
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("test@mail.com");
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        UserDto result = userService.getProfile(authentication);

        assertEquals("test@mail.com", result.getEmail());
        assertEquals("testUser", result.getUsername());
        assertEquals("old-avatar.png", result.getAvatar());
        assertEquals("about me", result.getAbout());

        verify(userRepository).findByEmail("test@mail.com");
    }


    @Test
    void updateAvatar_shouldUpdateAvatarAndDeleteOldOne() {
        mockSecurityContext();

        Map<String, String> body = Map.of("avatar", "new-avatar.png");

        UserDto result = userService.updateAvatar(body);

        assertEquals("new-avatar.png", user.getAvatar());
        verify(userRepository).save(user);
        verify(uploadService).deletePosterIfExists("old-avatar.png");
        assertEquals("new-avatar.png", result.getAvatar());
    }

    @Test
    void updateAvatar_shouldNotDeleteIfSameAvatar() {
        mockSecurityContext();

        Map<String, String> body = Map.of("avatar", "old-avatar.png");

        userService.updateAvatar(body);

        verify(uploadService, never()).deletePosterIfExists(any());
    }


    @Test
    void updateAbout_shouldUpdateAbout() {
        mockSecurityContext();

        Map<String, String> body = Map.of("about", "new about");

        UserDto result = userService.updateAbout(body);

        assertEquals("new about", user.getAbout());
        verify(userRepository).save(user);
        assertEquals("new about", result.getAbout());
    }

    @Test
    void updateName_shouldUpdateUsername() {
        mockSecurityContext();

        Map<String, String> body = Map.of("name", "newName");

        UserDto result = userService.updateName(body);

        assertEquals("newName", user.getUsernameField());
        verify(userRepository).save(user);
        assertEquals("newName", result.getUsername());
    }


    @Test
    void getUserStats_shouldReturnStats() {
        mockSecurityContext();

        when(itemRepository.countByUserId(1L)).thenReturn(10);
        when(categoryRepository.countByUserId(1L)).thenReturn(5);

        UserStatsDto stats = userService.getUserStats();

        assertEquals(10, stats.getTotalItems());
        assertEquals(5, stats.getTotalCategories());

        verify(itemRepository).countByUserId(1L);
        verify(categoryRepository).countByUserId(1L);
    }


    @Test
    void getUserByEmail_shouldReturnUser() {
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        Map<String, String> body = Map.of("email", "test@mail.com");

        UserDto result = userService.getUserByEmail(body);

        assertEquals("test@mail.com", result.getEmail());
        verify(userRepository).findByEmail("test@mail.com");
    }

    @Test
    void getUserByEmail_shouldThrowExceptionIfNotFound() {
        when(userRepository.findByEmail("notfound@mail.com"))
                .thenReturn(Optional.empty());

        Map<String, String> body = Map.of("email", "notfound@mail.com");

        assertThrows(IllegalArgumentException.class,
                () -> userService.getUserByEmail(body));
    }


    @Test
    void deleteUserByEmail_shouldCallRepository() {
        Map<String, String> body = Map.of("email", "test@mail.com");

        userService.deleteUserByEmail(body);

        verify(userRepository).deleteByEmail("test@mail.com");
    }


    private void mockSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }
}