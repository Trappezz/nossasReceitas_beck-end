package filters;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class CorsFilters extends Filter {
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:8080");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(200, -1); // resposta sem corpo
        } else {
            chain.doFilter(exchange);
        }
    }

    @Override
    public String description() {
        return "Filtro para adicionar cabeçalhos CORS";
    }
}
