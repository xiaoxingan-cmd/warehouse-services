package com.xiaoxingan.resources;

import com.xiaoxingan.enums.Status;
import com.xiaoxingan.exceptions.orders.OrderNotFoundException;
import com.xiaoxingan.exceptions.orders.OrderUpdateFailureException;
import com.xiaoxingan.models.Company;
import com.xiaoxingan.models.CustomerCart;
import com.xiaoxingan.services.CustomerCartService;
import jakarta.inject.Inject;
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

@Path("/orders")
@OpenAPIDefinition(
        info = @Info(title = "CustomerCart API", version = "1.0", description = "Сервис для получения корзины пользователя"),
        tags = { @Tag(name = "CustomerCart", description = "Методы для работы с корзиной") }
)
public class CustomerCartResource {
    @Inject
    CustomerCartService customerCartService;

    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Получить все заказы продукта", description = "Возвращает список всех кто заказал определенный продукт по ID.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = CustomerCart.class)))
    public Response getProducts(@PathParam("productId") Long productId) {
        List<CustomerCart> orders = customerCartService.getAllOrdersById(productId);
        return Response.ok(orders).build();
    }

    @POST
    @Path("/update/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Обновить статус заказа", description = "Обновляет статус заказа по ID.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response updateProductStatus(@PathParam("productId") Long productId, Status status) {
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
