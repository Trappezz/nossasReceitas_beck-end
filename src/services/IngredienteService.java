package services;

import conexao.ConexaoPostgres;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IngredienteService {

    public List<String> listarIngredientes() {
        List<String> ingredientes = new ArrayList<>();

        String sql = "SELECT * FROM nossas_receitas.ingrediente";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ingredientes.add(rs.getString("nome"));
            }

        } catch (Exception e) {
            System.err.println("Erro ao listar ingredientes: " + e.getMessage());
        }

        return ingredientes;
    }

    public boolean inserirIngrediente(String nome) {
        String sql = "INSERT INTO nossas_receitas.ingrediente (nome) VALUES (?)";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (Exception e) {
            System.err.println("Erro ao inserir ingrediente: " + e.getMessage());
            return false;
        }
    }

    public String converterListaParaJson(List<String> lista) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            json.append("\"").append(lista.get(i)).append("\"");
            if (i < lista.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }
}
