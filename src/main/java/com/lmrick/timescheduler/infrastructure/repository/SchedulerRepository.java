package com.lmrick.timescheduler.infrastructure.repository;

import com.lmrick.timescheduler.infrastructure.entity.SchedulerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerEntity, Long> {
	
	List<SchedulerEntity> findByScheduledTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
	List<SchedulerEntity> findByWorker(String worker);
	List<SchedulerEntity> findByWorkerAndScheduledTimeBetween(
					String worker,
					LocalDateTime start,
					LocalDateTime end
	);
	
}
