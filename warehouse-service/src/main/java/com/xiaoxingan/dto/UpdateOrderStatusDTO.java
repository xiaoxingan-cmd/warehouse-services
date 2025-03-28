package com.xiaoxingan.dto;

import com.xiaoxingan.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusDTO {
    @NotEmpty
    int productId;
    @NotEmpty
    int customerId;
    @NotEmpty
    Status status;
}
