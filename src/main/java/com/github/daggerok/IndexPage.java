package com.github.daggerok;

import java.util.Collections;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
}
