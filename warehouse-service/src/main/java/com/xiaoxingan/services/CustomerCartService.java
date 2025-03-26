package com.xiaoxingan.services;

import com.xiaoxingan.enums.Status;
import com.xiaoxingan.exceptions.orders.OrderNotFoundException;
import com.xiaoxingan.exceptions.orders.OrderUpdateFailureException;
import com.xiaoxingan.models.CustomerCart;
import com.xiaoxingan.repositories.CustomerCartRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ApplicationScoped
public class CustomerCartService {
    @Inject
    CustomerCartRepository customerCartRepository;

    public List<CustomerCart> getAllOrdersById(Long productId) {
        log.debug("Пытаюсь получить все заказы товара: {} в {}", productId, customerCartRepository.getClass().getName());
        return customerCartRepository.findAllOrdersById(productId);
    }

    @Transactional
    public void updateOrderById(Long productId, Status status) {
        log.debug("Пытаюсь получить заказ по идетификатору: {} в {}", productId, customerCartRepository.getClass().getName());
        CustomerCart customerCart = customerCartRepository.findById(productId);
        if (customerCart == null) {
            log.warn("В процессе получения заказа {} вернулся null", productId);
            throw new OrderNotFoundException("Заказ " + productId + " не найден.");
        }

        try {
            customerCart.setStatus(status);
            customerCart.setUpdatedAt(LocalDateTime.now());

            log.debug("Пытаюсь обновить заказ по идетификатору: {}", productId);
            customerCartRepository.persist(customerCart);
        } catch (Exception e) {
            log.error("Ошибка при попытке обновления заказа: {} ," + e.getMessage(), productId);
            throw new OrderUpdateFailureException("Произошла ошибка при обновлении заказа " + productId + "." + " " + e.getMessage());
        }
    }
}
