package org.sistema.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sistema.dto.PedidoDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class PedidosClient {

    // Se sua porta ou caminho forem diferentes, ajusta aqui:
    private static final String BASE_URL = "http://localhost:8081/pedidos";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public PedidosClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<PedidoDTO> listarPedidos() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            String body = response.body();
            return objectMapper.readValue(body,
                    new TypeReference<List<PedidoDTO>>() {});
        } else {
            throw new IOException("Erro ao listar pedidos. HTTP " + response.statusCode());
        }
    }

    public PedidoDTO criarPedido(PedidoDTO pedido)
            throws IOException, InterruptedException {

        String json = objectMapper.writeValueAsString(pedido);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            String body = response.body();
            return objectMapper.readValue(body, PedidoDTO.class);
        } else {
            throw new IOException("Erro ao criar pedido. HTTP "
                    + response.statusCode() + " - " + response.body());
        }
    }
}
