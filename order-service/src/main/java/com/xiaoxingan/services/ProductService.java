package com.xiaoxingan.services;

import com.xiaoxingan.dto.CustomerCartDTO;
import com.xiaoxingan.exceptions.products.ProductHasRunOutException;
import com.xiaoxingan.models.Customer;
import com.xiaoxingan.models.Product;
import com.xiaoxingan.repositories.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;
    @Inject
    CustomerCartService customerCartService;

    @Transactional
    public void orderProduct(CustomerCartDTO customerCartDTO) {
        Object[] result = productRepository.findProductAndCustomer(customerCartDTO.getProductId(), customerCartDTO.getCustomerId());

        if (result == null || result.length < 2) {
            throw new IllegalArgumentException("Товар или клиент не найдены!");
        }

        Product product = (Product) result[0];
        Customer customer = (Customer) result[1];

        if (product.getQuantity() - customerCartDTO.getQuantity() < 0) {
            throw new ProductHasRunOutException("Товар закончился на складе!");
        }

        customerCartService.constructOrder(product, customer, customerCartDTO.getQuantity());
    }
}
