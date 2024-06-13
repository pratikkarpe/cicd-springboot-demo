package com.pratik.cameldynamicroute.util;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    @Autowired
    CamelContext camelContext;

    @Autowired
    ProducerTemplate producerTemplate;

    public Utils(CamelContext camelContext, ProducerTemplate producerTemplate) {
        this.camelContext = camelContext;
        this.producerTemplate = producerTemplate;
    }

    public String buildUrl(String scheme, String host, String path, Map<String,String> queryParams){
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(scheme).setHost(host).setPath(path);
        if(queryParams != null){
            for(Map.Entry<String,String> entry:queryParams.entrySet()){
                uriBuilder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        return uriBuilder.toString();
    }

    public Exchange callRoute(Map<String,Object> exchangeHeaders,Object body,String route){
        Exchange request = ExchangeBuilder.anExchange(camelContext).build();
        exchangeHeaders.forEach((key,value) -> request.getIn().setHeader(key,value));
        if(body != null)
            request.getIn().setBody(body);
        return producerTemplate.send(route,request);
    }


}
