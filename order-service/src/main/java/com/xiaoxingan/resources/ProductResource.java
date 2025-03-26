package com.xiaoxingan.resources;

import com.xiaoxingan.client.ProductServiceClient;
import com.xiaoxingan.dto.CustomerCartDTO;
import com.xiaoxingan.exceptions.customers.CustomerNotFoundException;
import com.xiaoxingan.exceptions.orders.ConstructOrderFailureException;
import com.xiaoxingan.exceptions.products.ProductHasRunOutException;
import com.xiaoxingan.exceptions.products.ProductNotFoundException;
import com.xiaoxingan.exceptions.products.ProductUpdateFailureException;
import com.xiaoxingan.models.Product;
import com.xiaoxingan.services.ProductService;
import com.xiaoxingan.utils.validator.Validate;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Map;

@Path("/products")
public class ProductResource {
    @Inject
    ProductService productService;
    @Inject
    @RestClient
    ProductServiceClient productServiceClient;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        List<Product> products = productServiceClient.getProducts();
        return Response.ok(products).build();
    }

    @GET
    @Path("/{productName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("productName") String name) {
        try {
            Response response = productServiceClient.getProduct(name);
            return Response.ok(response.readEntity(Product.class)).build();
        } catch (WebApplicationException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ошибка: " + e.getMessage())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("/company/{companyName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByCompany(@PathParam("companyName") String companyName) {
        try {
            Response response = productServiceClient.getProductsByCompany(companyName);
            return Response.ok(response.readEntity(Product.class)).build();
        } catch (WebApplicationException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ошибка: " + e.getMessage())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("/select/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductById(@PathParam("productId") Long productId) {
        try {
            Response response = productServiceClient.getProductById(productId);
            return Response.ok(response.readEntity(Product.class)).build();
        } catch (WebApplicationException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ошибка: " + e.getMessage())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @POST
    @Path("/order")
    @Produces(MediaType.APPLICATION_JSON)
    public Response orderProduct(@Valid CustomerCartDTO customerCartDTO) {
        Validate<CustomerCartDTO> validate = new Validate<>();
        if (!validate.validateData(customerCartDTO)) {
            return validate.getBadResponse();
        }

        try {
            productService.orderProduct(customerCartDTO);
            return Response.ok().build();
        } catch (ProductNotFoundException | CustomerNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("Ошибка ", e.getMessage()))
                    .build();
        } catch (ProductHasRunOutException e) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity(Map.of("Ошибка ", e.getMessage()))
                    .build();
        } catch (ConstructOrderFailureException | ProductUpdateFailureException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("Ошибка ", e.getMessage()))
                    .build();
        }
    }
}
