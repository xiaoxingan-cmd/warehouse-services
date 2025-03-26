package com.xiaoxingan.repositories;

import com.xiaoxingan.models.Review;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ReviewRepository implements PanacheRepository<Review> {
    public List<Review> findByProductId(Long productId) {
//        return find("product_id", productId).list();

        return find("SELECT r FROM Review r JOIN FETCH r.product p JOIN FETCH r.customer c WHERE r.product.id = ?1", productId)
                .list();
    }
}
