package proyectobazarfei.system.objects.daoIMPL;

import java.math.BigDecimal;
import proyectobazarfei.system.objects.dao.ProductoDAO;
import proyectobazarfei.system.objects.vo.ProductoVO;
import proyectobazarfei.system.methods.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.methods.SesionManager;
import proyectobazarfei.system.objects.dao.PerfilUsuarioDAO;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public boolean crearProducto(ProductoVO producto) {
        LogManager.debug("Creando producto: " + producto.toString());

        String insertSQL = """
            INSERT INTO productos 
            (id, titulo, descripcion, precio, categoria, portada, metodos_pago_aceptados, imagenes) 
            VALUES (producto_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id INTO ?
        """;

        try (Connection conn = DatabaseManager.getConnection();
             OraclePreparedStatement stmt = (OraclePreparedStatement) conn.prepareStatement(insertSQL)) {

            stmt.setString(1, producto.getTitulo());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setString(4, producto.getCategoria());
            stmt.setString(5, producto.getPortada());

            Array metodosArray = conn.unwrap(oracle.jdbc.OracleConnection.class)
                .createOracleArray("SYS.ODCIVARCHAR2LIST", producto.getMetodosPagoAceptados().toArray());
            Array imagenesArray = conn.unwrap(oracle.jdbc.OracleConnection.class)
                .createOracleArray("SYS.ODCIVARCHAR2LIST", producto.getImagenes().toArray());


            stmt.setArray(6, metodosArray);
            stmt.setArray(7, imagenesArray);
            stmt.registerReturnParameter(8, java.sql.Types.INTEGER);
            stmt.executeUpdate();

            ResultSet rs = stmt.getReturnResultSet();
            int idGenerado = -1;
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                producto.setId(idGenerado);
            }

            LogManager.info("Producto creado con ID: " + idGenerado);

            // Asociar al perfil actual
            PerfilUsuarioDAO perfilUsuarioDAO = new PerfilUsuarioDAOImpl();
            PerfilUsuarioVO perfilActual = perfilUsuarioDAO.obtenerPerfilPorUsuarioId(SesionManager.obtenerUsuarioSesionActiva().getId());
            int idPerfil = perfilActual.getId();

            String sql = """
                DECLARE
                    prod_ref REF producto_typ;
                BEGIN
                    SELECT REF(p) INTO prod_ref FROM productos p WHERE p.id = :1;

                    UPDATE perfiles_usuarios pf
                    SET pf.productos = pf.productos MULTISET UNION DISTINCT
                        CAST(MULTISET(SELECT prod_ref FROM DUAL) AS lista_productos_typ)
                    WHERE pf.id = :2;
                END;
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, producto.getId());
                ps.setInt(2, perfilActual.getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error al asociar producto al perfil: " + e.getMessage());
            }

            LogManager.info("Producto asociado a perfil con ID: " + idPerfil);
            return true;

        } catch (SQLException e) {
            AlertaSistema.error("Error al crear y asociar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    
    private Array crearArrayDeStrings(Connection conn, List<String> lista) throws SQLException {
        if (lista == null || lista.isEmpty()) {
            return null;
        }

        OracleConnection oracleConn = conn.unwrap(OracleConnection.class);

        // Asegúrate de que ODCIVARCHAR2LIST existe en tu BD (es un tipo incorporado)
        ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor("ODCIVARCHAR2LIST", oracleConn);

        return new ARRAY(descriptor, oracleConn, lista.toArray(new String[0]));
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
        String sql = """
            UPDATE productos SET 
                titulo = ?, 
                descripcion = ?, 
                precio = ?, 
                categoria = ?, 
                portada = ?, 
                imagenes = ?, 
                metodos_pago_aceptados = ?
            WHERE id = ?
        """;


        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getTitulo());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setString(4, producto.getCategoria());
            stmt.setString(5, producto.getPortada());

            Array imagenesArray = crearArrayDeStrings(conn, producto.getImagenes());
            Array metodosArray = crearArrayDeStrings(conn, producto.getMetodosPagoAceptados());

            stmt.setArray(6, imagenesArray);
            stmt.setArray(7, metodosArray);
            stmt.setInt(8, producto.getId());


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
        
        Array metodosArray = rs.getArray("metodos_pago_aceptados");
        if (metodosArray != null) {
            p.setMetodosPagoAceptados(Arrays.asList((String[]) metodosArray.getArray()));
        }

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
        
        Array metodosArray = (Array) attrs[7];
        if (metodosArray != null) {
            p.setMetodosPagoAceptados(Arrays.asList((String[]) metodosArray.getArray()));
        }

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
                ProductoVO producto = mapearProductoDesdeResultSet(rs);
                lista.add(producto);
            }

        } catch (SQLException e) {
            LogManager.error("Error al buscar productos con filtros: " + e.getMessage());
        }

        return lista;
    }

} 
