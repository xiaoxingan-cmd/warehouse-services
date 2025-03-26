package com.xiaoxingan.resources;

import com.xiaoxingan.models.Company;
import io.quarkus.cache.CacheResult;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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

@Path("/cache/test/{number}")
@OpenAPIDefinition(
        info = @Info(title = "Test API", version = "1.0", description = "Сервис для тестирования работы кеша"),
        tags = { @Tag(name = "TestCache", description = "Методы для работы с кешем") }
)
public class TestCacheResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @CacheResult(cacheName = "testCache")
    @Operation(summary = "Получить кешированный ответ", description = "Первый вызов - 5сек, Второй - 2мс")
    @APIResponse(responseCode = "200",
            description = "Успешный ответ",
            content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response test(@PathParam("number") int i) throws InterruptedException {
        Thread.sleep(5000);

        int x = 1000 + i;

        return Response.ok(x).build();
    }
}
