package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.EditarFuncionarioService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EditarFuncionarioController implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"PUT".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        try (InputStream is = exchange.getRequestBody()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();

            int id = json.get("id_funcionario").getAsInt();
            String nome = json.get("nome").getAsString();
            String data = json.get("data").getAsString();
            String rg = json.get("rg").getAsString();
            int idCargo = json.get("id_cargo").getAsInt();
            double salario = json.get("salario").getAsDouble();
            String nomeFantasia = json.get("nome_fantasia").getAsString();
            String email = json.get("email").getAsString();

            EditarFuncionarioService service = new EditarFuncionarioService();
            boolean atualizado = service.atualizarFuncionario(id, nome, data, rg, idCargo, salario, nomeFantasia, email);

            String resposta = atualizado
                    ? "{\"message\": \"Funcionário atualizado com sucesso\"}"
                    : "{\"message\": \"Erro ao atualizar funcionário\"}";
            int statusCode = atualizado ? 200 : 500;

            enviarResposta(exchange, statusCode, resposta);

        } catch (Exception e) {
            String erro = "{\"message\": \"Erro ao processar dados\"}";
            enviarResposta(exchange, 400, erro);
        }
    }

    private void enviarResposta(HttpExchange exchange, int status, String resposta) throws IOException {
        byte[] bytes = resposta.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}

