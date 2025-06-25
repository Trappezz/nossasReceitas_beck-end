package services;

import conexao.ConexaoPostgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class FuncionarioService {

    public List<Map<String, Object>> listarFuncionarios() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT id_funcionario, nome, data_ingresso, salario, cargo_nome, nome_fantasia FROM nossas_receitas.funcionario";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> f = new HashMap<>();
                f.put("id_funcionario", rs.getInt("id_funcionario"));
                f.put("nome", rs.getString("nome"));
                f.put("data_ingresso", rs.getString("data_ingresso"));
                f.put("salario", rs.getDouble("salario"));
                f.put("cargo_nome", rs.getString("cargo_nome"));
                f.put("nome_fantasia", rs.getString("nome_fantasia"));
                lista.add(f);
            }

        } catch (Exception e) {
            System.err.println("Erro ao listar funcionários: " + e.getMessage());
        }

        return lista;
    }

    public boolean adicionarFuncionario(String nome, String data, double salario, String cargo, String restaurante) {
        String sql = "INSERT INTO nossas_receitas.funcionario (nome, data_ingresso, salario, cargo_nome, nome_fantasia) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, data);
            stmt.setDouble(3, salario);
            stmt.setString(4, cargo);
            stmt.setString(5, restaurante);

            int afetados = stmt.executeUpdate();
            return afetados > 0;

        } catch (Exception e) {
            System.err.println("Erro ao adicionar funcionário: " + e.getMessage());
            return false;
        }
    }
}
