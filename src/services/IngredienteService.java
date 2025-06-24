package services;

import conexao.ConexaoPostgres;
import java.sql.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class IngredienteService {

    public boolean inserirIngrediente(String nome, String descricao, int idMedida) {
        String sql = "INSERT INTO nossas_receitas.ingrediente (nome, descricao, id_medida) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setInt(3, idMedida);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (Exception e) {
            System.err.println("Erro ao inserir ingrediente: " + e.getMessage());
            return false;
        }
    }

    public String listarIngredientesJson() {
        String sql = """
            SELECT i.id_ingrediente, i.nome, i.descricao, m.id_medida, m.nome AS medida
            FROM nossas_receitas.ingrediente i
            LEFT JOIN nossas_receitas.medida m ON i.id_medida = m.id_medida
        """;

        JsonArray jsonArray = new JsonArray();

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("id_ingrediente", rs.getInt("id_ingrediente"));
                obj.addProperty("nome", rs.getString("nome"));
                obj.addProperty("descricao", rs.getString("descricao"));
                obj.addProperty("id_medida", rs.getInt("id_medida"));
                obj.addProperty("medida", rs.getString("medida"));
                jsonArray.add(obj);
            }

        } catch (Exception e) {
            System.err.println("Erro ao listar ingredientes: " + e.getMessage());
        }

        return jsonArray.toString();
    }
}
