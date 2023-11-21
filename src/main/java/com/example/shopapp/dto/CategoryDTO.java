package com.example.shopapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data

public class CategoryDTO {
    @NotEmpty(message = "Category's name can not be empty")
    private String name;

}
