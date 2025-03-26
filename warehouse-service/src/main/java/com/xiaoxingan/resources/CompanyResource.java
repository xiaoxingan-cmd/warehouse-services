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

import java.time.LocalDateTime;
import java.util.List;

@Path("/companies")
public class CompanyResource {
    @Inject
    CompanyService companyService;
    @Inject
    ModelMappers modelMappers;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompanies() {
        List<Company> companies = companyService.findAllCompanies();
        return Response.ok(companies).build();
    }

    @GET
    @Path("/{companyName}")
    @Produces(MediaType.APPLICATION_JSON)
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
            return Response.status(Response.Status.valueOf(e.getMessage())).build();
        }

        return Response.ok().build();
    }

    @POST
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
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
            return Response.status(Response.Status.valueOf(e.getMessage())).build();
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCompany(@PathParam("id") Long id) {
        companyService.deleteCompany(id);

        return Response.ok().build();
    }
}
