package com.xiaoxingan.resources;

import com.xiaoxingan.models.CustomerCart;
import com.xiaoxingan.services.CustomerCartService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/carts")
public class CustomerCartResource {
    @Inject
    CustomerCartService customerCartService;

    @GET
    @Path("/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerCart(@PathParam("customerId") Long customerId) {
        List<CustomerCart> customerCarts = customerCartService.getCustomerCarts(customerId);
        return Response.ok(customerCarts).build();
    }

    @DELETE
    @Path("/delete/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCustomer(@PathParam("orderId") Long orderId) {
        customerCartService.deleteOrder(orderId);
        return Response.ok().build();
    }
}
