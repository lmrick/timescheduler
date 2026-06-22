package com.lmrick.timescheduler.infrastructure.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
	
	@Column(nullable = false)
	private String product;
	
	@Column(nullable = false)
	private String worker;
	
	@Schema(example = "2026-06-22T14:00:00")
	@Column(nullable = false)
	private LocalDateTime scheduledTime;
	
	@Column(nullable = false)
	private Integer durationMinutes = 60;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SchedulerStatus status = SchedulerStatus.PENDING;
	
	@Column(nullable = false)
	private String client;
	
	@Column(length = 20)
	private String clientPhone;
	
	@Column(updatable = false)
	private LocalDateTime insertTime;
	
	@PrePersist
	public void prePersist() {
		this.insertTime = LocalDateTime.now();
	}
	
}
