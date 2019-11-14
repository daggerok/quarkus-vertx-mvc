# quarkus vertx mvc [![Build Status](https://travis-ci.org/daggerok/quarkus-vertx-mvc.svg?branch=master)](https://travis-ci.org/daggerok/quarkus-vertx-mvc)
Quarkus + Vertx + Thymeleaf, Freemarker, etc (native builds doesn't work because of template engine)

## dev

```bash
./mvnw compile quarkus:dev
http :8080
```

## jvm

```bash
./mvnw compile package
java -jar target/*-runner.jar
http :8080
```

## native

NOTE: In general with supported library this approach should works fine. But
now unfortunately, Thymeleaf / Freemarker apps are not supported for native builds...

```bash
docker build -f src/main/docker/Dockerfile.multistage -t daggerok/native-build .
docker run -i --rm --name native-app -p 8080:8080 native-build
http :8080
```

## links

* https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#strings
* https://docs.jboss.org/resteasy/docs/3.6.0.Final/userguide/html/Asynchronous_HTTP_Request_Processing.html
