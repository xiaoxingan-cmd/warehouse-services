package com.xiaoxingan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDTO {
    @NotBlank(message = "Имя компании не должно быть пустым!")
    @Size(min = 1, max = 200, message = "Имя компании должно быть длинною от 1 до 200 символов!")
    private String name;

    @Size(max = 2000, message = "Описание компании не должно быть больше чем 2000 символов!")
    private String description;

    @NotBlank(message = "Страна компании не должна быть пустой!")
    @Size(min = 1, max = 200, message = "Страна должна быть длинною от 1 до 200 символов!")
    private String country;
}
