package com.xiaoxingan.repositories;

import com.xiaoxingan.models.Review;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ReviewRepository implements PanacheRepository<Review> {
    public List<Review> findAllByCustomerId(Long customerId) {
        return find("customer.id", customerId).list();
    }
    public List<Review> findAllByProductId(Long productId) {
        return find("product.id", productId).list();
    }

    public Object[] findProductAndCustomer(Long productId, Long customerId) {
        return getEntityManager()
                .createQuery("SELECT p, c FROM Product p, Customer c WHERE p.id = :productId AND c.id = :customerId", Object[].class)
                .setParameter("productId", productId)
                .setParameter("customerId", customerId)
                .getSingleResult();
    }
}
