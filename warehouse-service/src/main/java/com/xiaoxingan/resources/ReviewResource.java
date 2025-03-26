package com.xiaoxingan.resources;

import com.xiaoxingan.models.CustomerCart;
import com.xiaoxingan.models.Review;
import com.xiaoxingan.services.ReviewService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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

@Path("/reviews")
@OpenAPIDefinition(
        info = @Info(title = "Review API", version = "1.0", description = "Сервис для получения отзывов"),
        tags = { @Tag(name = "Review", description = "Методы для работы с отзывами") }
)
public class ReviewResource {
    @Inject
    ReviewService reviewService;

    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Получить все отзывы продукта", description = "Возвращает список всех отзывов продукта по ID.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Review.class)))
    public Response getReviews(@PathParam("productId") Long productId) {
        List<Review> reviews = reviewService.getProductReviews(productId);
        return Response.ok(reviews).build();
    }
}
