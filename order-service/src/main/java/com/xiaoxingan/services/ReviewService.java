package com.xiaoxingan.services;

import com.xiaoxingan.dto.ReviewDTO;
import com.xiaoxingan.exceptions.reviews.ReviewUpdateFailureException;
import com.xiaoxingan.models.Customer;
import com.xiaoxingan.models.Product;
import com.xiaoxingan.models.Review;
import com.xiaoxingan.repositories.ReviewRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ApplicationScoped
public class ReviewService {
    @Inject
    ReviewRepository reviewRepository;

    public List<Review> findAllReviewsByCustomerId(Long customerId) {
        log.debug("Ищу все отзывы пользователя {} в {}", customerId, reviewRepository.getClass().getName());
        return reviewRepository.findAllByCustomerId(customerId);
    }

    public List<Review> findReviewsByProductId(Long productId) {
        log.debug("Ищу все отзывы о продукте {} в {}", productId, reviewRepository.getClass().getName());
        return reviewRepository.findAllByProductId(productId);
    }

    @Transactional
    public void addReview(Long productId, Long customerId, ReviewDTO reviewDTO) {
        try {
            log.debug("Ищу совпадения Product {} и Customer {}...", productId, customerId);
            Object[] result = reviewRepository.findProductAndCustomer(productId, customerId);

            Product product = (Product) result[0];
            Customer customer = (Customer) result[1];

            if (product == null || customer == null) {
                throw new NoResultException();
            }

            log.debug("Пытаюсь добавить новый отзыв {}, {}", reviewDTO.getTitle(), reviewDTO.getDescription());
            Review review = new Review();
            review.setProduct(product);
            review.setCustomer(customer);
            review.setTitle(reviewDTO.getTitle());
            review.setDescription(reviewDTO.getDescription());
            review.setUpdatedAt(LocalDateTime.now());
            review.setCreatedAt(LocalDateTime.now());

            reviewRepository.persist(review);
        } catch (NoResultException e) {
            throw new NoResultException("Товар или клиент не найдены!");
        } catch (Exception e) {
            log.error("Произошла ошибка во время добавления нового отзыва! " + e.getMessage());
            throw new ReviewUpdateFailureException("Ошибка при добавлении отзыва к товару: " + productId + " ." + e.getMessage());
        }
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        log.debug("Удаляю отзыв {} в {}", reviewId, reviewRepository.getClass().getName());
        reviewRepository.deleteById(reviewId);
    }
}
