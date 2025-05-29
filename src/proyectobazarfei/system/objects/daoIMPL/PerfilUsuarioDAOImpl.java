package proyectobazarfei.system.objects.daoIMPL;

import proyectobazarfei.system.objects.dao.PerfilUsuarioDAO;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;
import proyectobazarfei.system.objects.vo.UsuarioVO;
import proyectobazarfei.system.methods.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.objects.vo.PreguntaSeguridadVO;

public class PerfilUsuarioDAOImpl implements PerfilUsuarioDAO {

    @Override
    public PerfilUsuarioVO obtenerPerfilPorUsuarioId(int usuarioId) {
        LogManager.debug("Obteniendo perfil por id: " + usuarioId);
        String sql = "SELECT * FROM perfiles_usuarios WHERE datos_usuario = ?";

        PerfilUsuarioVO perfil = null;

        try (Connection conn = DatabaseManager.getConnection()) {

            // Obtener el REF al usuario
            PreparedStatement refStmt = conn.prepareStatement("SELECT REF(u) FROM usuarios u WHERE u.id = ?");
            refStmt.setInt(1, usuarioId);
            ResultSet refRs = refStmt.executeQuery();

            Ref refUsuario = null;
            if (refRs.next()) {
                refUsuario = refRs.getRef(1);
            } else {
                LogManager.error("No se encontró el REF del usuario con ID: " + usuarioId);
                return null;
            }

            // Usar el REF en la búsqueda
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setRef(1, refUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                perfil = mapearPerfil(rs);
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener perfil de usuario: " + e.getMessage());
        }

        return perfil;
    }


    @Override
    public void actualizarPerfil(PerfilUsuarioVO perfil) {
        LogManager.debug("Actualizando perfil: " + perfil.toString());
        String sql = "UPDATE perfiles_usuarios SET foto_perfil = ?, descripcion = ?, calificacion_promedio = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, perfil.getFotoPerfil());
            stmt.setString(2, perfil.getDescripcion());
            stmt.setDouble(3, perfil.getCalificacionPromedio());
            stmt.setInt(4, perfil.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            LogManager.error("Error al actualizar perfil: " + e.getMessage());
        }
    }

    @Override
    public void crearPerfil(PerfilUsuarioVO perfil) {
        LogManager.debug("Creando perfil: " + perfil.toString());
        String sql = "INSERT INTO perfiles_usuarios (id, datos_usuario, foto_perfil, descripcion, calificacion_promedio) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, perfil.getId());
            stmt.setInt(2, perfil.getDatosUsuario().getId());
            stmt.setString(3, perfil.getFotoPerfil());
            stmt.setString(4, perfil.getDescripcion());
            stmt.setDouble(5, perfil.getCalificacionPromedio());

            stmt.executeUpdate();

        } catch (SQLException e) {
            LogManager.error("Error al crear perfil: " + e.getMessage());
        }
    }

    @Override
    public void eliminarPerfil(int perfilId) {
        LogManager.debug("Eliminando perfil por id: " + perfilId);
        String sql = "DELETE FROM perfiles_usuarios WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, perfilId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar perfil: " + e.getMessage());
        }
    }

    private PerfilUsuarioVO mapearPerfil(ResultSet rs) throws SQLException {
        LogManager.debug("Mapeando perfil...");

        PerfilUsuarioVO perfil = new PerfilUsuarioVO();
        UsuarioVO usuario = new UsuarioVO();

        perfil.setId(rs.getInt("id"));

        // Obtener el REF al usuario
        Ref refUsuario = rs.getRef("datos_usuario");
        Connection conn = rs.getStatement().getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuarios u WHERE REF(u) = ?");
        stmt.setRef(1, refUsuario);
        ResultSet refRs = stmt.executeQuery();

        if (refRs.next()) {
            usuario.setId(refRs.getInt("id"));
            usuario.setNombre(refRs.getString("nombre"));
            usuario.setApodo(refRs.getString("apodo"));
            usuario.setCorreo(refRs.getString("correo"));
            usuario.setContrasena(refRs.getString("contrasena"));
            usuario.setRespuestaSeguridad(refRs.getString("respuesta_seguridad"));

            // REF a pregunta de seguridad
            Ref refPregunta = refRs.getRef("pregunta_seguridad");
            PreparedStatement stmtPregunta = conn.prepareStatement("SELECT * FROM preguntas_seguridad p WHERE REF(p) = ?");
            stmtPregunta.setRef(1, refPregunta);
            ResultSet rsPregunta = stmtPregunta.executeQuery();

            if (rsPregunta.next()) {
                PreguntaSeguridadVO pregunta = new PreguntaSeguridadVO();
                pregunta.setId(rsPregunta.getInt("id"));
                pregunta.setPregunta(rsPregunta.getString("pregunta"));
                usuario.setPreguntaSeguridad(pregunta);
            }
        }

        perfil.setDatosUsuario(usuario);
        perfil.setFotoPerfil(rs.getString("foto_perfil"));
        perfil.setDescripcion(rs.getString("descripcion"));
        perfil.setCalificacionPromedio(rs.getDouble("calificacion_promedio"));

        return perfil;
    }

    @Override
    public PerfilUsuarioVO obtenerPerfilPorId(int id) {
        LogManager.debug("Obteniendo perfil por ID: " + id);
        String sql = "SELECT * FROM perfiles_usuarios WHERE id = ?";
        PerfilUsuarioVO perfil = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                perfil = mapearPerfil(rs);
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener perfil por ID: " + e.getMessage());
        }

        return perfil;
    }
    
    @Override
    public PerfilUsuarioVO obtenerPerfilPorProductoId(int productoId) {
        LogManager.debug("Obteniendo perfil por producto ID: " + productoId);

        String sql = """
            SELECT p.*
            FROM perfiles_usuarios p,
                 TABLE(p.productos) prod
            WHERE DEREF(VALUE(prod)).id = ?
        """;

        PerfilUsuarioVO perfil = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                perfil = mapearPerfil(rs);
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener perfil por producto ID: " + e.getMessage());
        }

        return perfil;
    }

    @Override
    public void agregarVendedorFavorito(int idPerfilActual, int idPerfilFavorito) {
        LogManager.debug("Agregando perfil " + idPerfilFavorito + " a favoritos del perfil " + idPerfilActual);

        String sql = """
            UPDATE perfiles_usuarios p
            SET vendedores_favoritos = vendedores_favoritos MULTISET UNION DISTINCT
                CAST( MULTISET(
                    SELECT REF(f)
                    FROM perfiles_usuarios f
                    WHERE f.id = ?
                ) AS lista_perfiles_typ )
            WHERE p.id = ?
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPerfilFavorito);
            stmt.setInt(2, idPerfilActual);

            int filasActualizadas = stmt.executeUpdate();

            if (filasActualizadas == 0) {
                AlertaSistema.error("No se actualizó ningún perfil. Verifica los IDs.");
            } else {
                AlertaSistema.info("Perfil agregado exitosamente a favoritos.");
            }

        } catch (SQLException e) {
            AlertaSistema.error("Error al agregar vendedor a favoritos: " + e.getMessage());
        }
    }
    
@Override
public void eliminarVendedorFavorito(int idPerfilActual, int idPerfilEliminar) {
    LogManager.debug("Eliminando perfil " + idPerfilEliminar + " de favoritos del perfil " + idPerfilActual);

    String plsql = """
        DECLARE
            ref_a_eliminar REF perfil_usuario_typ;
            favoritos_actualizados lista_perfiles_typ;
        BEGIN
            -- Obtener el REF del perfil a eliminar
            SELECT REF(f) INTO ref_a_eliminar
            FROM perfiles_usuarios f
            WHERE f.id = ?;

            -- Obtener la nueva colección sin ese perfil
            SELECT CAST(
                MULTISET(
                    SELECT elemento
                    FROM (
                        SELECT COLUMN_VALUE AS elemento
                        FROM TABLE(
                            SELECT p.vendedores_favoritos
                            FROM perfiles_usuarios p
                            WHERE p.id = ?
                        )
                    )
                    WHERE elemento != ref_a_eliminar
                ) AS lista_perfiles_typ
            )
            INTO favoritos_actualizados
            FROM DUAL;

            -- Actualizar la colección
            UPDATE perfiles_usuarios
            SET vendedores_favoritos = favoritos_actualizados
            WHERE id = ?;
        END;
    """;

    try (Connection conn = DatabaseManager.getConnection();
         CallableStatement stmt = conn.prepareCall(plsql)) {

        stmt.setInt(1, idPerfilEliminar);  // para SELECT REF(f)
        stmt.setInt(2, idPerfilActual);    // para vendedores_favoritos
        stmt.setInt(3, idPerfilActual);    // para UPDATE

        stmt.execute();
        AlertaSistema.info("Perfil eliminado exitosamente de favoritos.");

    } catch (SQLException e) {
        AlertaSistema.error("Error al eliminar vendedor de favoritos: " + e.getMessage());
    }
}

    @Override
    public List<PerfilUsuarioVO> obtenerVendedoresFavoritos(int perfilId) {
        LogManager.debug("Obteniendo vendedores favoritos del perfil ID: " + perfilId);

        String sql = """
            SELECT column_value
            FROM TABLE(
                SELECT vendedores_favoritos
                FROM perfiles_usuarios
                WHERE id = ?
            )
        """;

        List<PerfilUsuarioVO> lista = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, perfilId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ref refPerfil = rs.getRef(1);  // Obtener el REF del perfil

                // Obtener los datos completos del perfil usando el REF
                PreparedStatement stmtPerfil = conn.prepareStatement("SELECT * FROM perfiles_usuarios p WHERE REF(p) = ?");
                stmtPerfil.setRef(1, refPerfil);
                ResultSet rsPerfil = stmtPerfil.executeQuery();

                if (rsPerfil.next()) {
                    PerfilUsuarioVO perfil = mapearPerfil(rsPerfil);
                    lista.add(perfil);
                }
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener vendedores favoritos: " + e.getMessage());
        }

        return lista;
    }
}
