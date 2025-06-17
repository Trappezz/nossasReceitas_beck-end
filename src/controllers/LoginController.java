package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.LoginService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class LoginController implements HttpHandler {
    private final String method;
    public LoginController(String method) {
        this.method = method;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final LoginService loginService = new LoginService();

        if (!exchange.getRequestMethod().equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        String email = json.get("email").getAsString();
        String senha = json.get("senha").getAsString();

        boolean loginValido = loginService.autenticar(email, senha); // Agora usa o banco

        String resposta = loginValido
                ? "{\"mensagem\": \"Login bem-sucedido\"}"
                : "{\"mensagem\": \"E-mail ou senha incorretos\"}";
        int status = loginValido ? 200 : 401;

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, resposta.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(resposta.getBytes());
        os.close();
    }
}