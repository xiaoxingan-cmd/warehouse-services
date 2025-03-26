package com.xiaoxingan.repositories;

import com.xiaoxingan.models.CustomerCart;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CustomerCartRepository implements PanacheRepository<CustomerCart> {
    public List<CustomerCart> findAllOrdersById(Long productId) {
        return find("products.id", productId).list();
    }
}
