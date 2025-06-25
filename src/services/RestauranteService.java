package services;

import conexao.ConexaoPostgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class RestauranteService {
    public List<Map<String, Object>> listarRestaurantes() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT nome, nota FROM nossas_receitas.restaurante ORDER BY nome ASC";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> restaurante = new HashMap<>();
                restaurante.put("nome", rs.getString("nome"));
                restaurante.put("nota", rs.getInt("nota"));
                lista.add(restaurante);
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar restaurantes: " + e.getMessage());
        }

        return lista;
    }
}
