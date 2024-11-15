package com.yarik.ytimetracker.api.controllers;

import com.yarik.ytimetracker.api.dto.AskDto;
import com.yarik.ytimetracker.api.dto.ProjectDto;
import com.yarik.ytimetracker.api.exceptions.BadRequestException;
import com.yarik.ytimetracker.api.exceptions.NotFoundException;
import com.yarik.ytimetracker.api.factories.ProjectDtoFactory;
import com.yarik.ytimetracker.store.entities.ProjectEntity;
import com.yarik.ytimetracker.store.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
//все финальные поля автоматически запихивает в конструктор. Инициализация бина через конструктор
@RestController     //Все методы данного контроллера возвращают response body
@Transactional
public class ProjectController {
    private final ProjectDtoFactory projectDtoFactory;
    private final ProjectRepository projectRepository;
    public static final String FETCH_PROJECTS = "/api/projects"; //ищет по критериям в отличии от GET
    public static final String DELETE_PROJECT = "/api/projects/{project_id}";
    public static final String CREATE_OR_UPDATE_PROJECT = "/api/projects";


    @GetMapping(FETCH_PROJECTS)
    public List<ProjectDto> fetchProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {
        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartingWithIgnoreCase)
                .orElseGet(projectRepository::streamAll);

        return projectStream
                .map(projectDtoFactory::makeProjectDto)
                .collect(Collectors.toList());

    }

    @PutMapping(CREATE_OR_UPDATE_PROJECT)
    public ProjectDto createOrUpdateProject(
            @RequestParam(value = "project_id", required = false) Optional<Long> optionalProjectId,
            @RequestParam(value = "project_name", required = false) Optional<String> optionalProjectName
            //Another param...
    ) {

        optionalProjectName = optionalProjectName.filter(projectName -> !projectName.trim().isEmpty());

        boolean isCreate = !optionalProjectId.isPresent();

        final ProjectEntity project = optionalProjectId
                .map(this::getProjectOrthrowException)
                .orElseGet(() -> ProjectEntity.builder().build());

        if (isCreate && !optionalProjectName.isPresent()) {
            throw new BadRequestException("Project name can't be empty");
        }

        optionalProjectName
                .ifPresent(projecName -> {
                    projectRepository
                            .findByName(projecName)
                            .filter(anotherProject -> !Objects.equals(anotherProject.getId(), project.getId()))
                            .ifPresent(anotherProject -> {
                                throw new BadRequestException(
                                        String.format("Project \"%s\" already exists.", projecName)
                                );
                            });
                    project.setName(projecName);
                });
        final ProjectEntity savedProject = projectRepository.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(savedProject);
    }


    @DeleteMapping(DELETE_PROJECT)
    public AskDto deleteProject(Long projectId) {

        ProjectEntity project = getProjectOrthrowException(projectId);

        projectRepository.deleteById(projectId);
        return AskDto.makeDefault(true);
    }

    private ProjectEntity getProjectOrthrowException(Long projectId) {
        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Project with \"%s\" doesn't exist.", projectId
                        )
                        )
                );
    }

}
