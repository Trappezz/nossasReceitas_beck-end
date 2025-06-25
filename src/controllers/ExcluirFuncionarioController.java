package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.FuncionarioService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ExcluirFuncionarioController implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1); // Método não permitido
            return;
        }

        try (InputStream is = exchange.getRequestBody()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("JSON recebido: " + body); // <-- Adiciona isso
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();

            if (!json.has("id_funcionario")) {
                sendResponse(exchange, 400, "{\"message\": \"Campo 'id_funcionario' é obrigatório.\"}");
                return;
            }

            int idFuncionario = json.get("id_funcionario").getAsInt();
            boolean sucesso = new FuncionarioService().deletarFuncionario(idFuncionario);

            if (sucesso) {
                sendResponse(exchange, 200, "{\"message\": \"Funcionário excluído com sucesso.\"}");
            } else {
                sendResponse(exchange, 404, "{\"message\": \"Funcionário não encontrado.\"}");
            }
        } catch (Exception e) {
            sendResponse(exchange, 400, "{\"message\": \"Erro ao processar requisição.\"}");
        }
    }
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}