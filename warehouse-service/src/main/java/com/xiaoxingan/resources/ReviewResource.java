package com.xiaoxingan.resources;

import com.xiaoxingan.models.Review;
import com.xiaoxingan.services.ReviewService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/reviews")
public class ReviewResource {
    @Inject
    ReviewService reviewService;

    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviews(@PathParam("productId") Long productId) {
        List<Review> reviews = reviewService.getProductReviews(productId);
        return Response.ok(reviews).build();
    }
}
