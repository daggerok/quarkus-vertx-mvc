package com.github.daggerok;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("")
public class IndexPage {

    @Inject
    private Thymeleaf thymeleaf;

    @GET
    public CompletionStage<Response> greeting() {
        return thymeleaf.view("index.html").render(
            Collections.singletonMap("message", "Hello!"));
    }

    @GET
    @Path("/a/b/c")
    public CompletionStage<Response> abc() {
        return thymeleaf.view("index.html").render(
            Collections.singletonMap("message", "Hello, abc!"));
    }

    @GET
    @Path("{path}") // @Path("{path: .*}")
    public CompletionStage<Response> path(@PathParam("path") String path) {
        return thymeleaf.view("index.html").render(
            Collections.singletonMap("message", String.format("Hello '%s' path!", path)));
    }
}
