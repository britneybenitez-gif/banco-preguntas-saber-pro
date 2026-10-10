package co.edu.unicauca.bancopreguntas.dataaccess;

import co.edu.unicauca.bancopreguntas.domain.repositories.AsignacionRevisorRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AsignacionRevisorRepositorySQLite implements AsignacionRevisorRepository {

    private Connection connection; // Inyecta o gestiona tu conexión SQLite aquí

    public AsignacionRevisorRepositorySQLite(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean asignarRevisor(int preguntaId, String correoRevisor) {
        String sql = "INSERT INTO AsignacionRevisor(pregunta_id, correo_revisor) VALUES(?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, preguntaId);
            pstmt.setString(2, correoRevisor);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar asignación: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean existeAsignacion(int preguntaId, String correoRevisor) {
        String sql = "SELECT COUNT(*) FROM AsignacionRevisor WHERE pregunta_id = ? AND correo_revisor = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, preguntaId);
            pstmt.setString(2, correoRevisor);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}