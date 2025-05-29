package proyectobazarfei.system.objects.daoIMPL;

import proyectobazarfei.system.objects.dao.ArticuloAyudaDAO;
import proyectobazarfei.system.objects.vo.ArticuloAyudaVO;
import proyectobazarfei.system.methods.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import proyectobazarfei.system.methods.LogManager;

public class ArticuloAyudaDAOImpl implements ArticuloAyudaDAO {

    @Override
    public List<ArticuloAyudaVO> obtenerTodosLosArticulos() {
        LogManager.debug("Obteniendo todos los artículos...");
        String sql = "SELECT * FROM articulos_ayuda";
        List<ArticuloAyudaVO> lista = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                lista.add(mapearArticulo(rs));
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener artículos de ayuda: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public ArticuloAyudaVO obtenerArticuloPorId(int id) {
        LogManager.debug("Obteniendo artículo con la id: " + id);
        String sql = "SELECT * FROM articulos_ayuda WHERE id = ?";
        ArticuloAyudaVO articulo = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                articulo = mapearArticulo(rs);
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener artículo de ayuda: " + e.getMessage());
        }

        return articulo;
    }

    private ArticuloAyudaVO mapearArticulo(ResultSet rs) throws SQLException {
        LogManager.debug("Mapeando artículo: " + rs.toString());
        ArticuloAyudaVO a = new ArticuloAyudaVO();
        a.setId(rs.getInt("id"));
        a.setTitulo(rs.getString("titulo"));
        a.setContenido(rs.getString("contenido"));
        return a;
    }
} 
