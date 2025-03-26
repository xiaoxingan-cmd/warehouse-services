package com.xiaoxingan.client;

import com.xiaoxingan.models.Product;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/products")
@RegisterRestClient(baseUri = "http://warehouse-service:8080")
public interface ProductServiceClient {
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    List<Product> getProducts();

    @GET
    @Path("/{productName}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getProduct(@PathParam("productName") String name);

    @GET
    @Path("/company/{companyName}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getProductsByCompany(@PathParam("companyName") String companyName);

    @GET
    @Path("/select/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getProductById(@PathParam("productId") Long productId);
}
