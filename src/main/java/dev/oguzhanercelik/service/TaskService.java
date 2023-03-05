package dev.oguzhanercelik.service;

import dev.oguzhanercelik.converter.TaskConverter;
import dev.oguzhanercelik.entity.Task;
import dev.oguzhanercelik.exception.ApiException;
import dev.oguzhanercelik.model.dto.TaskDto;
import dev.oguzhanercelik.model.error.ErrorEnum;
import dev.oguzhanercelik.model.request.TaskCreateRequest;
import dev.oguzhanercelik.model.request.TaskUpdateRequest;
import dev.oguzhanercelik.repository.ListRepository;
import dev.oguzhanercelik.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskConverter taskConverter;
    private final ListRepository listRepository;

    @Transactional
    public TaskDto create(Integer listId, TaskCreateRequest request, Integer userId) {
        if (taskRepository.countByListIdAndUserId(listId, userId) >= 10){
            throw new ApiException(ErrorEnum.TASK_MAX_LENGTH);
        }
        checkListExists(listId, userId);
        Task task = taskConverter.toEntity(listId, request, userId);
        return taskConverter.toDto(taskRepository.save(task));
    }

    public List<TaskDto> getTasksByListId(Integer listId, Integer userId) {
        checkListExists(listId, userId);
        return taskConverter.toDtoList(taskRepository.findByListId(listId));
    }

    @Transactional
    public void deleteById(Integer taskId, Integer userId) {
        taskRepository.deleteByIdAndUserId(taskId, userId);
    }

    public TaskDto updateById(Integer taskId, TaskUpdateRequest request, Integer userId) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId).orElseThrow(() -> new ApiException(ErrorEnum.TASK_NOT_FOUND));
        task.setName(request.getName());
        task.setDone(request.isDone());
        return taskConverter.toDto(taskRepository.save(task));
    }

    public void checkListExists(Integer listId, Integer userId) {
        if (BooleanUtils.isFalse(listRepository.existsByIdAndUserId(listId, userId))) {
            throw new ApiException(ErrorEnum.LIST_NOT_FOUND);
        }
    }


}
