package com.xiaoxingan.resources;

import com.xiaoxingan.dto.ReviewDTO;
import com.xiaoxingan.exceptions.reviews.ReviewUpdateFailureException;
import com.xiaoxingan.models.Review;
import com.xiaoxingan.services.ReviewService;
import com.xiaoxingan.utils.validator.Validate;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Path("/reviews")
@OpenAPIDefinition(
        info = @Info(title = "Review API", version = "1.0", description = "Сервис для работы с отзывами"),
        tags = { @Tag(name = "Review", description = "Методы для работы с отзывами") }
)
public class ReviewResource {
    @Inject
    ReviewService reviewService;

    @GET
    @Path("/list/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Получить все отзывы пользователя", description = "Возвращает все отзывы пользователя по ID.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Review.class)))
    public Response getAllUsersReviews(@PathParam("customerId") Long customerId) {
        List<Review> reviews = reviewService.findAllReviewsByCustomerId(customerId);
        return Response.ok(reviews).build();
    }

    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Получить все отзывы на товар", description = "Возвращает все отзывы на выбранный товар.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Review.class)))
    public Response listAllProductReviewS(@PathParam("productId") Long productId) {
        List<Review> reviews = reviewService.findReviewsByProductId(productId);
        return Response.ok(reviews).build();
    }

    @POST
    @Path("/{productId}/add/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Добавить отзыв на товар", description = "Добавляет отзыв на выбранный товар.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response addProductReview(@PathParam("productId") Long productId, @PathParam("customerId") Long customerId, @Valid ReviewDTO reviewDTO) {
        Validate<ReviewDTO> validate = new Validate<>();
        if (!validate.validateData(reviewDTO)) {
            return validate.getBadResponse();
        }

        try {
            reviewService.addReview(productId, customerId, reviewDTO);
        } catch (ReviewUpdateFailureException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("Ошибка ", e.getMessage()))
                    .build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("Ошибка ", e.getMessage()))
                    .build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/{productId}/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Удалить отзыв на товар", description = "Удаляет отзыв на выбранный товар.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response deleteProductReview(@PathParam("productId") Long productId) {
        reviewService.deleteReview(productId);
        return Response.ok().build();
    }
}
