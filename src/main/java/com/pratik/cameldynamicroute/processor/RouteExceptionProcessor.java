package com.pratik.cameldynamicroute.processor;


import com.pratik.cameldynamicroute.model.ErrorResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class RouteExceptionProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        Throwable caused = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,Throwable.class);
        if(caused instanceof HttpOperationFailedException) {
            final HttpOperationFailedException httpOperationFailedException = (HttpOperationFailedException) caused;

            exchange.getIn().setBody(new ErrorResponse(String.valueOf(httpOperationFailedException.getStatusCode()), "Error while calling api " + httpOperationFailedException.getMessage() + " " + httpOperationFailedException.getResponseBody()));
        }else
            exchange.getIn().setBody(new ErrorResponse("500","Error while calling api "+caused.getMessage()));
        }


}
