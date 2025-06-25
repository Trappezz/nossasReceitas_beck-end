package controllers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.CozinheiroService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class CozinheiroController implements HttpHandler {
    private final String method;

    public CozinheiroController(String method) {
        this.method = method;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(405, -1); // Método não permitido
            return;
        }

        try {
            List<Map<String, Object>> lista = new CozinheiroService().listarProducaoMensal();
            String json = new Gson().toJson(lista);

            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, json.getBytes(StandardCharsets.UTF_8).length);

            OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.close();
        } catch (Exception e) {
            String erro = "{\"erro\":\"Erro ao carregar produção mensal.\"}";
            exchange.sendResponseHeaders(500, erro.length());
            exchange.getResponseBody().write(erro.getBytes(StandardCharsets.UTF_8));
            exchange.getResponseBody().close();
        }
    }
}
