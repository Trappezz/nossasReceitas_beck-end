package services;

import conexao.ConexaoPostgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReceitaService {

    public boolean cadastrarReceita(String nome, String modoPreparo, int idCozinheiro, int idCategoria) {
        String sqlVerifica = "SELECT 1 FROM nossa_receita.receita WHERE nome = ?";
        String sqlInsert = "INSERT INTO nossa_receita.receita (nome, modo_preparo, id_cozinheiro, id_categoria) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoPostgres.conectar()) {
            // Verificar se já existe
            PreparedStatement checkStmt = conn.prepareStatement(sqlVerifica);
            checkStmt.setString(1, nome);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false; // Receita já existe
            }

            // Inserir nova receita
            PreparedStatement insertStmt = conn.prepareStatement(sqlInsert);
            insertStmt.setString(1, nome);
            insertStmt.setString(2, modoPreparo);
            insertStmt.setInt(3, idCozinheiro);
            insertStmt.setInt(4, idCategoria);

            insertStmt.executeUpdate();
            return true;

        } catch (Exception e) {
            System.err.println("Erro ao cadastrar receita: " + e.getMessage());
            return false;
        }
    }
}
