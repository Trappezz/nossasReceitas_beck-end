package routes;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import controllers.LoginController;

public class Router {
    public static void setupRoutes(HttpServer server) {
        server.createContext("/login", new LoginController("POST")); // POST



//  EXEMPLO:
//        server.createContext("/home/login", ); // POST
//        server.createContext("/teste1", );  // GET
//        server.createContext("/teste/", ); // PATCH com id na URL
    }
}

