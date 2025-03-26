package com.xiaoxingan.repositories;

import com.xiaoxingan.enums.Status;
import com.xiaoxingan.models.Customer;
import com.xiaoxingan.models.CustomerCart;
import com.xiaoxingan.models.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class CustomerCartRepository implements PanacheRepository<CustomerCart> {
    private final EntityManager entityManager;

    public CustomerCartRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<CustomerCart> findAllByCustomerId(Long customerId) {
        return find("SELECT c FROM CustomerCart c JOIN FETCH c.product p JOIN FETCH c.customer cu WHERE c.customer.id = ?1", customerId)
                .list();
    }

    @Transactional
    public void insertCustomerCart(Product product, Customer customer, short quantity, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        String query = "INSERT INTO customers_cart (products_id, customers_id, quantity, status, created_at, updated_at) " +
                "VALUES (" + product.getId() + ", " + customer.getId() + ", " + quantity + ", '" + status.name() + "', '" + createdAt + "', '" + updatedAt + "')";

        entityManager.createNativeQuery(query).executeUpdate();
    }
}
