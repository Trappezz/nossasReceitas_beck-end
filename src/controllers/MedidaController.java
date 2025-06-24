package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.MedidaService;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MedidaController implements HttpHandler {
    private final String method;

    public MedidaController(String method) {
        this.method = method;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        MedidaService medidaService = new MedidaService();

        if (!exchange.getRequestMethod().equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String resposta;
        int status;

        if (method.equalsIgnoreCase("GET")) {
            List<String> medidas = medidaService.listarMedidas();
            resposta = medidaService.converterListaParaJson(medidas);
            status = 200;
        } else if (method.equalsIgnoreCase("POST")) {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();

            String nome = json.get("nome").getAsString();

            boolean sucesso = medidaService.inserirMedida(nome);
            if (sucesso) {
                resposta = "{\"mensagem\": \"Medida inserida com sucesso\"}";
                status = 201;
            } else {
                resposta = "{\"mensagem\": \"Erro ao inserir medida\"}";
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
