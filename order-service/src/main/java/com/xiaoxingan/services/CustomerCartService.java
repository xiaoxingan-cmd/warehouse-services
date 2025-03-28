package com.xiaoxingan.services;

import com.xiaoxingan.models.CustomerCart;
import com.xiaoxingan.models.CustomerCartId;
import com.xiaoxingan.repositories.CustomerCartRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
public class CustomerCartService {
    @Inject
    CustomerCartRepository customerCartRepository;

    public List<CustomerCart> getCustomerCarts(Long customerId) {
        log.debug("Получаю список заказов пользователя по идентификатору: {}", customerId);
        return customerCartRepository.findAllByCustomerId(customerId);
    }

    @Transactional
    public void deleteOrder(CustomerCartId cartId) {
        log.debug("Удаляю заказ по идентификатору {} в {}", cartId, customerCartRepository.getClass().getName());
        customerCartRepository.deleteByCustomerCartId(cartId);
    }
}
