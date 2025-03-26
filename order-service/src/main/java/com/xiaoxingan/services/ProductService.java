package com.xiaoxingan.services;

import com.xiaoxingan.dto.CustomerCartDTO;
import com.xiaoxingan.exceptions.products.ProductHasRunOutException;
import com.xiaoxingan.models.Customer;
import com.xiaoxingan.models.Product;
import com.xiaoxingan.repositories.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;
    @Inject
    CustomerCartService customerCartService;

    @Transactional
    public void orderProduct(CustomerCartDTO customerCartDTO) {
        log.debug("Ищу связи между Customer {} и Product {} в базе данных...", customerCartDTO.getCustomerId(), customerCartDTO.getProductId());
        Object[] result = productRepository.findProductAndCustomer(customerCartDTO.getProductId(), customerCartDTO.getCustomerId());

        if (result == null || result.length < 2) {
            log.warn("Товар или клиент не найдены в базе данных!");
            throw new IllegalArgumentException("Товар или клиент не найдены!");
        }

        Product product = (Product) result[0];
        Customer customer = (Customer) result[1];

        if (product.getQuantity() - customerCartDTO.getQuantity() < 0) {
            log.warn("Товар закончился на складе!");
            throw new ProductHasRunOutException("Товар закончился на складе!");
        }

        log.debug("Создаю новый заказ...");
        customerCartService.constructOrder(product, customer, customerCartDTO.getQuantity());
    }
}
