package services;

import conexao.ConexaoPostgres;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PerfilService {
    public static String obterCargo(String email, String senha) {
        String sql = "SELECT * FROM nossas_receitas.usuario WHERE email = ? AND senha = ?";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("id_cargo"); // pega o valor da coluna "cargo"
            } else {
                return null; // ou um valor padrão, como "desconhecido"
            }

        } catch (Exception e) {
            System.err.println("Erro ao obter cargo: " + e.getMessage());
            return null;
        }
    }
}
