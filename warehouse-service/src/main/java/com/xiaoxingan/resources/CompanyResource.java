package com.xiaoxingan.resources;

import com.xiaoxingan.dto.CompanyDTO;
import com.xiaoxingan.models.Company;
import com.xiaoxingan.services.CompanyService;
import com.xiaoxingan.utils.ModelMappers;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Path("/companies")
@OpenAPIDefinition(
        info = @Info(title = "Company API", version = "1.0", description = "Сервис для управления компаниями"),
        tags = { @Tag(name = "Company", description = "Методы для работы с компаниями") }
)
public class CompanyResource {
    @Inject
    CompanyService companyService;
    @Inject
    ModelMappers modelMappers;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Получить все компании", description = "Возвращает список всех зарегистрированных компаний.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Company.class)))
    public Response getCompanies() {
        List<Company> companies = companyService.findAllCompanies();
        return Response.ok(companies).build();
    }

    @GET
    @Path("/{companyName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Получить компанию по названию", description = "Возвращает компанию полученную по имени.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = Company.class)))
    public Response getCompany(@PathParam("companyName") String companyName) {
        Company company = companyService.findCompanyByName(companyName);
        if (company == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(company).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Добавить новую компанию", description = "Добавляет новую компанию из DTO объекта.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response addCompany(@Valid CompanyDTO company) {
        Validate<CompanyDTO> validate = new Validate<>();
        if (!validate.validateData(company)) {
            return validate.getBadResponse();
        }

        Company newCompany = modelMappers.mapToCompany(company);
        newCompany.setCreatedAt(LocalDateTime.now());
        newCompany.setUpdatedAt(LocalDateTime.now());

        try {
            companyService.addCompany(newCompany);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("Ошибка", e.getMessage()))
                    .build();
        }

        return Response.ok().build();
    }

    @POST
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Обновить компанию", description = "Обновляет существующую компанию из DTO объекта.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response updateCompany(@PathParam("id") Long id, @Valid CompanyDTO company) {
        Validate<CompanyDTO> validate = new Validate<>();
        if (!validate.validateData(company)) {
            return validate.getBadResponse();
        }

        Company newCompany = modelMappers.mapToCompany(company);
        newCompany.setUpdatedAt(LocalDateTime.now());

        try {
            companyService.updateCompany(id, newCompany);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("Ошибка", e.getMessage()))
                    .build();
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Удалить компанию", description = "Удаляет существующую компанию по ID объекта.")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response deleteCompany(@PathParam("id") Long id) {
        companyService.deleteCompany(id);

        return Response.ok().build();
    }
}
