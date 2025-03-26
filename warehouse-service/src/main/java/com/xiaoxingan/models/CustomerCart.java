package com.xiaoxingan.models;

import com.xiaoxingan.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customers_cart")
public class CustomerCart {

    @EmbeddedId
    private CustomerCartId id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "products_id", nullable = false)
    private Product product;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customers_id", nullable = false)
    private Customer customer;

    @Column(name = "quantity", nullable = false)
    private short quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime updatedAt;
}

