package com.xiaoxingan.resources;

import com.xiaoxingan.dto.ReviewDTO;
import com.xiaoxingan.exceptions.reviews.ReviewUpdateFailureException;
import com.xiaoxingan.models.Review;
import com.xiaoxingan.services.ReviewService;
import com.xiaoxingan.utils.validator.Validate;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/reviews")
public class ReviewResource {
    @Inject
    ReviewService reviewService;

    @GET
    @Path("/list/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsersReviews(@PathParam("customerId") Long customerId) {
        List<Review> reviews = reviewService.findAllReviewsByCustomerId(customerId);
        return Response.ok(reviews).build();
    }

    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllProductReviewS(@PathParam("productId") Long productId) {
        List<Review> reviews = reviewService.findReviewsByProductId(productId);
        return Response.ok(reviews).build();
    }

    @POST
    @Path("/{productId}/add/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
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
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("Ошибка ", e.getMessage()))
                    .build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path("/{productId}/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProductReview(@PathParam("productId") Long productId) {
        reviewService.deleteReview(productId);
        return Response.ok().build();
    }
}
