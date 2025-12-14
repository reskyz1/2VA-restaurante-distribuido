package org.sistema.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sistema.dto.FuncionarioDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FuncionarioClient {

    private static final String BASE_URL = "http://localhost:8083/funcionarios";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FuncionarioClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public FuncionarioDTO buscarPorId(Long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String body = response.body();
            return objectMapper.readValue(body, FuncionarioDTO.class);
        } else if (response.statusCode() == 404) {
            return null;
        } else {
            throw new IOException("Erro ao consultar funcionário. HTTP "
                    + response.statusCode() + " - " + response.body());
        }
    }
}
