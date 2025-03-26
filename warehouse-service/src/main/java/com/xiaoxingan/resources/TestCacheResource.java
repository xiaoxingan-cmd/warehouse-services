package com.xiaoxingan.resources;

import io.quarkus.cache.CacheResult;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/cache/test/{number}")
public class TestCacheResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @CacheResult(cacheName = "testCache")
    public Response test(@PathParam("number") int i) throws InterruptedException {
        // TODO: второй вызов ожидаемое время выполнения 4мс.
        Thread.sleep(5000);

        int x = 1000 + i;

        return Response.ok(x).build();
    }
}
