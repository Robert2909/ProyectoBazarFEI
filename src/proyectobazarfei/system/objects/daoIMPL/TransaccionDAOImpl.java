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
        String sql = "INSERT INTO transacciones VALUES (transaccion_typ(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?))";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transaccion.getId());

            // producto
            PreparedStatement refStmt = conn.prepareStatement("SELECT REF(p) FROM productos p WHERE p.id = ?");
            refStmt.setInt(1, transaccion.getProducto().getId());
            ResultSet rsRef = refStmt.executeQuery();
            Ref refProducto = null;
            if (rsRef.next()) {
                refProducto = (Ref) rsRef.getObject(1);
            }
            stmt.setRef(2, refProducto);

            // métodos y entrega
            stmt.setString(3, transaccion.getMetodoPagoElegido());
            stmt.setString(4, transaccion.getMetodoEntregaElegido());
            stmt.setString(5, transaccion.getLugarEntrega());
            stmt.setString(6, transaccion.getHoraEntrega());

            // fechas
            stmt.setDate(7, new java.sql.Date(transaccion.getFechaCreacion().getTime()));
            stmt.setString(8, transaccion.getHoraCreacion());

            // estado, calificación, comentario
            stmt.setString(9, transaccion.getEstado());
            if (transaccion.getCalificacion() != null) {
                stmt.setInt(10, transaccion.getCalificacion());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }
            stmt.setString(11, transaccion.getComentario());

            // vendedor
            PreparedStatement refVendedorStmt = conn.prepareStatement("SELECT REF(p) FROM perfiles_usuarios p WHERE p.id = ?");
            refVendedorStmt.setInt(1, transaccion.getVendedor().getId());
            ResultSet rsVend = refVendedorStmt.executeQuery();
            if (rsVend.next()) {
                stmt.setRef(12, (Ref) rsVend.getObject(1));
            }

            // comprador
            PreparedStatement refCompradorStmt = conn.prepareStatement("SELECT REF(p) FROM perfiles_usuarios p WHERE p.id = ?");
            refCompradorStmt.setInt(1, transaccion.getComprador().getId());
            ResultSet rsComp = refCompradorStmt.executeQuery();
            if (rsComp.next()) {
                stmt.setRef(13, (Ref) rsComp.getObject(1));
            }

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

        // === Producto ===
        Ref productoRef = rs.getRef("producto");
        ProductoVO producto = new ProductoVO();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM productos p WHERE REF(p) = ?")) {
            stmt.setRef(1, productoRef);
            try (ResultSet prs = stmt.executeQuery()) {
                if (prs.next()) {
                    producto.setId(prs.getInt("id"));
                    producto.setTitulo(prs.getString("titulo"));
                    producto.setDescripcion(prs.getString("descripcion"));
                    producto.setPrecio(prs.getDouble("precio"));
                    producto.setCategoria(prs.getString("categoria"));
                    producto.setPortada(prs.getString("portada"));

                    Array imagenesArray = prs.getArray("imagenes");
                    if (imagenesArray != null) {
                        producto.setImagenes(List.of((String[]) imagenesArray.getArray()));
                    }

                    Array metodosPagoArray = prs.getArray("metodos_pago_aceptados");
                    if (metodosPagoArray != null) {
                        producto.setMetodosPagoAceptados(List.of((String[]) metodosPagoArray.getArray()));
                    }
                }
            }
        }
        t.setProducto(producto);

        // === Atributos básicos de transacción ===
        t.setId(rs.getInt("id"));
        t.setFechaCreacion(rs.getDate("fecha_creacion"));
        t.setHoraCreacion(rs.getString("hora_creacion"));
        t.setEstado(rs.getString("estado"));

        // Nuevos campos de método de pago y entrega
        t.setMetodoPagoElegido(rs.getString("metodo_pago_elegido"));
        t.setMetodoEntregaElegido(rs.getString("metodo_entrega_elegido"));
        t.setLugarEntrega(rs.getString("lugar_entrega")); // puede ser null
        t.setHoraEntrega(rs.getString("hora_entrega"));   // puede ser null

        int calif = rs.getInt("calificacion");
        t.setCalificacion(rs.wasNull() ? null : calif);
        t.setComentario(rs.getString("comentario"));

        // === Vendedor ===
        Ref vendedorRef = rs.getRef("vendedor");
        PerfilUsuarioVO vendedor = new PerfilUsuarioVO();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM perfiles_usuarios p WHERE REF(p) = ?")) {
            stmt.setRef(1, vendedorRef);
            try (ResultSet rsVend = stmt.executeQuery()) {
                if (rsVend.next()) {
                    vendedor.setId(rsVend.getInt("id"));
                    vendedor.setDescripcion(rsVend.getString("descripcion"));
                }
            }
        }
        t.setVendedor(vendedor);

        // === Comprador ===
        Ref compradorRef = rs.getRef("comprador");
        PerfilUsuarioVO comprador = new PerfilUsuarioVO();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM perfiles_usuarios p WHERE REF(p) = ?")) {
            stmt.setRef(1, compradorRef);
            try (ResultSet rsComp = stmt.executeQuery()) {
                if (rsComp.next()) {
                    comprador.setId(rsComp.getInt("id"));
                    comprador.setDescripcion(rsComp.getString("descripcion"));
                }
            }
        }
        t.setComprador(comprador);

        return t;
    }
} 