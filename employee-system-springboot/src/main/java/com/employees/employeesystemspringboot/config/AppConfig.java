package com.employees.employeesystemspringboot.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class AppConfig {
    private HttpClient client;
    private Gson gson;

    @Bean
    public HttpClient httpClient() {
        if (client == null) client = HttpClient.newHttpClient();
        return client;
    }

    @Bean
    public Gson gson() {
        if (gson == null) gson = new Gson();
        return gson;
    }
}