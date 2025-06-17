package api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import services.LoginService;
//
//import java.io.*;
//import java.net.InetSocketAddress;
//import java.nio.charset.StandardCharsets;
//
//public class Routes {
//
//    public static void main(String[] args) throws IOException {
//        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
//        server.createContext("/login", new LoginHandler());
//        server.setExecutor(null);
//        System.out.println("Servidor iniciado em http://localhost:8080");
//        server.start();
//    }
//
//    static class LoginHandler implements HttpHandler {
//        private final LoginService loginService = new LoginService();
//
//        @Override
//        public void handle(HttpExchange exchange) throws IOException {
//            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
//                exchange.sendResponseHeaders(405, -1); // Método não permitido
//                return;
//            }
//
//            InputStream is = exchange.getRequestBody();
//            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
//
//            String email = extrairValor(body, "email");
//            String senha = extrairValor(body, "senha");
//
//            boolean loginValido = loginService.autenticar(email, senha); // Agora usa o banco
//
//            String resposta = loginValido
//                    ? "{\"mensagem\": \"Login bem-sucedido\"}"
//                    : "{\"mensagem\": \"E-mail ou senha incorretos\"}";
//            int status = loginValido ? 200 : 401;
//
//            exchange.getResponseHeaders().add("Content-Type", "application/json");
//            exchange.sendResponseHeaders(status, resposta.getBytes().length);
//            OutputStream os = exchange.getResponseBody();
//            os.write(resposta.getBytes());
//            os.close();
//        }
//
//        private String extrairValor(String json, String chave) {
//            String procurado = "\"" + chave + "\"";
//            int start = json.indexOf(procurado);
//            if (start == -1) return "";
//            int doisPontos = json.indexOf(":", start);
//            int aspasInicio = json.indexOf("\"", doisPontos + 1);
//            int aspasFim = json.indexOf("\"", aspasInicio + 1);
//            return json.substring(aspasInicio + 1, aspasFim);
//        }
//    }
//}
