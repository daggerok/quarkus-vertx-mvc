package com.github.daggerok;

import io.vertx.ext.web.common.template.TemplateEngine;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Rendering extends CompletableFuture<Response> {

    private View view;

    private Rendering(View view) {
        this.view = view;
    }

    private Rendering render() {
        view.engine.render(view.attributes, "templates/" + view.template, r -> {
            // response.completeExceptionally(new RuntimeException("oops"));
            if (r.failed()) complete(Response.status(Response.Status.BAD_REQUEST)
                                             .entity(r.result().toString(StandardCharsets.UTF_8.displayName()))
                                             .type(MediaType.APPLICATION_JSON)
                                             .build());
            else complete(Response.ok()
                                  .entity(r.result().toString(StandardCharsets.UTF_8.displayName()))
                                  .type(MediaType.TEXT_HTML)
                                  .build());
        });
        return this;
    }

    public static View of(TemplateEngine engine) {
        return new View(engine, "index.html", Collections.emptyMap());
    }

    public static class View {

        private TemplateEngine engine;
        private String template;
        private Map<String, Object> attributes;

        private View(TemplateEngine engine, String template, Map<String, Object> attributes) {
            this.engine = engine;
            this.template = template;
            this.attributes = attributes;
        }

        public View view(String view) {
            this.template = view;
            return this;
        }

        public View attributes(Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }

        public CompletableFuture<Response> build() {
            return new Rendering(this).render();
        }
    }
}
