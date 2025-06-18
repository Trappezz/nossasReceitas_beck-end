package routes;
import com.sun.net.httpserver.HttpServer;
import controllers.LoginController;
import filters.CorsFilters;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;

public class Router {
    public static void setupRoutes(HttpServer server) {
        var login = server.createContext("/login", new LoginController("POST")); // POST
        login.getFilters().add(new CorsFilters());



//  EXEMPLO:
//        server.createContext("/home/login", ); // POST
//        server.createContext("/teste1", );  // GET
//        server.createContext("/teste/", ); // PATCH com id na URL
    }
}

