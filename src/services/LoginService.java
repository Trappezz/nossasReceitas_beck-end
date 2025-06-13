package services;

import conexao.ConexaoPostgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginService {
    public boolean autenticar(String email, String senha) {
        String sql = "SELECT * FROM nossas_receitas.usuario WHERE email = ? AND senha = ?";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // se existir algum resultado, está autenticado

        } catch (Exception e) {
            System.err.println("Erro na autenticação: " + e.getMessage());
            return false;
        }
    }
}
