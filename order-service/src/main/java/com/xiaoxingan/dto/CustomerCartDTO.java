package com.xiaoxingan.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerCartDTO {
    @NotNull(message = "productId не может быть пустым!")
    @Max(value = 1000000000)
    @Min(value = 1)
    private Long productId;

    @NotNull(message = "customerId не может быть пустым!")
    @Max(value = 1000000000)
    @Min(value = 1)
    private Long customerId;

    @NotNull(message = "quantity не может быть пустым!")
    @Max(value = 10000, message = "Максимум 10000 товаров")
    private short quantity;
}
