package com.xiaoxingan.services;

import com.xiaoxingan.models.Review;
import com.xiaoxingan.repositories.ReviewRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ReviewService {
    @Inject
    ReviewRepository reviewRepository;

    public List<Review> getProductReviews(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}
