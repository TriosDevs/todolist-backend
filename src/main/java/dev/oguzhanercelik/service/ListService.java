package dev.oguzhanercelik.service;

import dev.oguzhanercelik.converter.ListConverter;
import dev.oguzhanercelik.entity.List;
import dev.oguzhanercelik.exception.ApiException;
import dev.oguzhanercelik.model.dto.ListDto;
import dev.oguzhanercelik.model.error.ErrorEnum;
import dev.oguzhanercelik.model.request.ListCreateRequest;
import dev.oguzhanercelik.model.request.ListUpdateRequest;
import dev.oguzhanercelik.repository.ListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ListRepository listRepository;
    private final ListConverter listConverter;

    @Transactional
    public ListDto create(ListCreateRequest request, Integer userId) {
        if (listRepository.countByUserId(userId) >= 5) {
            throw new ApiException(ErrorEnum.LIST_MAX_LENGTH);
        }
        final List list = listConverter.toEntity(request, userId);
        return listConverter.toDto(listRepository.save(list));
    }

    public java.util.List<ListDto> getListsByUserId(Integer userId) {
        final java.util.List<List> list = listRepository.findByUserId(userId);
        return listConverter.toDtoList(list);
    }

    public void deleteById(Integer listId) {
        listRepository.deleteById(listId);
    }

    public ListDto updateByListId(Integer listId, ListUpdateRequest request) {
        final List list = listRepository.findById(listId).orElseThrow(() -> new ApiException(ErrorEnum.LIST_NOT_FOUND));
        list.setName(request.getName());
        return listConverter.toDto(listRepository.save(list));
    }
}
