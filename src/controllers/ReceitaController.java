package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.ReceitaService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ReceitaController implements HttpHandler {
    private final String method;

    public ReceitaController(String method) {
        this.method = method;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        String nomeReceita = json.get("nome_receita").getAsString();
        String modoPreparo = json.get("modo_preparo").getAsString();
        int idCozinheiro = json.get("id_cozinheiro").getAsInt();
        int idCategoria = json.get("id_categoria").getAsInt();

        ReceitaService service = new ReceitaService();
        boolean sucesso = service.cadastrarReceita(nomeReceita, modoPreparo, idCozinheiro, idCategoria);

        String resposta;
        int status;

        if (sucesso) {
            status = 201;
            resposta = "{\"mensagem\": \"Receita cadastrada com sucesso!\"}";
        } else {
            status = 409;
            resposta = "{\"mensagem\": \"Receita já existe ou erro ao cadastrar.\"}";
        }

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, resposta.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(resposta.getBytes());
        os.close();
    }
}
