package proyectobazarfei.system.objects.daoIMPL;

import proyectobazarfei.system.objects.dao.PreguntaSeguridadDAO;
import proyectobazarfei.system.objects.vo.PreguntaSeguridadVO;
import proyectobazarfei.system.methods.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import proyectobazarfei.system.methods.LogManager;

public class PreguntaSeguridadDAOImpl implements PreguntaSeguridadDAO {

    @Override
    public List<PreguntaSeguridadVO> obtenerTodasLasPreguntas() {
        LogManager.debug("Obteniendo todas las preguntas...");
        String sql = "SELECT * FROM preguntas_seguridad";
        List<PreguntaSeguridadVO> lista = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearPregunta(rs));
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener preguntas de seguridad: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public PreguntaSeguridadVO obtenerPreguntaPorId(int id) {
        LogManager.debug("Obteniendo pregunta por id: " + id);
        String sql = "SELECT * FROM preguntas_seguridad WHERE id = ?";
        PreguntaSeguridadVO pregunta = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                pregunta = mapearPregunta(rs);
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener pregunta de seguridad: " + e.getMessage());
        }

        return pregunta;
    }

    private PreguntaSeguridadVO mapearPregunta(ResultSet rs) throws SQLException {
        LogManager.debug("Mapeando pregunta...");
        PreguntaSeguridadVO p = new PreguntaSeguridadVO();
        p.setId(rs.getInt("id"));
        p.setPregunta(rs.getString("pregunta"));
        return p;
    }
    
    @Override
    public PreguntaSeguridadVO obtenerPreguntaAleatoria() {
        LogManager.debug("Obteniendo una pregunta de seguridad aleatoria.");
        String sql = "SELECT * FROM preguntas_seguridad ORDER BY DBMS_RANDOM.VALUE FETCH FIRST 1 ROWS ONLY";
        PreguntaSeguridadVO pregunta = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                pregunta = mapearPregunta(rs);
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener pregunta de seguridad aleatoria.");
        }

        return pregunta;
    }
} 
