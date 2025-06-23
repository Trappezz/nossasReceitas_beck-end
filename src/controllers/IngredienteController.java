package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.IngredienteService;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class IngredienteController implements HttpHandler {
    private final String method;

    public IngredienteController(String method) {
        this.method = method;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        IngredienteService ingredienteService = new IngredienteService();

        if (!exchange.getRequestMethod().equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String resposta;
        int status;

        if (method.equalsIgnoreCase("GET")) {
            List<String> ingredientes = ingredienteService.listarIngredientes();
            resposta = ingredienteService.converterListaParaJson(ingredientes);
            status = 200;

        } else if (method.equalsIgnoreCase("POST")) {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();

            String nome = json.get("nome").getAsString();

            boolean sucesso = ingredienteService.inserirIngrediente(nome);
            if (sucesso) {
                resposta = "{\"mensagem\": \"Ingrediente inserido com sucesso\"}";
                status = 201;
            } else {
                resposta = "{\"mensagem\": \"Erro ao inserir ingrediente\"}";
                status = 500;
            }

        } else {
            resposta = "{\"mensagem\": \"Método não suportado\"}";
            status = 405;
        }

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, resposta.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(resposta.getBytes());
        os.close();
    }
}
