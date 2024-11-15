package com.yarik.ytimetracker.store.repositories;

import com.yarik.ytimetracker.store.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

}
