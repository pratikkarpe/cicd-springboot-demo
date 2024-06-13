package com.pratik.cameldynamicroute.service;

import com.pratik.cameldynamicroute.configuration.ApiProperties;
import com.pratik.cameldynamicroute.model.ErrorResponse;
import com.pratik.cameldynamicroute.util.Utils;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.http.common.HttpMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ApiInvokeserviceTest {

    @Autowired
    CamelContext camelContext;

    @Spy
    @InjectMocks
    ApiInvokeService apiInvokeService;

    @Mock
    Utils  utils;

    @Mock
    ApiProperties apiProperties;

    @BeforeEach
    void setUp(){
        Mockito.when(apiProperties.getScheme()).thenReturn("http");
        Mockito.when(apiProperties.getHost()).thenReturn("test.com");
        Mockito.when(apiProperties.getPath()).thenReturn("/test");
        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("q","test");
        Mockito.when(utils.buildUrl(apiProperties.getScheme(),apiProperties.getHost(), apiProperties.getPath(), requestHeaders)).thenReturn("test.com");
        Map<String,Object> exchangeHeaders = new HashMap<>();
        exchangeHeaders.put("url","test.com");
        exchangeHeaders.put(Exchange.HTTP_METHOD, HttpMethods.GET);
        Exchange response = ExchangeBuilder.anExchange(camelContext).build();
        response.getIn().setBody("<h1>Hi</h1>");
        Mockito.when(utils.callRoute(exchangeHeaders,null,"direct:api")).thenReturn(response);
    }

    @Test
    void whencallAPI_returnsValidResponse() throws Exception {
        apiInvokeService.callAPI();
        Mockito.verify(apiInvokeService,Mockito.times(1)).callAPI();
    }

    @Test
    void whenCallAPI_returnsException(){
        Map<String,Object> exchangeHeaders = new HashMap<>();
        exchangeHeaders.put("url","test.com");
        exchangeHeaders.put(Exchange.HTTP_METHOD, HttpMethods.GET);
        Exchange response = ExchangeBuilder.anExchange(camelContext).build();
        response.getIn().setBody(new ErrorResponse("400","Bad request"));
        Mockito.when(utils.callRoute(exchangeHeaders,null,"direct:apiTest")).thenReturn(response);
        Assertions.assertThrows(Exception.class,() -> apiInvokeService.callAPI());
    }

}
