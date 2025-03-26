package com.xiaoxingan.repositories;

import com.xiaoxingan.models.Review;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ReviewRepository implements PanacheRepository<Review> {
    public Object[] findProductAndCustomer(Long productId, Long customerId) {
        return getEntityManager()
                .createQuery("SELECT p, c FROM Product p, Customer c WHERE p.id = :productId AND c.id = :customerId", Object[].class)
                .setParameter("productId", productId)
                .setParameter("customerId", customerId)
                .getSingleResult();
    }

    public List<Review> findAllByCustomerId(Long customerId) {
        return find("SELECT r FROM Review r JOIN FETCH r.product p JOIN FETCH r.customer c WHERE r.customer.id = ?1", customerId)
                .list();
    }

    public List<Review> findAllByProductId(Long productId) {
        return find("SELECT r FROM Review r JOIN FETCH r.product p JOIN FETCH r.customer c WHERE r.product.id = ?1", productId)
                .list();
    }
}
