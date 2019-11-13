package com.github.daggerok;

import io.vavr.collection.HashMap;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.concurrent.CompletionStage;

@Path("")
public class IndexPage {

    @Inject
    private Thymeleaf thymeleaf;

    @GET
    @Path("/a/b/c")
    public CompletionStage<Response> abc() {
        return thymeleaf.view("index.html").render(
                Collections.singletonMap("message", "Hello, abc!"));
    }

    @GET
    @Path("{path: ^.*$}")
    public CompletionStage<Response> path(@PathParam("path") String path) {
        return thymeleaf.view("index.html").render(
                HashMap.<String, Object>of("message", "Ola!",
                                           "path", path)
                        .toJavaMap());
    }
}
