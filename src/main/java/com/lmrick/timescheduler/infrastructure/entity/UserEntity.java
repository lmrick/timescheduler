package com.lmrick.timescheduler.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(
				name = "users",
				indexes = {
								@Index(name = "idx_users_username", columnList = "username")
				}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(
					nullable = false,
					unique = true,
					length = 50
	)
	private String username;
	
	@Column(
					nullable = false,
					length = 100
	)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Role role;
	
	@Column(nullable = false)
	private Long tokenVersion = 0L;
	
	@Column(length = 255)
	private String refreshTokenHash;
	
}
