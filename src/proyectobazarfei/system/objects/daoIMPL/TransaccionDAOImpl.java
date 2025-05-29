package proyectobazarfei.system.objects.daoIMPL;

import proyectobazarfei.system.objects.dao.TransaccionDAO;
import proyectobazarfei.system.objects.vo.TransaccionVO;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;
import proyectobazarfei.system.objects.vo.ProductoVO;
import proyectobazarfei.system.methods.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import proyectobazarfei.system.methods.LogManager;

public class TransaccionDAOImpl implements TransaccionDAO {

    @Override
    public void registrarTransaccion(TransaccionVO transaccion) {
        LogManager.debug("Registrando transacción: " + transaccion.toString());
        String sql = "INSERT INTO transacciones (id, producto, fecha_creacion, hora_creacion, estado, metodo_pago, calificacion, comentario, vendedor, comprador) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transaccion.getId());
            stmt.setInt(2, transaccion.getProducto().getId());
            stmt.setDate(3, new java.sql.Date(transaccion.getFechaCreacion().getTime()));
            stmt.setString(4, transaccion.getHoraCreacion());
            stmt.setString(5, transaccion.getEstado());
            stmt.setString(6, transaccion.getMetodoPago());
            if (transaccion.getCalificacion() != null) {
                stmt.setInt(7, transaccion.getCalificacion());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.setString(8, transaccion.getComentario());
            stmt.setInt(9, transaccion.getVendedor().getId());
            stmt.setInt(10, transaccion.getComprador().getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al registrar transacción: " + e.getMessage());
        }
    }

    @Override
    public List<TransaccionVO> obtenerProductosActivosPorPerfilId(int perfilId) {
        LogManager.debug("Obteniendo productos activos por id de perfil: " + perfilId);
        return obtenerTransaccionesPorPerfilYEstado(perfilId, "vendedor", "En proceso");
    }

    private List<TransaccionVO> obtenerTransaccionesPorPerfilYEstado(int perfilId, String tipo, String estado) {
            LogManager.debug("Obteniendo transacciones desde perfil: " + perfilId + ", tipo: " + tipo + ", estado: " + estado);
            String sql = "SELECT * FROM transacciones WHERE " + tipo +
                         " = (SELECT REF(p) FROM perfiles_usuarios p WHERE p.id = ?) AND estado = ? ORDER BY fecha_creacion DESC";

            List<TransaccionVO> lista = new ArrayList<>();

            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, perfilId);
                stmt.setString(2, estado);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    lista.add(mapearTransaccion(rs));
                }

            } catch (SQLException e) {
                LogManager.error("Error al obtener transacciones por perfil y estado: " + e.getMessage());
            }

            return lista;
        }
    
    @Override
    public List<TransaccionVO> obtenerComprasPorPerfilId(int perfilId) {
        LogManager.debug("Obteniendo compras por id de perfil: " + perfilId);
        return obtenerTransaccionesPorPerfil(perfilId, "comprador");
    }

    @Override
    public List<TransaccionVO> obtenerVentasPorPerfilId(int perfilId) {
        LogManager.debug("Obteniendo compras por id de perfil: " + perfilId);
        return obtenerTransaccionesPorPerfil(perfilId, "vendedor");
    }

    private List<TransaccionVO> obtenerTransaccionesPorPerfil(int perfilId, String tipo) {
        LogManager.debug("Obteniendo transacciones desde perfil: " + perfilId + " de tipo: " + tipo);
        String sql = "SELECT * FROM transacciones WHERE " + tipo + 
                     " = (SELECT REF(p) FROM perfiles_usuarios p WHERE p.id = ?) ORDER BY fecha_creacion DESC";
        List<TransaccionVO> lista = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, perfilId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearTransaccion(rs));
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener transacciones por perfil: " + e.getMessage());
        }

        return lista;
    }


    @Override
    public void actualizarEstadoTransaccion(int transaccionId, String nuevoEstado) {
        LogManager.debug("Actualizando estado de la transacción: " + transaccionId + " al estado: " + nuevoEstado);
        String sql = "UPDATE transacciones SET estado = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, transaccionId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de transacción: " + e.getMessage());
        }
    }

    @Override
    public void calificarTransaccion(int transaccionId, int calificacion, String comentario) {
        LogManager.debug("Calificando transacción de id: " + transaccionId + " con calificación: " + calificacion + " y el comentario: " + comentario);
        String sql = "UPDATE transacciones SET calificacion = ?, comentario = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, calificacion);
            stmt.setString(2, comentario);
            stmt.setInt(3, transaccionId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al calificar transacción: " + e.getMessage());
        }
    }

    @Override
    public TransaccionVO obtenerTransaccionPorId(int id) {
        LogManager.debug("Obteniendo transacción por id: " + id);
        String sql = "SELECT * FROM transacciones WHERE id = ?";
        TransaccionVO transaccion = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                transaccion = mapearTransaccion(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener transacción: " + e.getMessage());
        }

        return transaccion;
    }

    private TransaccionVO mapearTransaccion(ResultSet rs) throws SQLException {
        LogManager.debug("Mapeando transacción...");
        TransaccionVO t = new TransaccionVO();
        Connection conn = rs.getStatement().getConnection();

        // Producto
        Ref productoRef = rs.getRef("producto");
        PreparedStatement productoStmt = conn.prepareStatement("SELECT * FROM productos p WHERE REF(p) = ?");
        productoStmt.setRef(1, productoRef);
        ResultSet productoRs = productoStmt.executeQuery();
        ProductoVO producto = new ProductoVO();
        if (productoRs.next()) {
            producto.setId(productoRs.getInt("id"));
            producto.setTitulo(productoRs.getString("titulo"));
            producto.setDescripcion(productoRs.getString("descripcion"));
            producto.setPrecio(productoRs.getDouble("precio"));
            producto.setCategoria(productoRs.getString("categoria"));
            producto.setPortada(productoRs.getString("portada"));
        }
        t.setProducto(producto);

        // Datos básicos de transacción
        t.setId(rs.getInt("id"));
        t.setFechaCreacion(rs.getDate("fecha_creacion"));
        t.setHoraCreacion(rs.getString("hora_creacion"));
        t.setEstado(rs.getString("estado"));
        t.setMetodoPago(rs.getString("metodo_pago"));
        t.setCalificacion(rs.getInt("calificacion"));
        t.setComentario(rs.getString("comentario"));

        // Vendedor
        Ref vendedorRef = rs.getRef("vendedor");
        PreparedStatement vendedorStmt = conn.prepareStatement("SELECT * FROM perfiles_usuarios p WHERE REF(p) = ?");
        vendedorStmt.setRef(1, vendedorRef);
        ResultSet vendedorRs = vendedorStmt.executeQuery();
        PerfilUsuarioVO vendedor = new PerfilUsuarioVO();
        if (vendedorRs.next()) {
            vendedor.setId(vendedorRs.getInt("id"));
            vendedor.setDescripcion(vendedorRs.getString("descripcion"));
        }
        t.setVendedor(vendedor);

        // Comprador
        Ref compradorRef = rs.getRef("comprador");
        PreparedStatement compradorStmt = conn.prepareStatement("SELECT * FROM perfiles_usuarios p WHERE REF(p) = ?");
        compradorStmt.setRef(1, compradorRef);
        ResultSet compradorRs = compradorStmt.executeQuery();
        PerfilUsuarioVO comprador = new PerfilUsuarioVO();
        if (compradorRs.next()) {
            comprador.setId(compradorRs.getInt("id"));
            comprador.setDescripcion(compradorRs.getString("descripcion"));
        }
        t.setComprador(comprador);

        return t;
    }

} 