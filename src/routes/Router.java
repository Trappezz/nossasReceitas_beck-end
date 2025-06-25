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

        var ingredientePost = server.createContext("/ingredientes/adcionar", new IngredienteController("POST"));
        ingredientePost.getFilters().add(new CorsFilters());

        var receitasGet = server.createContext("/receitas/consultar", new ReceitaController("GET"));
        receitasGet.getFilters().add(new CorsFilters());

        var receitasPost = server.createContext("/receitas/adcionar", new ReceitaController("POST"));
        receitasPost.getFilters().add(new CorsFilters());

        var medidaGet = server.createContext("/medida/consultar", new MedidaController("GET"));
        medidaGet.getFilters().add(new CorsFilters());

        var medidaPost = server.createContext("/medida/adcionar", new MedidaController("POST"));
        medidaPost.getFilters().add(new CorsFilters());

        var restauranteGet = server.createContext("/restaurantes", new RestauranteController("GET"));
        restauranteGet.getFilters().add(new CorsFilters());

        var cozinheirosGet = server.createContext("/api/cozinheiros", new CozinheiroController("GET"));
        cozinheirosGet.getFilters().add(new CorsFilters());

        var categoriaPost = server.createContext("/api/categorias", new CategoriaController("POST"));
        categoriaPost.getFilters().add(new CorsFilters());

        var funcionarioGet = server.createContext("/funcionarios", new FuncionarioController("GET"));
        funcionarioGet.getFilters().add(new CorsFilters());

        var funcionarioPost = server.createContext("/funcionarios", new FuncionarioController("POST"));
        funcionarioPost.getFilters().add(new CorsFilters());

        var funcionarioUpdate = server.createContext("/api/funcionarios/atualizar", new EditarFuncionarioController());
        funcionarioUpdate.getFilters().add(new CorsFilters());



//  EXEMPLO:
//        server.createContext("/home/login", ); // POST
//        server.createContext("/teste1", );  // GET
//        server.createContext("/teste/", ); // PATCH com id na URL
    }
}

