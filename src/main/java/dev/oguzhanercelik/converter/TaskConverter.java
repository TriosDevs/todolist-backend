package dev.oguzhanercelik.converter;

import dev.oguzhanercelik.entity.Task;
import dev.oguzhanercelik.model.dto.TaskDto;
import dev.oguzhanercelik.model.request.TaskCreateRequest;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskConverter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Task toEntity(Integer listId, TaskCreateRequest request, Integer userId) {
        return Task.builder()
                .name(request.getName())
                .listId(listId)
                .userId(userId)
                .build();
    }

    public TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .done(task.isDone())
                .createdAt(task.getCreatedAt().format(DATE_TIME_FORMATTER))
                .build();
    }

    public List<TaskDto> toDtoList(List<Task> tasks) {
        return tasks.stream().map(this::toDto).collect(Collectors.toList());
    }

}
