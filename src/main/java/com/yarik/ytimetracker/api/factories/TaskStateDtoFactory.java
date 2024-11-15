package com.yarik.ytimetracker.api.factories;

import com.yarik.ytimetracker.api.dto.ProjectDto;
import com.yarik.ytimetracker.api.dto.TaskStateDto;
import com.yarik.ytimetracker.store.entities.TaskStateEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskStateDtoFactory {
    public TaskStateDto makeTaskStateDto(TaskStateEntity entity){
        return TaskStateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .ordinal(entity.getOrdinal())
                .build();
    }
}
