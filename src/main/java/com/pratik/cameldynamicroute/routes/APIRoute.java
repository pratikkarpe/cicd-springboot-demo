package com.pratik.cameldynamicroute.routes;

import com.pratik.cameldynamicroute.processor.RouteExceptionProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class APIRoute extends RouteBuilder {

    @Autowired
    private RouteExceptionProcessor routeExceptionProcessor;

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .log("Exception occurred at APIRoute")
                .process(routeExceptionProcessor)
                .handled(Boolean.TRUE);

        from("direct:api")
                .log("Calling api ${header.url}")
                .toD("${header.url}");

    }
}
