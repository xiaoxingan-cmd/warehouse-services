package com.xiaoxingan.resources;

import com.xiaoxingan.enums.Status;
import com.xiaoxingan.exceptions.orders.OrderNotFoundException;
import com.xiaoxingan.exceptions.orders.OrderUpdateFailureException;
import com.xiaoxingan.models.CustomerCart;
import com.xiaoxingan.services.CustomerCartService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/orders")
public class CustomerCartResource {
    @Inject
    CustomerCartService customerCartService;

    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts(@PathParam("productId") Long productId) {
        List<CustomerCart> orders = customerCartService.getAllOrdersById(productId);
        return Response.ok(orders).build();
    }

    @POST
    @Path("/update/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCompany(@PathParam("productId") Long productId, Status status) {
        try {
            customerCartService.updateOrderById(productId, status);
        } catch (OrderNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("Ошибка", e.getMessage()))
                    .build();
        } catch (OrderUpdateFailureException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("Ошибка", e.getMessage()))
                    .build();
        }

        return Response.ok().build();
    }
}
