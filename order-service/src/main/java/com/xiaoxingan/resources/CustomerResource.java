package com.xiaoxingan.resources;

import com.xiaoxingan.dto.CustomerDTO;
import com.xiaoxingan.exceptions.customers.CustomerUpdateFailureException;
import com.xiaoxingan.models.Customer;
import com.xiaoxingan.models.CustomerCart;
import com.xiaoxingan.services.CustomerService;
import com.xiaoxingan.utils.validator.Validate;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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

@Path("/customers")
@OpenAPIDefinition(
        info = @Info(title = "Customer API", version = "1.0", description = "Сервис для работы с покупателем"),
        tags = { @Tag(name = "Customer", description = "Методы для работы с покупателем") }
)
public class CustomerResource {
    @Inject
    CustomerService customerService;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Получить всех пользователей", description = "Возвращает всех пользователей.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Customer.class)))
    public Response getCustomers() {
        List<Customer> customers = customerService.findAllCustomers();
        return Response.ok(customers).build();
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Зарегистрировать нового пользователя", description = "Регистрирует пользователя.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response registerCustomer(@Valid CustomerDTO customerDTO) {
        Validate<CustomerDTO> validate = new Validate<>();
        if (!validate.validateData(customerDTO)) {
            return validate.getBadResponse();
        }

        try {
            customerService.registerCustomer(customerDTO);
        } catch (CustomerUpdateFailureException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("Ошибка ", e.getMessage()))
                    .build();
        }
        return Response.ok().build();
    }
}
