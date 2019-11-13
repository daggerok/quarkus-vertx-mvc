package com.github.daggerok;

import io.vertx.core.Vertx;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Singleton
public class Freemarker {

    @Inject
    private Vertx vertx;

    private TemplateEngine engine;

    @PostConstruct
    public void setup() {
        engine = FreeMarkerTemplateEngine.create(vertx);
    }

    public Rendering view(String template) {
        return Rendering.of(engine).view(template);
    }

    public static class Rendering {

        private TemplateEngine engine;
        private String view;
        private Map<String, Object> attributes;

        private Rendering(TemplateEngine engine, String view, Map<String, Object> attributes) {
            this.engine = engine;
            this.view = view;
            this.attributes = attributes;
        }

        private static Rendering of(TemplateEngine engine) {
            Objects.requireNonNull(engine, "Template engine may not be null.");
            return new Rendering(engine, "index.ftl", Collections.emptyMap());
        }

        private Rendering view(String view) {
            Objects.requireNonNull(view, "Template view may not be null.");
            this.view = view;
            return this;
        }

        public CompletableFuture<Response> render(Map<String, Object> attributes) {
            Objects.requireNonNull(attributes, "Template attributes may not be null.");
            this.attributes = attributes;
            return render();
        }

        public CompletableFuture<Response> render() {
            CompletableFuture<Response> response = new CompletableFuture<>();
            engine.render(this.attributes, "templates/" + this.view, r -> {
                // response.completeExceptionally(new RuntimeException("oops"));
                if (r.failed()) response.complete(
                        Response.status(Status.BAD_REQUEST)
                                .entity(r.cause().getLocalizedMessage())
                                .type(MediaType.APPLICATION_JSON)
                                .build());
                else response.complete(
                        Response.ok()
                                .entity(r.result().toString(StandardCharsets.UTF_8.displayName()))
                                .type(MediaType.TEXT_HTML)
                                .build());
            });
            return response;
        }
    }
}
