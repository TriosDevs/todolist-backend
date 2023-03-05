package dev.oguzhanercelik.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListDto {

    private Integer id;
    private String name;
    private String createdAt;
    private Integer count;

}
