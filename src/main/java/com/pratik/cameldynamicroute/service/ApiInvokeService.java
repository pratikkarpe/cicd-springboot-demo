package com.pratik.cameldynamicroute.service;

import com.pratik.cameldynamicroute.configuration.ApiProperties;
import com.pratik.cameldynamicroute.model.ErrorResponse;
import com.pratik.cameldynamicroute.util.Utils;
import org.apache.camel.Exchange;
import org.apache.camel.http.common.HttpMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApiInvokeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiInvokeService.class);

    @Autowired
    Utils utils;

    @Autowired
    ApiProperties apiProperties;

    public void callAPI() throws Exception {
        Map<String,String> requestHeaders = new HashMap<>();
        requestHeaders.put("q","test");
        String apiUrl = utils.buildUrl(apiProperties.getScheme(),apiProperties.getHost(), apiProperties.getPath(), requestHeaders);
        Map<String,Object> exchangeHeaders = new HashMap<>();
        exchangeHeaders.put("url",apiUrl);
        exchangeHeaders.put(Exchange.HTTP_METHOD, HttpMethods.GET);
        Exchange response = utils.callRoute(exchangeHeaders,null,"direct:api");
        validateResponse(response);
        String responseStr = response.getMessage().getBody(String.class);
        LOGGER.info("Response string is: {}",responseStr);
    }

    public void validateResponse(Exchange response) throws Exception {
        Object resp = response.getMessage().getBody();
        if(resp instanceof ErrorResponse){
            ErrorResponse errorResponse = (ErrorResponse) resp;
            LOGGER.error("Error status code {} error msg {} ",errorResponse.getCode(),errorResponse.getMessage());
            throw new Exception(errorResponse.getMessage());
        }
    }

}
