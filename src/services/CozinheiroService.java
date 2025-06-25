package services;

import conexao.ConexaoPostgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class CozinheiroService {
    public List<Map<String, Object>> listarProducaoMensal() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = """
            SELECT nome, prontas, afazer, meta, checkbox
            FROM nossas_receitas.producao_mensal
            ORDER BY nome ASC
        """;

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> cozinheiro = new HashMap<>();
                cozinheiro.put("nome", rs.getString("nome"));
                cozinheiro.put("prontas", rs.getInt("prontas"));
                cozinheiro.put("afazer", rs.getInt("afazer"));
                cozinheiro.put("meta", rs.getInt("meta"));
                cozinheiro.put("checkbox", rs.getBoolean("checkbox"));
                lista.add(cozinheiro);
            }

        } catch (Exception e) {
            System.err.println("Erro ao buscar produção mensal: " + e.getMessage());
        }

        return lista;
    }
}
