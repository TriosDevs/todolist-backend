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
public class ListUpdateRequest {

    @Length(message = "listCreateRequest.name.length", min = 1, max = 20)
    @NotBlank(message = "listCreateRequest.name.notBlank")
    private String name;

}
