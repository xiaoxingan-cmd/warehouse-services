package com.xiaoxingan.resources;

import com.xiaoxingan.dto.ProductDTO;
import com.xiaoxingan.exceptions.companies.CompanyNotFoundException;
import com.xiaoxingan.exceptions.products.ProductNotFoundException;
import com.xiaoxingan.exceptions.products.ProductUpdateFailureException;
import com.xiaoxingan.exceptions.products.ProductsHaveDifferentCompanyNameException;
import com.xiaoxingan.models.CustomerCart;
import com.xiaoxingan.models.Product;
import com.xiaoxingan.services.ProductService;
import com.xiaoxingan.utils.validator.Validate;
import jakarta.inject.Inject;
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

@Path("/products")
@OpenAPIDefinition(
        info = @Info(title = "Product API", version = "1.0", description = "Сервис для работы с товарами"),
        tags = { @Tag(name = "Product", description = "Методы для работы с товарами") }
)
public class ProductResource {
    @Inject
    ProductService productService;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Получить все товары", description = "Возвращает все товары из базы данных.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Product.class)))
    public Response getProducts() {
        List<Product> products = productService.findAllProducts();
        return Response.ok(products).build();
    }

    @GET
    @Path("/{productName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Получить товар по имени", description = "Возвращает товар по его имени.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Product.class)))
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
    @Operation(summary = "Получить все товары компании", description = "Возвращает все товары зарегистрированные компанией.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Product.class)))
    public Response getProductsByCompany(@PathParam("companyName") String companyName) {
        List<Product> products = productService.findAllCompanyProducts(companyName);
        return Response.ok(products).build();
    }

    @GET
    @Path("/select/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Получить товар", description = "Возвращает товар по ID.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Product.class)))
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
    @Operation(summary = "Добавить новый товар", description = "Добавляет новый товар из DTO объекта.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
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
    @Operation(summary = "Добавить несколько продуктов разом", description = "Выполняет batch-запрос с использованием DTO.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
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
    @Operation(summary = "Обновить товар", description = "Обвновляет товар по ID.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
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
    @Operation(summary = "Удалить товар", description = "Удаляет товар по ID.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response deleteProduct(@PathParam("id") Long id) {
        productService.deleteProduct(id);

        return Response.ok().build();
    }
}
