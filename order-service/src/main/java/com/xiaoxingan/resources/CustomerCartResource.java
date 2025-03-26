package com.xiaoxingan.resources;

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

@Path("/carts")
@OpenAPIDefinition(
        info = @Info(title = "CustomerCart API", version = "1.0", description = "Сервис для работы с корзиной пользователя"),
        tags = { @Tag(name = "CustomerCart", description = "Методы для работы с корзиной пользователя") }
)
public class CustomerCartResource {
    @Inject
    CustomerCartService customerCartService;

    @GET
    @Path("/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Получить корзину пользователя", description = "Возвращает корзину по ID пользователя.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = CustomerCart.class)))
    public Response getCustomerCart(@PathParam("customerId") Long customerId) {
        List<CustomerCart> customerCarts = customerCartService.getCustomerCarts(customerId);
        return Response.ok(customerCarts).build();
    }

    @DELETE
    @Path("/delete/{cartId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Удалить заказ", description = "Удаляет заказ по ID.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response deleteOrder(@PathParam("cartId") Long cartId) {
        customerCartService.deleteOrder(cartId);
        return Response.ok().build();
    }
}
