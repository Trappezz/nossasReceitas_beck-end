package conexao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoPostgres {

    private static final String URL = "jdbc:postgresql://localhost:5432/nossas_receitas";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "";

    public static Connection conectar() {
        Connection conexao = null;

        try {
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão realizada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco: " + e.getMessage());
        }

        return conexao;
    }

    public static void main(String[] args) {
        conectar();
    }
}

