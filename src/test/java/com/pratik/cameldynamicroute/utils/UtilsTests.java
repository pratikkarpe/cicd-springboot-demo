package com.pratik.cameldynamicroute.utils;

import com.pratik.cameldynamicroute.util.Utils;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.http.common.HttpMethods;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@CamelSpringBootTest
@EnableAutoConfiguration
public class UtilsTests {

    @Autowired
    CamelContext camelContext;

    @Autowired
    ProducerTemplate producerTemplate;

    @Spy
    @InjectMocks
    Utils utils;

    @EndpointInject("mock:test")
    MockEndpoint mockEndpoint;

    @Configuration
    static class TestConfig{

        @Bean
        RouteBuilder route(){
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:api2").to("mock:test").routeId("testId");
                }
            };
        }

        @Bean
        RouteBuilder getRoute(){
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:apiTest").to("mock:test").routeId("testId1");
                }
            };
        }
    }

    @Test
    void callRouteTest_withoutBody() throws InterruptedException {
        Utils u = new Utils(camelContext,producerTemplate);
        Map<String,Object> exchangeHeaders = new HashMap<>();
        exchangeHeaders.put("url","test.com");
        exchangeHeaders.put(Exchange.HTTP_METHOD, HttpMethods.GET);
        u.callRoute(exchangeHeaders,null,"direct:api2");
        mockEndpoint.message(0).header("url").isEqualTo("test.com");
        mockEndpoint.assertIsSatisfied();
    }

    @Test
    void callRouteTest_withBody() throws InterruptedException {
        Utils u = new Utils(camelContext,producerTemplate);
        Map<String,Object> exchangeHeaders = new HashMap<>();
        exchangeHeaders.put("url","test.com");
        exchangeHeaders.put(Exchange.HTTP_METHOD, HttpMethods.GET);
        u.callRoute(exchangeHeaders,"testBody","direct:apiTest");
        mockEndpoint.message(0).header("url").isEqualTo("test.com");
        mockEndpoint.message(0).body(String.class).isEqualTo("testBody");
        mockEndpoint.assertIsSatisfied();
    }

    @Test
    void buildUrlTest_withQueryParams(){
        Utils u = new Utils(camelContext,producerTemplate);
        Map<String,String> params = new HashMap<>();
        params.put("q","test");
        Assertions.assertEquals("http://test.com/test?q=test",u.buildUrl("http","test.com", "/test",params));
    }
    @Test
    void buildUrlTest_withoutQueryParams(){
        Utils u = new Utils(camelContext,producerTemplate);
        Assertions.assertEquals("http://test.com/test",u.buildUrl("http","test.com", "/test",null));
    }

}
