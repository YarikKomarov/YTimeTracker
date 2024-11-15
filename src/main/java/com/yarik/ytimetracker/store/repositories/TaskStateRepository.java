package com.yarik.ytimetracker.store.repositories;

import com.yarik.ytimetracker.store.entities.TaskEntity;
import com.yarik.ytimetracker.store.entities.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {

}
