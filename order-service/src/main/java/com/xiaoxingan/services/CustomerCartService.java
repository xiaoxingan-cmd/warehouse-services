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

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class CustomerCartService {
    @Inject
    CustomerCartRepository customerCartRepository;
    @Inject
    ProductRepository productRepository;

    public List<CustomerCart> getCustomerCarts(Long customerId) {
        return customerCartRepository.findAllByCustomerId(customerId);
    }

    @Transactional
    public void constructOrder(Product product, Customer customer, short quantity) {
        try {
            short newQuantity = (short) (product.getQuantity() - quantity);
            product.setQuantity(newQuantity);
            productRepository.persist(product);

            customerCartRepository.insertCustomerCart(product, customer, quantity, Status.WAITING_FOR_CONFIRMATION, LocalDateTime.now(), LocalDateTime.now());
        } catch (Exception e) {
            throw new ConstructOrderFailureException("Произошла ошибка во время создания заказа! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        customerCartRepository.deleteById(orderId);
    }
}
