package com.lmrick.timescheduler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmrick.timescheduler.infrastructure.dto.*;
import com.lmrick.timescheduler.infrastructure.entity.Role;
import com.lmrick.timescheduler.infrastructure.entity.UserEntity;
import com.lmrick.timescheduler.security.JwtFilter;
import com.lmrick.timescheduler.security.JwtUtil;
import com.lmrick.timescheduler.services.AuthService;
import com.lmrick.timescheduler.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
class AuthControllerTest {
	
	@Autowired private MockMvc mockMvc;
	
	@MockitoBean private JwtUtil jwtUtil;
	
	@MockitoBean private JwtFilter jwtFilter;
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@MockitoBean private AuthenticationManager authenticationManager;
	
	@MockitoBean private UserService userService;
	
	@MockitoBean private AuthService authService;
	
	@Test
	void shouldRegisterUserSuccessfully() throws Exception {
		CreateUserDTO request = new CreateUserDTO("raphael", "123456", Role.USER);
		UserResponseDTO response = new UserResponseDTO("raphael", "USER");
		
		when(authService.createUser(any())).thenReturn(response);
		
		mockMvc.perform(post("/auth/register")
										.with(csrf()).contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(request)))
						
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.username").value("raphael"))
						.andExpect(jsonPath("$.role").value("USER"));
		
		verify(authService).createUser(any());
	}
	
	@Test
	void shouldReturnConflictWhenRegisterFails() throws Exception {
		CreateUserDTO request = new CreateUserDTO("raphael", "123456", Role.USER);
		
		when(authService.createUser(any())).thenThrow(RuntimeException.class);
		
		mockMvc.perform(post("/auth/register")
										.contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(request)))
						.andExpect(status().is5xxServerError());
	}
	
	@Test
	void shouldLoginSuccessfully() throws Exception {
		AuthRequestDTO request = new AuthRequestDTO("raphael", "123456");
		
		UserEntity user = new UserEntity();
		user.setUsername("raphael");
		
		UserResponseDTO userResponse = new UserResponseDTO("raphael", "USER");
		AuthResponseDTO response = new AuthResponseDTO("access-token", "refresh-token", userResponse);
		Authentication authentication = mock(Authentication.class);
		
		when(authenticationManager.authenticate(any())).thenReturn(authentication);
		when(authentication.getName()).thenReturn("raphael");
		when(userService.getEntityByUsername("raphael")).thenReturn(user);
		when(userService.login(user.getUsername())).thenReturn(response);
		
		mockMvc.perform(post("/auth/login")
										.contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(request)))
						
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.accessToken").value("access-token"))
						.andExpect(jsonPath("$.user.username").value("raphael"));
	}
	
	@Test
	void shouldRejectInvalidCredentials() throws Exception {
		AuthRequestDTO request =
						new AuthRequestDTO(
										"wrong",
										"password"
						);
		
		when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException());
		
		mockMvc.perform(post("/auth/login")
										.contentType(MediaType.APPLICATION_JSON)
										.content(mapper.writeValueAsString(request)))
						.andExpect(status().is5xxServerError());
		
		
	}
	
	@Test
	void shouldRefreshTokenSuccessfully() throws Exception {
		AuthResponseDTO response =
						new AuthResponseDTO(
										"new-access",
										"new-refresh",
										new UserResponseDTO(
														"raphael",
														"USER"
										)
						);
		
		when(userService.refresh("old-token")).thenReturn(response);
		
		RefreshTokenRequestDTO request = new RefreshTokenRequestDTO("old-token");
		
		mockMvc.perform(post("/auth/refresh")
														.contentType(MediaType.APPLICATION_JSON)
														.content(mapper.writeValueAsString(request)))
						
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.accessToken")
										.value("new-access"));
	}
	
	@Test
	void shouldLogoutSuccessfully() throws Exception {
		mockMvc.perform(post("/auth/logout")
										.header("Authorization", "Bearer jwt-token"))
						.andExpect(status().isOk());
		
		verify(userService).logout("jwt-token");
	}
	
}