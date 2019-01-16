package com.ichat4j.wrj.robot.ichat4j.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

@Service("httpService")
public class HttpService {

    @Value("${http.connection.manager.maxTotal:1000}")
    private int maxTotal = 1000;

    @Value("${http.connection.manager.defaultMaxPerRoute:300}")
    private int defaultMaxPerRoute = 300;

    @Value("${http.request.config.connectionRequestTimeout:5000}")
    private int connectionRequestTimeout = 5000;

    @Value("${http.request.config.connectTimeout:5000}")
    private int connectTimeout = 5000;

    @Value("${http.request.config.socketTimeout:5000}")
    private int socketTimeout = 5000;

    @Value("${http.request.config.soTimeout:5000}")
    private int soTimeout = 5000;

    private RestTemplate rest;

    public PoolingHttpClientConnectionManager getConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(this.maxTotal);
        connectionManager.setDefaultMaxPerRoute(this.defaultMaxPerRoute);
        SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setSoTimeout(soTimeout).build();
        connectionManager.setDefaultSocketConfig(socketConfig);
        return connectionManager;
    }

    public RequestConfig getRequestConfig() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(this.connectionRequestTimeout).setConnectTimeout(this.connectTimeout).setSocketTimeout(this.socketTimeout).build();
        return requestConfig;
    }

    public HttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(this.getConnectionManager()).setDefaultRequestConfig(getRequestConfig()).build();
        return httpClient;
    }

    @PostConstruct
    public void initial() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));

        this.rest = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(getHttpClient());
        this.rest.setRequestFactory(requestFactory);
        this.rest.getMessageConverters().add(new FormHttpMessageConverter());
        this.rest.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        rest.getMessageConverters().add(new MyConverter());
    }

    public RestTemplate getRestTemplate() {
        return this.rest;
    }

    public <T> ResponseEntity<T> exchangeForGet(String url, Map<String, String> headParams, Map<String, String> paramsMap, Class<T> clazz) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        HttpHeaders headers = new HttpHeaders();
        if (headParams != null && headParams.size() > 0) {
            for (Entry<String, String> entry : headParams.entrySet()) {
                headers.add(entry.getKey(), entry.getValue());
            }
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        if (paramsMap != null && paramsMap.size() > 0) {
            for (Entry<String, String> entry : paramsMap.entrySet()) {
                map.add(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        builder.queryParams(map);
        return this.rest.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, clazz);
    }

    public <T> ResponseEntity<T> exchangeForGet(String url, Map<String, String> headParams, Map<String, String> paramsMap, ParameterizedTypeReference<T> responseType) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        HttpHeaders headers = new HttpHeaders();
        if (headParams != null && headParams.size() > 0) {
            for (Entry<String, String> entry : headParams.entrySet()) {
                headers.add(entry.getKey(), entry.getValue());
            }
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        if (paramsMap != null && paramsMap.size() > 0) {
            for (Entry<String, String> entry : paramsMap.entrySet()) {
                map.add(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        builder.queryParams(map);
        return this.rest.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> exchangeForPost(String url, Map<String, String> headersMap, Object entity, Class<T> clazz) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        HttpHeaders headers = new HttpHeaders();
        if (headersMap != null && headersMap.size() > 0) {
            for (Entry<String, String> entry : headersMap.entrySet()) {
                headers.set(entry.getKey(), entry.getValue());
            }
        }

        HttpEntity<Object> requestEntity = new HttpEntity<Object>(entity, headers);
        return this.rest.exchange(builder.build().encode().toUri(), HttpMethod.POST, requestEntity, clazz);
    }

    public <T> ResponseEntity<T> exchangeForPost(String url, Map<String, String> headersMap, Object entity, ParameterizedTypeReference<T> responseType) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        HttpHeaders headers = new HttpHeaders();
        if (headersMap != null && headersMap.size() > 0) {
            for (Entry<String, String> entry : headersMap.entrySet()) {
                headers.set(entry.getKey(), entry.getValue());
            }
        }

        HttpEntity<Object> requestEntity = new HttpEntity<Object>(entity, headers);
        return this.rest.exchange(builder.build().encode().toUri(), HttpMethod.POST, requestEntity, responseType);
    }

}
