package com.example.demostopwatch;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class CustomRestTemplateConfig {

    // Determines the timeout in milliseconds until a connection is established.
    private static final int CONNECT_TIMEOUT = 1000;

    // The timeout when requesting a connection from the connection manager.
    private static final int REQUEST_TIMEOUT = 100;

    // The timeout for waiting for data
    private static final int SOCKET_TIMEOUT = 1000;

    private static final int MAX_TOTAL_CONNECTIONS = 50;
    private static final int DEFAULT_KEEP_ALIVE_TIME_MILLIS = 20 * 1000;
    private static final int CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS = 30;

    @Bean
    public RestTemplate restTemplate()
    {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(clientHttpRequestFactory()));
        restTemplate.setMessageConverters(Collections.singletonList(mappingJacksonHttpMessageConverter()));

        restTemplate.setInterceptors( Collections.singletonList(new RequestResponseLoggingInterceptor()) );

        restTemplate.setRequestFactory(clientHttpRequestFactory());

        return restTemplate;
    }

    private HttpMessageConverter<?> mappingJacksonHttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean
    public CloseableHttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT).build();

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                //.setConnectionManager(poolingConnectionManager())
                //.setKeepAliveStrategy(connectionKeepAliveStrategy())
                .build();
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        clientHttpRequestFactory.setHttpClient(httpClient());
        clientHttpRequestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        clientHttpRequestFactory.setReadTimeout(REQUEST_TIMEOUT);
        clientHttpRequestFactory.setConnectionRequestTimeout(SOCKET_TIMEOUT);

        return new BufferingClientHttpRequestFactory(clientHttpRequestFactory);

        //return clientHttpRequestFactory;
    }



}
