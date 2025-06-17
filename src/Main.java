import static conexao.ConexaoPostgres.conectar;
import com.sun.net.httpserver.HttpServer;
import routes.Router;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        conectar();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        Router.setupRoutes(server); // configura as rotas
        server.setExecutor(null); // usa um executor default
        server.start();
        System.out.println("Servidor iniciado na porta 8000");

    }
}