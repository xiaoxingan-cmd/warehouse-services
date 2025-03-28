package com.xiaoxingan.repositories;

import com.xiaoxingan.models.CustomerCart;
import com.xiaoxingan.models.CustomerCartId;
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

    public List<CustomerCart> findAllOrdersById(Long productId) {
        return find("SELECT c FROM CustomerCart c JOIN FETCH c.product p JOIN FETCH c.customer cu WHERE p.id = ?1", productId)
                .list();
    }

    public CustomerCart findByCartId(CustomerCartId cartId) {
        return find("SELECT c FROM CustomerCart c WHERE c.id.customerId = ?1 AND c.id.productId = ?2",
                cartId.getCustomerId(), cartId.getProductId())
                .firstResult();
    }

    @Transactional
    public void updateOrder(CustomerCart customerCart) {
        String query = "UPDATE CustomerCart c SET c.status = :status, c.updatedAt = :updatedAt WHERE c.id = :id";
        entityManager.createQuery(query)
                .setParameter("status", customerCart.getStatus())
                .setParameter("updatedAt", LocalDateTime.now())
                .setParameter("id", customerCart.getId())
                .executeUpdate();
    }
}
