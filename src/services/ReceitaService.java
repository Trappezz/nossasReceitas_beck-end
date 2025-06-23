package services;

import com.google.gson.JsonObject;
import conexao.ConexaoPostgres;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceitaService {

    public boolean inserirReceita(String nome, String modoPreparo, int idCozinheiro, int idCategoria) {
        String verificar = "SELECT COUNT(*) FROM nossas_receitas.receita WHERE nome = ?";
        String sql = "INSERT INTO nossas_receitas.receita (nome, modo_preparo, id_cozinheiro, id_categoria) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoPostgres.conectar()) {
            PreparedStatement checkStmt = conn.prepareStatement(verificar);
            checkStmt.setString(1, nome);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Receita já existe
            }

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, modoPreparo);
            stmt.setInt(3, idCozinheiro);
            stmt.setInt(4, idCategoria);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao inserir receita: " + e.getMessage());
            return false;
        }
    }

    public List<JsonObject> listarReceitas() {
        List<JsonObject> receitas = new ArrayList<>();
        String sql = "SELECT id_receita, nome, modo_preparo, id_cozinheiro, id_categoria, data_criacao FROM nossas_receitas.receita";

        try (Connection conn = ConexaoPostgres.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                JsonObject json = new JsonObject();
                json.addProperty("id_receita", rs.getInt("id_receita"));
                json.addProperty("nome", rs.getString("nome"));
                json.addProperty("modo_preparo", rs.getString("modo_preparo"));
                json.addProperty("id_cozinheiro", rs.getInt("id_cozinheiro"));
                json.addProperty("id_categoria", rs.getInt("id_categoria"));
                json.addProperty("data_criacao", rs.getString("data_criacao"));
                receitas.add(json);
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar receitas: " + e.getMessage());
        }

        return receitas;
    }
}
