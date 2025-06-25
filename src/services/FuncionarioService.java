package services;

import conexao.ConexaoPostgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class FuncionarioService {

    public List<Map<String, Object>> listarFuncionarios() {
        List<Map<String, Object>> lista = new ArrayList<>();
        String sql = "SELECT id_funcionario, nome, data_ingresso, salario, id_cargo, nome_fantasia FROM nossas_receitas.funcionarios";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> f = new HashMap<>();
                f.put("id_funcionario", rs.getInt("id_funcionario"));
                f.put("nome", rs.getString("nome"));
                f.put("data_ingresso", rs.getDate("data_ingresso").toString());
                f.put("salario", rs.getDouble("salario"));
                f.put("id_cargo", rs.getInt("id_cargo"));
                f.put("nome_fantasia", rs.getString("nome_fantasia"));
                lista.add(f);
            }

        } catch (Exception e) {
            System.err.println("Erro ao listar funcionários: " + e.getMessage());
        }

        return lista;
    }

    public boolean adicionarFuncionario(String nome, String data, double salario, int idCargo, String nomeFantasia) {
        String sql = "INSERT INTO nossas_receitas.funcionarios (nome, nome_fantasia, salario, data_ingresso, id_cargo) " +
                "VALUES (?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);              // nome
            stmt.setString(2, nomeFantasia);      // nome_fantasia
            stmt.setDouble(3, salario);           // salario
            stmt.setString(4, data);              // data_ingresso como string, TO_DATE no SQL
            stmt.setInt(5, idCargo);              // id_cargo

            int afetados = stmt.executeUpdate();
            return afetados > 0;

        } catch (Exception e) {
            System.err.println("Erro ao adicionar funcionário: " + e.getMessage());
            return false;
        }
    }
}
