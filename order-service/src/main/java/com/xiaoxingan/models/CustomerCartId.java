package com.xiaoxingan.models;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class CustomerCartId implements Serializable {

    private int productId;
    private int customerId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CustomerCartId that = (CustomerCartId) o;
        return Objects.equals(productId, that.productId) && Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, customerId);
    }
}

