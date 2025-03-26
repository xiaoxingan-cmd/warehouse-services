package com.xiaoxingan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerCartDTO {
    @NotBlank(message = "productId не может быть пустым!")
    private Long productId;

    @NotBlank(message = "customerId не может быть пустым!")
    private Long customerId;

    @NotBlank(message = "quantity не может быть пустым!")
    private short quantity;
}
