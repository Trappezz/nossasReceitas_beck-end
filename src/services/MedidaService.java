package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import conexao.ConexaoPostgres;

public class MedidaService {

    public boolean inserirMedida(String nome) {
        String sql = "INSERT INTO nossas_receitas.medida (nome) VALUES (?)";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> listarMedidas() {
        List<String> medidas = new ArrayList<>();
        String sql = "SELECT * FROM nossas_receitas.medida";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String medidaJson = String.format("{\"id\": %d, \"nome\": \"%s\"}", rs.getInt("id_medida"), rs.getString("nome"));
                medidas.add(medidaJson);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return medidas;
    }

    public String converterListaParaJson(List<String> medidas) {
        return "[" + String.join(",", medidas) + "]";
    }
}
