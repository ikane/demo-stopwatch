package com.example.demostopwatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ClientHttpResponse response = execution.execute(request, body);
        stopWatch.stop();


        //long length = response.getHeaders().getContentLength();
        byte[] responseBody = FileCopyUtils.copyToByteArray(response.getBody());

        //Add optional additional headers
        response.getHeaders().add("stopwatch", String.valueOf(stopWatch.getTotalTimeMillis()));
        response.getHeaders().add("memory", String.valueOf(responseBody.length));
        response.getHeaders().add("status code", String.valueOf(response.getRawStatusCode()));

        logResponse(response);
        //log.info(stopWatch.prettyPrint());

        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) throws IOException
    {

            log.info("===========================request begin================================================");
            log.info("URI         : {}", request.getURI());
            log.info("Method      : {}", request.getMethod());
            log.info("Headers     : {}", request.getHeaders());
            log.info("Request body: {}", new String(body, "UTF-8"));
            log.info("==========================request end================================================");

    }

    private void logResponse(ClientHttpResponse response) throws IOException
    {

            log.info("============================response begin==========================================");
            log.info("Status code  : {}", response.getStatusCode());
            log.info("Status text  : {}", response.getStatusText());
            log.info("Headers      : {}", response.getHeaders());
            log.info("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
            log.info("stopwatch    : {}", response.getHeaders().getValuesAsList("stopwatch").get(0));
            log.info("memory       : {}", response.getHeaders().getValuesAsList("memory").get(0));
            log.info("=======================response end=================================================");


    }

}
