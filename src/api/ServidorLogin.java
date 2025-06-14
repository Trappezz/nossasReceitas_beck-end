package api;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServidorLogin {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // HTML na raiz
        server.createContext("/", new HtmlHandler());

        // API de login
        server.createContext("/login", new LoginHandler());

        server.setExecutor(null);
        System.out.println("Servidor iniciado em http://localhost:8080");
        server.start();
    }

    static class HtmlHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            String caminho = "src/web/login/login.html"; // Caminho relativo ao projeto
            File file = new File(caminho);

            if (!file.exists()) {
                String erro = "Arquivo não encontrado: " + caminho;
                exchange.sendResponseHeaders(404, erro.length());
                OutputStream os = exchange.getResponseBody();
                os.write(erro.getBytes());
                os.close();
                return;
            }

            byte[] response = Files.readAllBytes(Paths.get(caminho));
            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        }
    }

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            String email = extrairValor(body, "email");
            String senha = extrairValor(body, "senha");

            boolean loginValido = "admin@empresa.com".equals(email) && "1234".equals(senha);

            String resposta = loginValido
                    ? "{\"mensagem\": \"Login bem-sucedido\"}"
                    : "{\"mensagem\": \"Credenciais inválidas\"}";
            int status = loginValido ? 200 : 401;

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(status, resposta.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(resposta.getBytes());
            os.close();
        }

        private String extrairValor(String json, String chave) {
            String procurado = "\"" + chave + "\"";
            int start = json.indexOf(procurado);
            if (start == -1) return "";
            int doisPontos = json.indexOf(":", start);
            int aspasInicio = json.indexOf("\"", doisPontos + 1);
            int aspasFim = json.indexOf("\"", aspasInicio + 1);
            return json.substring(aspasInicio + 1, aspasFim);
        }
    }
}
