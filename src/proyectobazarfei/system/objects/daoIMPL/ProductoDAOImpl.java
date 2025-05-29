package proyectobazarfei.system.objects.daoIMPL;

import java.math.BigDecimal;
import proyectobazarfei.system.objects.dao.ProductoDAO;
import proyectobazarfei.system.objects.vo.ProductoVO;
import proyectobazarfei.system.methods.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oracle.sql.STRUCT;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.LogManager;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public void crearProducto(ProductoVO producto) {
        LogManager.debug("Creando producto: " + producto.toString());
        String sql = "INSERT INTO productos (id, titulo, descripcion, precio, categoria) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, producto.getId());
            stmt.setString(2, producto.getTitulo());
            stmt.setString(3, producto.getDescripcion());
            stmt.setDouble(4, producto.getPrecio());
            stmt.setString(5, producto.getCategoria());
            stmt.executeUpdate();

            // Imágenes deben insertarse por separado si están en una tabla anidada

        } catch (SQLException e) {
            AlertaSistema.error("Error al crear producto: " + e.getMessage());
        }
    }

    @Override
    public ProductoVO obtenerProductoPorId(int id) {
        LogManager.debug("Obteniendo mensaje por id: " + id);
        String sql = "SELECT * FROM productos WHERE id = ?";
        ProductoVO producto = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                producto = mapearProductoDesdeResultSet(rs);
            }

        } catch (SQLException e) {
            AlertaSistema.error("Error al obtener producto por ID: " + e.getMessage());
        }

        return producto;
    }

    @Override
    public List<ProductoVO> obtenerProductosPorPerfilId(int perfilId) {
        LogManager.debug("Obteniendo productos por la id del perfil: " + perfilId);

        String sql = """
            SELECT DEREF(VALUE(p))
            FROM TABLE(
                SELECT productos
                FROM perfiles_usuarios
                WHERE id = ?
            ) p
        """;

        List<ProductoVO> lista = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, perfilId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearProductoDesdeStruct(rs));
            }

        } catch (SQLException e) {
            AlertaSistema.error("Error al obtener productos del perfil: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<ProductoVO> buscarProductos(String criterioBusqueda) {
        LogManager.debug("Buscando productos por criterio de búsqueda: " + criterioBusqueda);
        String sql = "SELECT * FROM productos WHERE LOWER(titulo) LIKE ? OR LOWER(descripcion) LIKE ?";
        List<ProductoVO> lista = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String criterio = "%" + criterioBusqueda.toLowerCase() + "%";
            stmt.setString(1, criterio);
            stmt.setString(2, criterio);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearProductoDesdeResultSet(rs));
            }

        } catch (SQLException e) {
            AlertaSistema.error("Error al buscar productos: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public void actualizarProducto(ProductoVO producto) {
        LogManager.debug("Actualizando producto: " + producto.toString());
        String sql = "UPDATE productos SET titulo = ?, descripcion = ?, precio = ?, categoria = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getTitulo());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setString(4, producto.getCategoria());
            stmt.setInt(5, producto.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            AlertaSistema.error("Error al actualizar producto: " + e.getMessage());
        }
    }

    @Override
    public void eliminarProducto(int id) {
        LogManager.debug("Eliminando producto por id: " + id);
        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            AlertaSistema.error("Error al eliminar producto: " + e.getMessage());
        }
    }

    private ProductoVO mapearProductoDesdeResultSet(ResultSet rs) throws SQLException {
        ProductoVO p = new ProductoVO();
        p.setId(rs.getInt("id"));
        p.setTitulo(rs.getString("titulo"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getDouble("precio"));
        p.setCategoria(rs.getString("categoria"));

        Array imagenesArray = rs.getArray("imagenes");
        if (imagenesArray != null) {
            p.setImagenes(Arrays.asList((String[]) imagenesArray.getArray()));
        }

        p.setPortada(rs.getString("portada"));
        return p;
    }
    
    private ProductoVO mapearProductoDesdeStruct(ResultSet rs) throws SQLException {
        STRUCT struct = (STRUCT) rs.getObject(1);
        Object[] attrs = struct.getAttributes();
        ProductoVO p = new ProductoVO();

        p.setId(((BigDecimal) attrs[0]).intValue());
        p.setTitulo((String) attrs[1]);
        p.setDescripcion((String) attrs[2]);
        p.setPrecio(((BigDecimal) attrs[3]).doubleValue());
        p.setCategoria((String) attrs[4]);

        Array imagenesArray = (Array) attrs[5];
        if (imagenesArray != null) {
            p.setImagenes(Arrays.asList((String[]) imagenesArray.getArray()));
        }

        p.setPortada((String) attrs[6]);

        return p;
    }

    
    @Override
    public List<ProductoVO> obtenerTodosLosProductos() {
        LogManager.debug("Obteniendo todos los productos...");
        String sql = "SELECT * FROM productos";
        List<ProductoVO> lista = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearProductoDesdeResultSet(rs));
            }

        } catch (SQLException e) {
            AlertaSistema.error("Error al obtener la lista de productos.");
        }

        return lista;
    }
    
    @Override
    public List<ProductoVO> buscarProductosConFiltros(String texto, String categoria, Double precioMin, Double precioMax) {
        LogManager.debug("Buscando productos con filtros...");

        StringBuilder sql = new StringBuilder("SELECT * FROM productos WHERE 1=1 ");
        List<Object> parametros = new ArrayList<>();

        if (texto != null && !texto.isBlank()) {
            sql.append("AND LOWER(titulo) LIKE ? ");
            parametros.add("%" + texto.toLowerCase() + "%");
        }

        if (categoria != null && !categoria.isBlank() && !categoria.equals("Categoría")) {
            sql.append("AND LOWER(categoria) = ? ");
            parametros.add(categoria.toLowerCase());
        }

        if (precioMin != null) {
            sql.append("AND precio >= ? ");
            parametros.add(precioMin);
        }

        if (precioMax != null) {
            sql.append("AND precio <= ? ");
            parametros.add(precioMax);
        }

        List<ProductoVO> lista = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductoVO producto = new ProductoVO();
                producto.setId(rs.getInt("id"));
                producto.setTitulo(rs.getString("titulo"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setCategoria(rs.getString("categoria"));
                producto.setPortada(rs.getString("portada"));
                // las imágenes adicionales se cargan por separado si se requieren
                lista.add(producto);
            }

        } catch (SQLException e) {
            LogManager.error("Error al buscar productos con filtros: " + e.getMessage());
        }

        return lista;
    }

} 
