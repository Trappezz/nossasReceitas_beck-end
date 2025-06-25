package routes;
import com.sun.net.httpserver.HttpServer;
import controllers.*;
import filters.CorsFilters;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;

public class Router {
    public static void setupRoutes(HttpServer server) {
        var login = server.createContext("/login", new LoginController("POST")); // POST
        login.getFilters().add(new CorsFilters());

        var perfil = server.createContext("/perfil", new PerfilController("POST"));
        perfil.getFilters().add(new CorsFilters());

        var ingredienteGet = server.createContext("/ingredientes/consultar", new IngredienteController("GET"));
        ingredienteGet.getFilters().add(new CorsFilters());

        var ingredientePost = server.createContext("/ingredientes/inserir", new IngredienteController("POST"));
        ingredientePost.getFilters().add(new CorsFilters());

        var receitasGet = server.createContext("/receitas/consultar", new ReceitaController("GET"));
        receitasGet.getFilters().add(new CorsFilters());

        var receitasPost = server.createContext("/receitas/inserir", new ReceitaController("POST"));
        receitasPost.getFilters().add(new CorsFilters());

        var medidaGet = server.createContext("/medida/consultar", new MedidaController("GET"));
        medidaGet.getFilters().add(new CorsFilters());

        var medidaPost = server.createContext("/medida/inserir", new MedidaController("POST"));
        medidaPost.getFilters().add(new CorsFilters());


//  EXEMPLO:
//        server.createContext("/home/login", ); // POST
//        server.createContext("/teste1", );  // GET
//        server.createContext("/teste/", ); // PATCH com id na URL
    }
}

