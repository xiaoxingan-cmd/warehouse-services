package com.xiaoxingan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    @NotBlank(message = "Заголовок не должен быть пустым!")
    @Size(min = 1, max = 200, message = "Заголовок должен быть длинною от 1 до 200 символов!")
    private String title;

    @NotBlank(message = "Описание не должен быть пустым!")
    @Size(min = 1, max = 2000, message = "Заголовок должен быть длинною от 1 до 2000 символов!")
    private String description;
}
