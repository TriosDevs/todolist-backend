package dev.oguzhanercelik.converter;

import dev.oguzhanercelik.entity.List;
import dev.oguzhanercelik.model.dto.ListDto;
import dev.oguzhanercelik.model.request.ListCreateRequest;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
public class ListConverter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public List toEntity(ListCreateRequest request, Integer userId) {
        return List.builder()
                .name(request.getName())
                .userId(userId)
                .build();
    }

    public ListDto toDto(List list) {
        return ListDto.builder()
                .id(list.getId())
                .name(list.getName())
                .createdAt(list.getCreatedAt().format(DATE_TIME_FORMATTER))
                .count(list.getTasks() == null ? 0 : list.getTasks().size())
                .build();
    }

    public java.util.List<ListDto> toDtoList(java.util.List<List> lists) {
        return lists.stream().map(this::toDto).collect(Collectors.toList());
    }

}
