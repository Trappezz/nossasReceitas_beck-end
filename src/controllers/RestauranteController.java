package controllers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.RestauranteService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class RestauranteController implements HttpHandler {
    private final String method;

    public RestauranteController(String method) {
        this.method = method;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(405, -1); // Método não permitido
            return;
        }

        try {
            List<Map<String, Object>> restaurantes = new RestauranteService().listarRestaurantes();
            String respostaJson = new Gson().toJson(restaurantes);

            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, respostaJson.getBytes(StandardCharsets.UTF_8).length);

            OutputStream os = exchange.getResponseBody();
            os.write(respostaJson.getBytes(StandardCharsets.UTF_8));
            os.close();
        } catch (Exception e) {
            String erro = "{\"erro\": \"Erro interno ao buscar restaurantes.\"}";
            exchange.sendResponseHeaders(500, erro.length());
            exchange.getResponseBody().write(erro.getBytes(StandardCharsets.UTF_8));
            exchange.getResponseBody().close();
        }
    }
}
