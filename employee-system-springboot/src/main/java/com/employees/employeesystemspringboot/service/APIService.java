package com.employees.employeesystemspringboot.service;

import com.employees.employeesystemspringboot.config.AppConfig;
import com.employees.employeesystemspringboot.exception.ApiException;
import com.employees.employeesystemspringboot.model.Employee;
import com.employees.employeesystemspringboot.model.ImportSummary;
import com.employees.employeesystemspringboot.model.Position;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.*;

@Service
public class APIService {
    private final String apiUrl;
    private final AppConfig appConfig;

    public APIService(@Value("${app.api.url}") String apiUrl, AppConfig appConfig) {
        this.apiUrl = apiUrl;
        this.appConfig = appConfig;
    }

    public ImportSummary fetchEmployeesFromApi(EmployeeService employeeService) throws IOException, InterruptedException {
        int imported = 0;

        List<Employee> employees = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        HttpClient client = appConfig.httpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Gson gson = appConfig.gson();
            JsonArray posts = gson.fromJson(response.body(), JsonArray.class);
            for (JsonElement elem : posts) {
                JsonObject post = elem.getAsJsonObject();

                String name = post.get("name").getAsString();
                String[] names = name.split(" ");
                if (names.length <= 1) {
                    throw new ApiException("Nieprawidłowy format");
                }
                String firstName = names[0];
                String lastName = names[1];
                String email = post.get("email").getAsString();
                String companyName = post.get("company").getAsJsonObject().get("name").getAsString();

                try {
                    employees.add(new Employee(firstName, lastName, email, companyName, Position.PROGRAMISTA));
                } catch (Exception e) {
                    errors.add(e.getMessage());
                }
            }
        } else {
            throw new ApiException("Błąd HTTP: " + response.statusCode());
        }

        try {
            for (Employee employee : employees) {
                employeeService.addEmployee(employee);
                imported++;
            }
        } catch (Exception e) {
            errors.add(e.getMessage());
        }

        return new ImportSummary(imported, errors);
    }
}