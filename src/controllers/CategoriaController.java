package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.CategoriaService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CategoriaController implements HttpHandler {
    private final String method;

    public CategoriaController(String method) {
        this.method = method;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try (InputStream is = exchange.getRequestBody()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();

            if (!json.has("descricao") || json.get("descricao").getAsString().trim().isEmpty()) {
                String erro = "{\"message\": \"Descrição é obrigatória.\"}";
                sendResponse(exchange, 400, erro);
                return;
            }

            String descricao = json.get("descricao").getAsString().trim();
            boolean sucesso = new CategoriaService().inserirCategoria(descricao);

            if (sucesso) {
                String resposta = "{\"message\": \"Categoria salva com sucesso!\"}";
                sendResponse(exchange, 201, resposta);
            } else {
                String erro = "{\"message\": \"Erro ao salvar a categoria.\"}";
                sendResponse(exchange, 500, erro);
            }

        } catch (Exception e) {
            String erro = "{\"message\": \"Erro interno no servidor.\"}";
            sendResponse(exchange, 500, erro);
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
