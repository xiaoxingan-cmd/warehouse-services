package com.xiaoxingan.services;

import com.xiaoxingan.dto.CustomerDTO;
import com.xiaoxingan.exceptions.customers.CustomerUpdateFailureException;
import com.xiaoxingan.models.Customer;
import com.xiaoxingan.repositories.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class CustomerService {
    @Inject
    CustomerRepository customerRepository;

    public List<Customer> findAllCustomers() {
        return customerRepository.listAll();
    }

    @Transactional
    public void registerCustomer(CustomerDTO customerDTO) {
        try {
            Customer customer = new Customer();
            customer.setUsername(customerDTO.getUsername());
            customer.setAdress(customerDTO.getAdress());
            customer.setCountry(customerDTO.getCountry());
            customer.setUpdatedAt(LocalDateTime.now());
            customer.setCreatedAt(LocalDateTime.now());

            customerRepository.persist(customer);
        } catch (Exception e) {
            throw new CustomerUpdateFailureException("Пользователь уже зарегистрирован или произошла ошибка при обновлении данных! " + e.getMessage());
        }
    }
}
