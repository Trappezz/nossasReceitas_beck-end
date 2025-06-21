package services;

import conexao.ConexaoPostgres;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PerfilService {
    public static String obterCargo(String id_funcionarios) {
        String sql = "SELECT * FROM nossas_receitas.funcionarios WHERE id_funcionarios = ?";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(id_funcionarios));
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
