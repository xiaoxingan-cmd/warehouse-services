package com.xiaoxingan.services;

import com.xiaoxingan.models.Review;
import com.xiaoxingan.repositories.ReviewRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
public class ReviewService {
    @Inject
    ReviewRepository reviewRepository;

    public List<Review> getProductReviews(Long productId) {
        log.debug("Получаю отзывы о товаре {} в {}", productId, reviewRepository.getClass().getName());
        return reviewRepository.findByProductId(productId);
    }
}
