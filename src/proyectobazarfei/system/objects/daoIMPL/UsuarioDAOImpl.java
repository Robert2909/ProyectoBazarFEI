package proyectobazarfei.system.objects.daoIMPL;

import proyectobazarfei.system.objects.dao.UsuarioDAO;
import proyectobazarfei.system.objects.vo.UsuarioVO;
import proyectobazarfei.system.methods.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.objects.vo.PreguntaSeguridadVO;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public UsuarioVO registrarUsuario(UsuarioVO usuario) {
        LogManager.debug("Registrando usuario: " + usuario.toString());
        String sql = "INSERT INTO usuarios (nombre, apodo, correo, contrasena, pregunta_seguridad, respuesta_seguridad) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {

            // Obtener REF de la pregunta de seguridad
            PreparedStatement refStmt = conn.prepareStatement("SELECT REF(p) FROM preguntas_seguridad p WHERE p.id = ?");
            refStmt.setInt(1, usuario.getPreguntaSeguridad().getId());
            ResultSet refRs = refStmt.executeQuery();

            Ref ref = null;
            if (refRs.next()) {
                ref = refRs.getRef(1);
            } else {
                AlertaSistema.error("No se encontró la pregunta de seguridad especificada.");
                return null;
            }

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApodo());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getContrasena());
            stmt.setRef(5, ref);
            stmt.setString(6, usuario.getRespuestaSeguridad());

            int filas = stmt.executeUpdate();

            if (filas > 0) {
                PreparedStatement idStmt = conn.prepareStatement("SELECT MAX(id) FROM usuarios WHERE correo = ?");
                idStmt.setString(1, usuario.getCorreo());
                ResultSet idRs = idStmt.executeQuery();
                if (idRs.next()) {
                    usuario.setId(idRs.getInt(1));
                }
                return usuario;
            }

        } catch (SQLException e) {
            AlertaSistema.error("Error al registrar usuario.");
        }

        return null;
    }

    @Override
    public UsuarioVO obtenerUsuarioPorCorreo(String correo) {
        LogManager.debug("Obteniendo usuario por el correo: " + correo);
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        UsuarioVO usuario = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = mapearUsuario(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por correo: " + e.getMessage());
        }

        return usuario;
    }

    @Override
    public UsuarioVO obtenerUsuarioPorId(int id) {
        LogManager.debug("Obteniendo usuario por el id: " + id);
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        UsuarioVO usuario = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = mapearUsuario(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por ID: " + e.getMessage());
        }

        return usuario;
    }

    @Override
    public List<UsuarioVO> obtenerTodosLosUsuarios() {
        LogManager.debug("Obteniendo todos los usuarios...");
        String sql = "SELECT * FROM usuarios";
        List<UsuarioVO> lista = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public void actualizarUsuario(UsuarioVO usuario) {
        LogManager.debug("Actualizando usuario (sin cambiar pregunta seguridad): " + usuario.toString());
        String sql = "UPDATE usuarios SET nombre = ?, apodo = ?, correo = ?, contrasena = ?, respuesta_seguridad = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApodo());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getContrasena());
            stmt.setString(5, usuario.getRespuestaSeguridad());
            stmt.setInt(6, usuario.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            LogManager.error("Error al actualizar usuario (sin REF): " + e.getMessage());
            AlertaSistema.error("Error al actualizar el usuario.");
        }
    }

    @Override
    public void eliminarUsuario(int id) {
        LogManager.debug("Eliminando usuario con el id: " + id);
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
    }

    private UsuarioVO mapearUsuario(ResultSet rs) throws SQLException {
        LogManager.debug("Mapeando usuario...");
        UsuarioVO u = new UsuarioVO();
        
        u.setId(rs.getInt("id"));       
        u.setNombre(rs.getString("nombre"));        
        u.setApodo(rs.getString("apodo"));        
        u.setCorreo(rs.getString("correo"));        
        u.setContrasena(rs.getString("contrasena"));
        u.setRespuestaSeguridad(rs.getString("respuesta_seguridad"));
        
        Ref ref = rs.getRef("pregunta_seguridad");
        Connection conn = rs.getStatement().getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM preguntas_seguridad p WHERE REF(p) = ?");
        stmt.setRef(1, ref);
        ResultSet refRs = stmt.executeQuery();

        if (refRs.next()) {
            PreguntaSeguridadVO pregunta = new PreguntaSeguridadVO();
            pregunta.setId(refRs.getInt("id"));
            pregunta.setPregunta(refRs.getString("pregunta"));
            u.setPreguntaSeguridad(pregunta);
        }
        
        return u;
    }
    
    @Override
    public UsuarioVO iniciarSesion(String correo, String contrasena) {
        LogManager.debug("Iniciando sesión con correo: " + correo + " y contraseña: " + contrasena);
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";
        UsuarioVO usuario = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                usuario = mapearUsuario(rs);
            }
        } catch (SQLException e) {
            AlertaSistema.error("Error de conexión con la base de datos: UsuarioDAOImpl:iniciarSesion()");
        }

        return usuario;
    }
    
    @Override
    public UsuarioVO actualizarContrasena(String correo, String nuevaContrasena) {
        LogManager.debug("Actualizando contraseña :" + nuevaContrasena + " para el correo: " + correo);
        String sql = "UPDATE usuarios SET contrasena = ? WHERE correo = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevaContrasena);
            stmt.setString(2, correo);

            int filas = stmt.executeUpdate();

            if (filas > 0) {
                // Si la actualización fue exitosa, se regresa el usuario actualizado
                return obtenerUsuarioPorCorreo(correo);
            }

        } catch (SQLException e) {
            AlertaSistema.error("Error al actualizar la contraseña del usuario.");
        }

        return null;
    }

@Override
public UsuarioVO obtenerUsuarioPorPerfilId(int perfilId) {
    LogManager.debug("Obteniendo usuario por ID de perfil: " + perfilId);
    String sql = "SELECT datos_usuario FROM perfiles_usuarios WHERE id = ?";
    UsuarioVO usuario = null;

    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, perfilId);
        ResultSet rs = stmt.executeQuery();

        Ref refUsuario = null;
        if (rs.next()) {
            refUsuario = rs.getRef("datos_usuario");
        }

        if (refUsuario != null) {
            PreparedStatement usuarioStmt = conn.prepareStatement("SELECT * FROM usuarios u WHERE REF(u) = ?");
            usuarioStmt.setRef(1, refUsuario);
            ResultSet usuarioRs = usuarioStmt.executeQuery();

            if (usuarioRs.next()) {
                usuario = new UsuarioVO();
                usuario.setId(usuarioRs.getInt("id"));
                usuario.setNombre(usuarioRs.getString("nombre"));
                usuario.setApodo(usuarioRs.getString("apodo"));
                usuario.setCorreo(usuarioRs.getString("correo"));
                usuario.setContrasena(usuarioRs.getString("contrasena"));
                usuario.setRespuestaSeguridad(usuarioRs.getString("respuesta_seguridad"));

                Ref refPregunta = usuarioRs.getRef("pregunta_seguridad");
                if (refPregunta != null) {
                    PreparedStatement psPregunta = conn.prepareStatement("SELECT * FROM preguntas_seguridad p WHERE REF(p) = ?");
                    psPregunta.setRef(1, refPregunta);
                    ResultSet rsPregunta = psPregunta.executeQuery();

                    if (rsPregunta.next()) {
                        PreguntaSeguridadVO pregunta = new PreguntaSeguridadVO();
                        pregunta.setId(rsPregunta.getInt("id"));
                        pregunta.setPregunta(rsPregunta.getString("pregunta"));
                        usuario.setPreguntaSeguridad(pregunta);
                    }
                }
            }
        }

    } catch (SQLException e) {
        LogManager.error("Error al obtener usuario por perfil ID: " + e.getMessage());
    }

    return usuario;
}



}