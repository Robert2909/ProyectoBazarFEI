package proyectobazarfei.system.objects.daoIMPL;

import proyectobazarfei.system.objects.dao.ChatDAO;
import proyectobazarfei.system.objects.vo.ChatVO;
import proyectobazarfei.system.objects.vo.MensajeVO;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;
import proyectobazarfei.system.methods.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.REF;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import proyectobazarfei.system.methods.AlertaSistema;
import proyectobazarfei.system.methods.LogManager;
import proyectobazarfei.system.objects.dao.PerfilUsuarioDAO;
import proyectobazarfei.system.objects.dao.UsuarioDAO;
import proyectobazarfei.system.objects.vo.UsuarioVO;

public class ChatDAOImpl implements ChatDAO {

    @Override
    public void crearChat(ChatVO chat) {
        LogManager.debug("Creando un chat...");
        String sql = "INSERT INTO chats (id, vendedor, comprador) VALUES (?, (SELECT REF(p) FROM perfiles_usuarios p WHERE p.id = ?), (SELECT REF(p) FROM perfiles_usuarios p WHERE p.id = ?))";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, chat.getId());
            stmt.setInt(2, chat.getVendedor().getId());
            stmt.setInt(3, chat.getComprador().getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            LogManager.error("Error al crear chat: " + e.getMessage());
        }
    }

    @Override
    public ChatVO obtenerChatPorId(int id) {
        LogManager.debug("Obteniendo chat por id: " + id);

        String sql = """
            SELECT c.id, DEREF(vendedor).id AS id_vendedor, DEREF(comprador).id AS id_comprador
            FROM chats c
            WHERE c.id = ?
        """;

        ChatVO chat = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                PerfilUsuarioVO vendedor = new PerfilUsuarioVO();
                PerfilUsuarioVO comprador = new PerfilUsuarioVO();

                vendedor.setId(rs.getInt("id_vendedor"));
                comprador.setId(rs.getInt("id_comprador"));

                UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
                vendedor.setDatosUsuario(usuarioDAO.obtenerUsuarioPorPerfilId(vendedor.getId()));
                comprador.setDatosUsuario(usuarioDAO.obtenerUsuarioPorPerfilId(comprador.getId()));

                chat = new ChatVO();
                chat.setId(rs.getInt("id"));
                chat.setVendedor(vendedor);
                chat.setComprador(comprador);
                chat.setMensajes(obtenerMensajesDelChat(chat.getId()));
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener chat: " + e.getMessage());
        }

        return chat;
    }

    @Override
    public List<ChatVO> obtenerChatsPorPerfilId(int perfilId) {
        LogManager.debug("Obteniendo chats por el id del perfil: " + perfilId);

        String sql = """
            SELECT c.id, DEREF(vendedor).id AS id_vendedor, DEREF(comprador).id AS id_comprador
            FROM chats c
            WHERE DEREF(vendedor).id = ? OR DEREF(comprador).id = ?
        """;

        List<ChatVO> lista = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, perfilId);
            stmt.setInt(2, perfilId);
            ResultSet rs = stmt.executeQuery();

            UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

            while (rs.next()) {
                ChatVO chat = new ChatVO();

                PerfilUsuarioVO vendedor = new PerfilUsuarioVO();
                PerfilUsuarioVO comprador = new PerfilUsuarioVO();

                vendedor.setId(rs.getInt("id_vendedor"));
                comprador.setId(rs.getInt("id_comprador"));

                vendedor.setDatosUsuario(usuarioDAO.obtenerUsuarioPorPerfilId(vendedor.getId()));
                comprador.setDatosUsuario(usuarioDAO.obtenerUsuarioPorPerfilId(comprador.getId()));

                chat.setId(rs.getInt("id"));
                chat.setVendedor(vendedor);
                chat.setComprador(comprador);
                chat.setMensajes(obtenerMensajesDelChat(chat.getId()));

                lista.add(chat);
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener chats por perfil: " + e.getMessage());
            AlertaSistema.error("No se pudieron obtener tus chats.");
        }

        return lista;
    }

    @Override
    public void agregarMensajeAlChat(int chatId, MensajeVO mensaje) {
        Connection conn = null;
        PreparedStatement stmt = null;

        String sql = """
            UPDATE chats
            SET mensajes = mensajes MULTISET UNION DISTINCT ?
            WHERE id = ?
        """;

        try {
            conn = DatabaseManager.getConnection();
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);

            // Obtener el REF del perfil remitente
            PreparedStatement refStmt = conn.prepareStatement("SELECT REF(p) FROM perfiles_usuarios p WHERE p.id = ?");
            refStmt.setInt(1, mensaje.getRemitente().getId());
            ResultSet refRs = refStmt.executeQuery();

            REF remitenteRef = null;
            if (refRs.next()) {
                remitenteRef = (REF) refRs.getObject(1);
            }
            refRs.close();
            refStmt.close();

            if (remitenteRef == null) {
                throw new SQLException("No se pudo obtener el REF del remitente.");
            }

            // Crear STRUCT para mensaje_typ (con remitente incluido)
            StructDescriptor structDesc = StructDescriptor.createDescriptor("MENSAJE_TYP", oracleConn);
            Object[] attrs = {
                new java.sql.Date(mensaje.getFechaEnvio().getTime()),
                mensaje.getHoraEnvio(),
                mensaje.getTexto(),
                remitenteRef
            };
            STRUCT mensajeStruct = new STRUCT(structDesc, oracleConn, attrs);

            // Crear ARRAY para lista_mensajes_typ
            ArrayDescriptor arrayDesc = ArrayDescriptor.createDescriptor("LISTA_MENSAJES_TYP", oracleConn);
            STRUCT[] mensajesArray = new STRUCT[] { mensajeStruct };
            ARRAY mensajesCollection = new ARRAY(arrayDesc, oracleConn, mensajesArray);

            // Ejecutar actualización
            stmt = conn.prepareStatement(sql);
            stmt.setArray(1, mensajesCollection);
            stmt.setInt(2, chatId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LogManager.error("Error al agregar mensaje al chat ID " + chatId + ": " + e.getMessage());
            AlertaSistema.error("No se pudo enviar el mensaje.");
        } finally {
            DatabaseManager.cerrarConexion(conn);
        }
    }


    @Override
    public List<MensajeVO> obtenerMensajesPorChatId(int chatId) {
        List<MensajeVO> mensajes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = """
            SELECT m.fecha_envio, m.hora_envio, m.texto, m.remitente
            FROM chats c, TABLE(c.mensajes) m
            WHERE c.id = ?
            ORDER BY m.fecha_envio, m.hora_envio
        """;

        try {
            conn = DatabaseManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, chatId);
            rs = stmt.executeQuery();

            UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

            while (rs.next()) {
                Date fecha = rs.getDate("fecha_envio");
                String hora = rs.getString("hora_envio");
                String texto = rs.getString("texto");
                int remitenteId = rs.getInt("remitente");

                // Obtener perfil completo del remitente
                PerfilUsuarioVO perfilRemitente = new PerfilUsuarioVO();
                perfilRemitente.setId(remitenteId);
                perfilRemitente.setDatosUsuario(usuarioDAO.obtenerUsuarioPorPerfilId(remitenteId));

                MensajeVO mensaje = new MensajeVO();
                mensaje.setFechaEnvio(fecha);
                mensaje.setHoraEnvio(hora);
                mensaje.setTexto(texto);
                mensaje.setRemitente(perfilRemitente);

                mensajes.add(mensaje);
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener los mensajes del chat ID " + chatId + ": " + e.getMessage());
            AlertaSistema.error("No se pudieron cargar los mensajes.");
        } finally {
            DatabaseManager.cerrarConexion(conn);
        }

        return mensajes;
    }



    private ChatVO mapearChat(ResultSet rs) throws SQLException {
        LogManager.debug("Mapeando chat...");
        ChatVO c = new ChatVO();
        PerfilUsuarioVO v = new PerfilUsuarioVO();
        PerfilUsuarioVO comp = new PerfilUsuarioVO();

        c.setId(rs.getInt("id"));
        v.setId(rs.getInt("vendedor"));
        comp.setId(rs.getInt("comprador"));
        c.setVendedor(v);
        c.setComprador(comp);
        return c;
    }

private List<MensajeVO> obtenerMensajesDelChat(int chatId) {
    LogManager.debug("Obteniendo mensajes del chat: " + chatId);

    String sql = """
        SELECT m.fecha_envio, m.hora_envio, m.texto, DEREF(m.remitente).id AS remitente_id
        FROM chats c, TABLE(c.mensajes) m
        WHERE c.id = ?
        ORDER BY m.fecha_envio, m.hora_envio
    """;

    List<MensajeVO> mensajes = new ArrayList<>();

    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, chatId);
        ResultSet rs = stmt.executeQuery();

        PerfilUsuarioDAO perfilDAO = new PerfilUsuarioDAOImpl();

        while (rs.next()) {
            Date fecha = rs.getDate("fecha_envio");
            String hora = rs.getString("hora_envio");
            String texto = rs.getString("texto");
            int remitenteId = rs.getInt("remitente_id");

            PerfilUsuarioVO remitente = perfilDAO.obtenerPerfilPorId(remitenteId);
            MensajeVO mensaje = new MensajeVO(texto, fecha, hora, remitente);
            mensajes.add(mensaje);
        }

    } catch (SQLException e) {
        LogManager.error("Error al obtener mensajes del chat: " + e.getMessage());
    }

    return mensajes;
}


}
