package services;

import conexao.ConexaoPostgres;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CategoriaService {

    public boolean inserirCategoria(String descricao) {
        String sql = "INSERT INTO nossas_receitas.categoria (descricao) VALUES (?)";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, descricao);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (Exception e) {
            System.err.println("Erro ao inserir categoria: " + e.getMessage());
            return false;
        }
    }
}
