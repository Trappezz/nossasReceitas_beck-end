package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.FuncionarioService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class FuncionarioController implements HttpHandler {
    private final String method;

    public FuncionarioController(String method) {
        this.method = method;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        FuncionarioService service = new FuncionarioService();

        if (!exchange.getRequestMethod().equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        exchange.getResponseHeaders().add("Content-Type", "application/json");

        if ("GET".equalsIgnoreCase(method)) {
            List<Map<String, Object>> lista = service.listarFuncionarios();
            String json = new Gson().toJson(lista);
            sendResponse(exchange, 200, json);

        } else if ("POST".equalsIgnoreCase(method)) {
            try (InputStream is = exchange.getRequestBody()) {
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                JsonObject json = JsonParser.parseString(body).getAsJsonObject();

                if (!json.has("nome") || !json.has("data_ingresso") || !json.has("salario")
                        || !json.has("id_cargo") || !json.has("nome_fantasia")) {
                    sendResponse(exchange, 400, "{\"message\": \"Campos obrigatórios faltando.\"}");
                    return;
                }

                String nome = json.get("nome").getAsString();
                String data = json.get("data_ingresso").getAsString();
                double salario = json.get("salario").getAsDouble();
                int cargo = json.get("id_cargo").getAsInt();
                String restaurante = json.get("nome_fantasia").getAsString();

                boolean sucesso = service.adicionarFuncionario(nome, data, salario, cargo, restaurante);
                if (sucesso) {
                    sendResponse(exchange, 201, "{\"message\": \"Funcionário cadastrado com sucesso\"}");
                } else {
                    sendResponse(exchange, 500, "{\"message\": \"Erro ao salvar funcionário\"}");
                }

            } catch (Exception e) {
                sendResponse(exchange, 400, "{\"message\": \"Dados inválidos\"}");
            }
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
