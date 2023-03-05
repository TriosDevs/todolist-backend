package dev.oguzhanercelik.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateRequest {

    @Length(message = "taskCreateRequest.name.length", min = 1, max = 20)
    @NotBlank(message = "taskCreateRequest.name.notBlank")
    private String name;
    private boolean done;

}
