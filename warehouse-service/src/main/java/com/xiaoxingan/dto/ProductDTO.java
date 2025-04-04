package com.xiaoxingan.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDTO {

    @NotNull(message = "Компания изготовитель не должна быть пустой!")
    private String company;

    @NotBlank(message = "Имя товара не должно быть пустым!")
    @Size(min = 1, max = 200, message = "Имя товара должно быть длинною от 1 до 200 символов!")
    private String name;

    @NotBlank(message = "Описание товара не должно быть пустым!")
    @Size(min = 1, max = 2000, message = "Описание товара должно быть длинною от 1 до 200 символов!")
    private String description;

    @NotNull(message = "Цена товара не должны быть пустой!")
    @Max(value = 1000000000)
    @Min(value = 1)
    private BigDecimal price;

    @NotNull(message = "Количество товара не должны быть пустым!")
    @Max(value = 10000)
    private short quantity;
}
