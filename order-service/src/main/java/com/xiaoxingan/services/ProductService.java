package com.xiaoxingan.services;

import com.xiaoxingan.dto.CustomerCartDTO;
import com.xiaoxingan.enums.Status;
import com.xiaoxingan.exceptions.orders.ConstructOrderFailureException;
import com.xiaoxingan.exceptions.products.ProductHasRunOutException;
import com.xiaoxingan.exceptions.products.ProductOrCustomerNotFoundException;
import com.xiaoxingan.models.Customer;
import com.xiaoxingan.models.Product;
import com.xiaoxingan.repositories.CustomerCartRepository;
import com.xiaoxingan.repositories.CustomerRepository;
import com.xiaoxingan.repositories.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;
    @Inject
    CustomerCartRepository customerCartRepository;
    @Inject
    CustomerRepository customerRepository;

    @Transactional
    public void orderProduct(CustomerCartDTO customerCartDTO) {
        try {
            log.debug("Ищу связи между Customer {} и Product {} в базе данных...", customerCartDTO.getCustomerId(), customerCartDTO.getProductId());
            Product product = productRepository.findByIdWithLock(customerCartDTO.getProductId());
            Customer customer = customerRepository.findById(customerCartDTO.getCustomerId());

            if (product == null || customer == null) {
                throw new ProductOrCustomerNotFoundException("Товар или Покупатель не найдены!");
            }

            short orderRemainQuantity = (short) (product.getQuantity() - customerCartDTO.getQuantity());
            if (orderRemainQuantity < 0) {
                throw new ProductHasRunOutException("Товар закончился на складе!");
            }

            product.setQuantity(orderRemainQuantity);

            log.debug("Пытаюсь обновить кол-во товара в {}", productRepository.getClass().getName());
            productRepository.persist(product);

            log.debug("Создаю новый заказ в {}", customerCartRepository.getClass().getName());
            customerCartRepository.insertCustomerCart(product, customer, customerCartDTO.getQuantity(), Status.WAITING_FOR_CONFIRMATION, LocalDateTime.now(), LocalDateTime.now());
        } catch (ProductOrCustomerNotFoundException e) {
            log.warn("Товар или Покупатель не найдены в базе данных!");
            throw e;
        } catch (ProductHasRunOutException e) {
            log.warn("Товар закончился на складе!");
            throw e;
        } catch (Exception e) {
            log.error("Произошла ошибка во время создания нового заказа! " + e.getMessage());
            throw new ConstructOrderFailureException("Произошла ошибка во время создания заказа! " + e.getMessage());
        }
    }
}
