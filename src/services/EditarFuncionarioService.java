package services;

import conexao.ConexaoPostgres;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditarFuncionarioService {

    public boolean atualizarFuncionario(int id, String nome, String dataIngresso, String rg,
                                        int idCargo, double salario, String nomeFantasia, String email) {

        String sql = """
            UPDATE nossas_receitas.funcionarios
            SET nome = ?, data_ingresso = ?, rg = ?, id_cargo = ?, salario = ?, nome_fantasia = ?, email = ?
            WHERE id_funcionario = ?
        """;

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, dataIngresso);
            stmt.setString(3, rg);
            stmt.setInt(4, idCargo);
            stmt.setDouble(5, salario);
            stmt.setString(6, nomeFantasia);
            stmt.setString(7, email);
            stmt.setInt(8, id);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Erro ao atualizar funcionário: " + e.getMessage());
            return false;
        }
    }
}
