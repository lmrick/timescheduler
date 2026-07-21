package com.lmrick.timescheduler.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
				name = "scheduler",
				indexes = {
								@Index(name = "idx_scheduler_time", columnList = "scheduledTime")
				}
)
public class SchedulerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String product;
	
	@Column(nullable = false, length = 100)
	private String worker;
	
	@Column(nullable = false)
	private LocalDateTime scheduledTime;
	
	@Column(nullable = false)
	private Integer durationMinutes = 60;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SchedulerStatus status = SchedulerStatus.PENDING;
	
	@Column(nullable = false, length = 100)
	private String client;
	
	@Column(length = 25)
	private String clientPhone;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime insertTime;
	
	@PrePersist
	public void prePersist() {
		this.insertTime = LocalDateTime.now();
	}
	
}
