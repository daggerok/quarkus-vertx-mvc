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
    private Freemarker freemarker;

    @GET
    public CompletionStage<Response> greeting() {
        return freemarker.view("index.ftl").render(
            Collections.singletonMap("message", "Hello!"));
    }

    @GET
    @Path("/a/b/c")
    public CompletionStage<Response> abc() {
        return freemarker.view("index.ftl").render(
            Collections.singletonMap("message", "Hello, abc!"));
    }

    @GET
    @Path("{path}")
    public CompletableFuture<Response> path(@PathParam("path") String path) {
        return freemarker.view("index.ftl").render(
            Collections.singletonMap("message", String.format("Hello '%s' path!", path)));
    }
}
