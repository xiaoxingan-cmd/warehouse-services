package com.xiaoxingan.services;

import com.xiaoxingan.enums.Status;
import com.xiaoxingan.exceptions.orders.OrderNotFoundException;
import com.xiaoxingan.exceptions.orders.OrderUpdateFailureException;
import com.xiaoxingan.models.CustomerCart;
import com.xiaoxingan.repositories.CustomerCartRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class CustomerCartService {
    @Inject
    CustomerCartRepository customerCartRepository;

    public List<CustomerCart> getAllOrdersById(Long productId) {
        return customerCartRepository.findAllOrdersById(productId);
    }

    @Transactional
    public void updateOrderById(Long productId, Status status) {
        CustomerCart customerCart = customerCartRepository.findById(productId);
        if (customerCart == null) {
            throw new OrderNotFoundException("Заказ " + productId + " не найден.");
        }

        try {
            customerCart.setStatus(status);
            customerCart.setUpdatedAt(LocalDateTime.now());

            customerCartRepository.persist(customerCart);
        } catch (Exception e) {
            throw new OrderUpdateFailureException("Произошла ошибка при обновлении заказа " + productId + "." + " " + e.getMessage());
        }
    }
}
