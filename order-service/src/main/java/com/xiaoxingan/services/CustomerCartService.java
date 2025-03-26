package com.xiaoxingan.services;

import com.xiaoxingan.enums.Status;
import com.xiaoxingan.exceptions.orders.ConstructOrderFailureException;
import com.xiaoxingan.models.Customer;
import com.xiaoxingan.models.CustomerCart;
import com.xiaoxingan.models.Product;
import com.xiaoxingan.repositories.CustomerCartRepository;
import com.xiaoxingan.repositories.ProductRepository;
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
    @Inject
    ProductRepository productRepository;

    public List<CustomerCart> getCustomerCarts(Long customerId) {
        log.debug("Получать список заказов пользователя по идентификатору: {}", customerId);
        return customerCartRepository.findAllByCustomerId(customerId);
    }

    @Transactional
    public void constructOrder(Product product, Customer customer, short quantity) {
        try {
            short newQuantity = (short) (product.getQuantity() - quantity);
            product.setQuantity(newQuantity);
            log.debug("Пытаюсь обновить кол-во товара в {}", productRepository.getClass().getName());
            productRepository.persist(product);

            log.debug("Создаю новый заказ в {}", customerCartRepository.getClass().getName());
            customerCartRepository.insertCustomerCart(product, customer, quantity, Status.WAITING_FOR_CONFIRMATION, LocalDateTime.now(), LocalDateTime.now());
        } catch (Exception e) {
            log.error("Произошла ошибка во время создания нового заказа! " + e.getMessage());
            throw new ConstructOrderFailureException("Произошла ошибка во время создания заказа! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        log.debug("Удаляю заказ по идентификатору {} в {}", orderId, customerCartRepository.getClass().getName());
        customerCartRepository.deleteById(orderId);
    }
}
