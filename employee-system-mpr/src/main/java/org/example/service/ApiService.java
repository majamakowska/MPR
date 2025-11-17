package org.example.service;

import com.google.gson.*;
import org.example.exception.ApiException;
import org.example.model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.*;

public class ApiService {

    public List<Employee> fetchEmployeesFromApi() throws IOException, InterruptedException {
        List<Employee> employees = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
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
                String companyName = post.get("companyName").getAsString();

                employees.add(new Employee(firstName, lastName, email, companyName, Position.PROGRAMISTA));
            }
        } else {
            throw new ApiException("Błąd HTTP: " + response.statusCode());
        }

        return employees;
    }
}