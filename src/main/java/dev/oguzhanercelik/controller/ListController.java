package dev.oguzhanercelik.controller;

import dev.oguzhanercelik.config.MdcConstant;
import dev.oguzhanercelik.model.dto.ListDto;
import dev.oguzhanercelik.model.request.ListCreateRequest;
import dev.oguzhanercelik.model.request.ListUpdateRequest;
import dev.oguzhanercelik.service.ListService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/lists")
@RequiredArgsConstructor
public class ListController {

    private final ListService listService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ListDto create(@RequestBody @Valid ListCreateRequest request) {
        log.info("List created!");
        return listService.create(request, Integer.valueOf(MDC.get(MdcConstant.X_USER_ID)));
    }

    @GetMapping
    public List<ListDto> getListsByUserId() {
        log.info("Lists are returned for {}", MDC.get(MdcConstant.X_USER_ID));
        return listService.getListsByUserId(Integer.valueOf(MDC.get(MdcConstant.X_USER_ID)));
    }

    @DeleteMapping("/{listId}")
    public void deleteById(@PathVariable Integer listId) {
        log.info("List is deleted!");
        listService.deleteById(listId);
    }

    @PutMapping("/{listId}")
    public ListDto updateByListId(@PathVariable Integer listId, @RequestBody @Valid ListUpdateRequest request) {
        log.info("List is updated for id {}!", listId);
        return listService.updateByListId(listId, request);
    }

}
