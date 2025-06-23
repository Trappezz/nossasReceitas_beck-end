package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.PerfilService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PerfilController implements HttpHandler {
    private final String method;

    public PerfilController(String method) {
        this.method = method;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final PerfilService perfilService = new PerfilService();

        if (!exchange.getRequestMethod().equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();

        String email = json.get("email").getAsString();
        String senha = json.get("senha").getAsString();

        String idCargo = perfilService.obterCargo(email,  senha);

        int status;
        String resposta;

        if (idCargo == null) {
            status = 401;
            resposta = "{\"mensagem\": \"Cargo não encontrado\"}";
        } else {
            status = 200;
            JsonObject respostaJson = new JsonObject();
            respostaJson.addProperty("id_cargo", idCargo);
            resposta = respostaJson.toString();
        }

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, resposta.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(resposta.getBytes());
        os.close();
    }
}
