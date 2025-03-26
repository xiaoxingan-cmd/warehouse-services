package com.xiaoxingan.resources;

import com.xiaoxingan.dto.ProductDTO;
import com.xiaoxingan.exceptions.companies.CompanyNotFoundException;
import com.xiaoxingan.exceptions.products.ProductNotFoundException;
import com.xiaoxingan.exceptions.products.ProductUpdateFailureException;
import com.xiaoxingan.exceptions.products.ProductsHaveDifferentCompanyNameException;
import com.xiaoxingan.models.Product;
import com.xiaoxingan.services.ProductService;
import com.xiaoxingan.utils.validator.Validate;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/products")
public class ProductResource {
    @Inject
    ProductService productService;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        List<Product> products = productService.findAllProducts();
        return Response.ok(products).build();
    }

    @GET
    @Path("/{productName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduct(@PathParam("productName") String name) {
        Product product = productService.findProductByName(name);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(product).build();
    }

    @GET
    @Path("/company/{companyName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByCompany(@PathParam("companyName") String companyName) {
        List<Product> products = productService.findAllCompanyProducts(companyName);
        return Response.ok(products).build();
    }

    @GET
    @Path("/select/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductById(@PathParam("productId") Long productId) {
        Product product = productService.findProductById(productId);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(product).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(@Valid ProductDTO productDTO) {
        Validate<ProductDTO> validate = new Validate<>();
        if (!validate.validateData(productDTO)) {
            return validate.getBadResponse();
        }

        try {
            productService.addProduct(productDTO);
        } catch (CompanyNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("Ошибка", e.getMessage()))
                    .build();
        } catch (ProductUpdateFailureException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("Ошибка", e.getMessage()))
                    .build();
        }

        return Response.ok().build();
    }

    @POST
    @Path("/add/batch")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProducts(@Valid List<ProductDTO> productDTO) {
        Validate<List<ProductDTO>> validate = new Validate<>();
        if (!validate.validateData(productDTO)) {
            return validate.getBadResponse();
        }

        try {
            productService.addProducts(productDTO);
        } catch (CompanyNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("Ошибка", e.getMessage()))
                    .build();
        } catch (ProductUpdateFailureException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("Ошибка", e.getMessage()))
                    .build();
        } catch (ProductsHaveDifferentCompanyNameException e) {
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(Map.of("Ошибка", e.getMessage()))
                    .build();
        }

        return Response.ok().build();
    }

    @POST
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(@PathParam("id") Long id, @Valid ProductDTO productDTO) {
        Validate<ProductDTO> validate = new Validate<>();
        if (!validate.validateData(productDTO)) {
            return validate.getBadResponse();
        }

        try {
            productService.updateProduct(id, productDTO);
        } catch (ProductUpdateFailureException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("Ошибка", e.getMessage()))
                    .build();
        } catch (ProductNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("Ошибка", e.getMessage()))
                    .build();
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@PathParam("id") Long id) {
        productService.deleteProduct(id);

        return Response.ok().build();
    }
}
