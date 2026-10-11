package co.edu.unicauca.bancopreguntas.dataaccess;

import co.edu.unicauca.bancopreguntas.domain.entities.EstadoPregunta;
import co.edu.unicauca.bancopreguntas.domain.entities.Pregunta;
import co.edu.unicauca.bancopreguntas.domain.repositories.PreguntaRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PreguntaRepositorySQLite implements PreguntaRepository {
    private final String url;

    // Constructor para la aplicación real (BD local en la raíz del proyecto)
    public PreguntaRepositorySQLite() {
        this.url = "jdbc:sqlite:saberpro.db";
        inicializarBaseDeDatos();
    }

    // Constructor para pruebas unitarias (BD en memoria, NO se guarda en disco)
    public PreguntaRepositorySQLite(String url) {
        this.url = url;
        inicializarBaseDeDatos();
    }

    private void inicializarBaseDeDatos() {
        String sql = "CREATE TABLE IF NOT EXISTS preguntas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "autor_id INTEGER NOT NULL, " +
                "contexto TEXT, " +
                "pregunta_directa TEXT NOT NULL, " +
                "distractor1 TEXT, distractor2 TEXT, distractor3 TEXT, distractor4 TEXT, " +
                "respuesta_correcta TEXT, " +
                "justificacion TEXT, " +
                "bibliografia TEXT, " +
                "competencia TEXT, " +
                "tema TEXT, " +
                "subtema TEXT, " +
                "nivel_dificultad TEXT, " +
                "estado TEXT DEFAULT 'BORRADOR'" +
                ")";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar la BD: " + e.getMessage(), e);
        }
    }

    @Override
    public void guardar(Pregunta pregunta) {
        String sql = "INSERT INTO preguntas (autor_id, contexto, pregunta_directa, distractor1, distractor2, distractor3, distractor4, respuesta_correcta, justificacion, bibliografia, competencia, tema, subtema, nivel_dificultad, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, pregunta.getAutorId());
            ps.setString(2, pregunta.getContexto());
            ps.setString(3, pregunta.getPreguntaDirecta());
            ps.setString(4, pregunta.getDistractor1());
            ps.setString(5, pregunta.getDistractor2());
            ps.setString(6, pregunta.getDistractor3());
            ps.setString(7, pregunta.getDistractor4());
            ps.setString(8, pregunta.getRespuestaCorrecta());
            ps.setString(9, pregunta.getJustificacion());
            ps.setString(10, pregunta.getBibliografia());
            ps.setString(11, pregunta.getCompetencia());
            ps.setString(12, pregunta.getTema());
            ps.setString(13, pregunta.getSubtema());
            ps.setString(14, pregunta.getNivelDificultad());
            ps.setString(15, pregunta.getEstado().name());
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pregunta.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la pregunta: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarEstado(int preguntaId, EstadoPregunta nuevoEstado) {
        String sql = "UPDATE preguntas SET estado = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, preguntaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el estado: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Pregunta> buscarPorId(int id) {
        String sql = "SELECT * FROM preguntas WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapearPregunta(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la pregunta: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Pregunta> listarPorAutor(int autorId, List<EstadoPregunta> estadosFiltro, String nivelDificultad, int offset, int limit) {
        List<Pregunta> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM preguntas WHERE autor_id = ?");
        List<Object> params = new ArrayList<>();
        params.add(autorId);

        if (estadosFiltro != null && !estadosFiltro.isEmpty()) {
            sql.append(" AND estado IN (").append(String.join(",", Collections.nCopies(estadosFiltro.size(), "?"))).append(")");
            for (EstadoPregunta estado : estadosFiltro) {
                params.add(estado.name());
            }
        }
        if (nivelDificultad != null && !nivelDificultad.trim().isEmpty()) {
            sql.append(" AND nivel_dificultad = ?");
            params.add(nivelDificultad);
        }
        sql.append(" ORDER BY id DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPregunta(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar preguntas: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public int contarPorAutor(int autorId, List<EstadoPregunta> estadosFiltro, String nivelDificultad) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM preguntas WHERE autor_id = ?");
        List<Object> params = new ArrayList<>();
        params.add(autorId);

        if (estadosFiltro != null && !estadosFiltro.isEmpty()) {
            sql.append(" AND estado IN (").append(String.join(",", Collections.nCopies(estadosFiltro.size(), "?"))).append(")");
            for (EstadoPregunta estado : estadosFiltro) {
                params.add(estado.name());
            }
        }
        if (nivelDificultad != null && !nivelDificultad.trim().isEmpty()) {
            sql.append(" AND nivel_dificultad = ?");
            params.add(nivelDificultad);
        }

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar preguntas: " + e.getMessage(), e);
        }
        return 0;
    }

    // Método auxiliar para convertir una fila de la BD en un objeto Pregunta
    private Pregunta mapearPregunta(ResultSet rs) throws SQLException {
        Pregunta p = new Pregunta(); 
        p.setId(rs.getInt("id"));
        p.setAutorId(rs.getInt("autor_id"));
        p.setContexto(rs.getString("contexto"));
        p.setPreguntaDirecta(rs.getString("pregunta_directa"));
        p.setDistractor1(rs.getString("distractor1"));
        p.setDistractor2(rs.getString("distractor2"));
        p.setDistractor3(rs.getString("distractor3"));
        p.setDistractor4(rs.getString("distractor4"));
        p.setRespuestaCorrecta(rs.getString("respuesta_correcta"));
        p.setJustificacion(rs.getString("justificacion"));
        p.setBibliografia(rs.getString("bibliografia"));
        p.setCompetencia(rs.getString("competencia"));
        p.setTema(rs.getString("tema"));
        p.setSubtema(rs.getString("subtema"));
        p.setNivelDificultad(rs.getString("nivel_dificultad"));
        p.setEstado(EstadoPregunta.valueOf(rs.getString("estado")));
        return p;
    }
}