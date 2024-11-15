package com.yarik.ytimetracker.store.repositories;

import com.yarik.ytimetracker.store.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> { //класс для работы с сущностями проекта
    Optional<ProjectEntity> findByName(String name);
    Stream<ProjectEntity> streamAll();
    Stream<ProjectEntity> streamAllByNameStartingWithIgnoreCase(String prefixName);
}
