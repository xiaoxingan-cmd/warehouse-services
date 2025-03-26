package com.xiaoxingan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
    @NotBlank(message = "Имя пользователя не может быть пустым!")
    @Size(min = 1, max = 100, message = "Имя пользователя должно быть от 1 до 100 символов длинною!")
    private String username;

    @NotBlank(message = "Адрес не может быть пустым!")
    @Size(min = 1, max = 1000, message = "Адрес должен быть от 1 до 1000 символов длинною!")
    private String adress;

    @NotBlank(message = "Страна не может быть пустой!")
    @Size(min = 1, max = 200, message = "Страна должна быть от 1 до 200 символов длинною!")
    private String country;
}
