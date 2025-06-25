package controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.ReceitaService;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReceitaController implements HttpHandler {
    private final String method;

    public ReceitaController(String method) {
        this.method = method;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ReceitaService receitaService = new ReceitaService();

        if (!exchange.getRequestMethod().equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String resposta;
        int status;

        if (method.equalsIgnoreCase("GET")) {
            List<JsonObject> receitas = receitaService.listarReceitas();
            resposta = receitas.toString(); // Lista de objetos JSON convertida para string
            status = 200;

        } else if (method.equalsIgnoreCase("POST")) {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();


            String nome = json.get("nome").getAsString();
            String modoPreparo = json.get("modo_preparo").getAsString();
            int idCozinheiro = json.has("id_cozinheiro") ? json.get("id_cozinheiro").getAsInt() : 1; // Fixo ou vindo do token
            int idCategoria = json.has("categoria") ? json.get("categoria").getAsInt() : json.get("id_categoria").getAsInt();
            JsonArray ingredientes = json.getAsJsonArray("ingredientes");

            boolean sucesso = receitaService.inserirReceitaCompleta(nome, modoPreparo, idCozinheiro, idCategoria, ingredientes);

            if (sucesso) {
                resposta = "{\"mensagem\": \"Receita inserida com sucesso\"}";
                status = 201;
            } else {
                resposta = "{\"mensagem\": \"Erro ao inserir receita (nome pode estar duplicado)\"}";
                status = 400;
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
