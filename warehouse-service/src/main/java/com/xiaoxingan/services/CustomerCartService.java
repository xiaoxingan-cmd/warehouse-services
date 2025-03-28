package com.xiaoxingan.services;

import com.xiaoxingan.enums.Status;
import com.xiaoxingan.exceptions.orders.OrderNotFoundException;
import com.xiaoxingan.exceptions.orders.OrderUpdateFailureException;
import com.xiaoxingan.models.CustomerCart;
import com.xiaoxingan.models.CustomerCartId;
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
    public void updateOrderById(CustomerCartId cartId, Status status) {
        log.debug("Пытаюсь получить заказ по идетификатору: {} в {}", cartId, customerCartRepository.getClass().getName());
        CustomerCart customerCart = customerCartRepository.findByCartId(cartId);
        if (customerCart == null) {
            log.warn("В процессе получения заказа {} вернулся null", cartId);
            throw new OrderNotFoundException("Заказ " + cartId + " не найден.");
        }

        try {
            customerCart.setStatus(status);
            customerCart.setUpdatedAt(LocalDateTime.now());

            log.debug("Пытаюсь обновить заказ по идетификатору: {}", cartId);
            customerCartRepository.updateOrder(customerCart);
        } catch (Exception e) {
            log.error("Ошибка при попытке обновления заказа: {} ," + e.getMessage(), cartId);
            throw new OrderUpdateFailureException("Произошла ошибка при обновлении заказа " + cartId + "." + " " + e.getMessage());
        }
    }
}
