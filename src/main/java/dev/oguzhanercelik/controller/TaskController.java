package dev.oguzhanercelik.controller;

import dev.oguzhanercelik.config.MdcConstant;
import dev.oguzhanercelik.model.dto.TaskDto;
import dev.oguzhanercelik.model.request.TaskCreateRequest;
import dev.oguzhanercelik.model.request.TaskUpdateRequest;
import dev.oguzhanercelik.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(@RequestBody @Valid TaskCreateRequest request, @RequestParam Integer listId) {
        log.info("Task created!");
        return taskService.create(listId, request, Integer.valueOf(MDC.get(MdcConstant.X_USER_ID)));
    }

    @GetMapping
    public List<TaskDto> getListsByUserId(@RequestParam Integer listId) {
        log.info("Tasks are returned for id {}", listId);
        return taskService.getTasksByListId(listId, Integer.valueOf(MDC.get(MdcConstant.X_USER_ID)));
    }

    @DeleteMapping("{taskId}")
    public void deleteById(@PathVariable Integer taskId) {
        taskService.deleteById(taskId, Integer.valueOf(MDC.get(MdcConstant.X_USER_ID)));
    }

    @PutMapping("/{taskId}")
    public TaskDto updateById(@PathVariable Integer taskId, @RequestBody @Valid TaskUpdateRequest request) {
        return taskService.updateById(taskId, request, Integer.valueOf(MDC.get(MdcConstant.X_USER_ID)));
    }

}
