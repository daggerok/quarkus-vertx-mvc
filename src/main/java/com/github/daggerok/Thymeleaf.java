package com.github.daggerok;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.vertx.core.Vertx;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;

@Singleton
// @ApplicationScoped
public class Thymeleaf {

    @Inject
    private Vertx vertx;

    private TemplateEngine engine;

    @PostConstruct
    public void setupThymeleaf() {
        engine = ThymeleafTemplateEngine.create(vertx);
    }

    public ViewBuilder view(String template) {
        return ViewBuilder.of(engine).view(template);
    }

    public static class ViewBuilder {

        private TemplateEngine engine;
        private String view;
        private Map<String, Object> attributes;

        private ViewBuilder(TemplateEngine engine, String view, Map<String, Object> attributes) {
            this.engine = engine;
            this.view = view;
            this.attributes = attributes;
        }
        public static ViewBuilder of(TemplateEngine engine) {
            return new ViewBuilder(engine, "index.html", Collections.emptyMap());
        }
        public ViewBuilder view(String view) {
            this.view = view;
            return this;
        }
        public ViewBuilder attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }
        public CompletableFuture<Response> render(Map<String, Object> attributes) {
            return this.attributes(attributes).render();
        }
        public CompletableFuture<Response> render() {
            CompletableFuture<Response> response = new CompletableFuture<>();
            engine.render(this.attributes, "templates/" + this.view, r -> {
                // response.completeExceptionally(new RuntimeException("oops"));
                if (r.failed()) response.complete(
                    Response.status(Status.BAD_REQUEST)
                            .entity(r.result().toString(StandardCharsets.UTF_8.displayName()))
                            .type(MediaType.APPLICATION_JSON).build());
                else response.complete(
                    Response.ok().entity(r.result().toString(StandardCharsets.UTF_8.displayName()))
                            .type(MediaType.TEXT_HTML).build());
            });
            return response;
        }
    }
}