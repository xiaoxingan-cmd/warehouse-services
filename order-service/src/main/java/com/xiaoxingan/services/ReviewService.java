package com.xiaoxingan.services;

import com.xiaoxingan.dto.ReviewDTO;
import com.xiaoxingan.exceptions.reviews.ReviewUpdateFailureException;
import com.xiaoxingan.models.Customer;
import com.xiaoxingan.models.Product;
import com.xiaoxingan.models.Review;
import com.xiaoxingan.repositories.ProductRepository;
import com.xiaoxingan.repositories.ReviewRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ReviewService {
    @Inject
    ReviewRepository reviewRepository;
    @Inject
    ProductRepository productRepository;

    public List<Review> findAllReviewsByCustomerId(Long customerId) {
        return reviewRepository.findAllByCustomerId(customerId);
    }

    public List<Review> findReviewsByProductId(Long productId) {
        return reviewRepository.findAllByProductId(productId);
    }

    @Transactional
    public void addReview(Long productId, Long customerId, ReviewDTO reviewDTO) {
        Object[] result = reviewRepository.findProductAndCustomer(productId, customerId);

        if (result == null || result.length < 2) {
            throw new IllegalArgumentException("Товар или клиент не найдены!");
        }

        Product product = (Product) result[0];
        Customer customer = (Customer) result[1];

        try {
            Review review = new Review();
            review.setProduct(product);
            review.setCustomer(customer);
            review.setTitle(reviewDTO.getTitle());
            review.setDescription(reviewDTO.getDescription());
            review.setUpdatedAt(LocalDateTime.now());
            review.setCreatedAt(LocalDateTime.now());

            reviewRepository.persist(review);
        } catch (Exception e) {
            throw new ReviewUpdateFailureException("Ошибка при добавлении отзыва к товару: " + productId + " ." + e.getMessage());
        }
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        productRepository.deleteById(reviewId);
    }
}
