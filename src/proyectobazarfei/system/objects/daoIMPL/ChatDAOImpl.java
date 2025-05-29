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
import proyectobazarfei.system.methods.LogManager;

public class ChatDAOImpl implements ChatDAO {

    @Override
    public void crearChat(ChatVO chat) {
        LogManager.debug("Creando un chat...");
        String sql = "INSERT INTO chats (id, vendedor, comprador) VALUES (?, ?, ?)";

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
        String sql = "SELECT * FROM chats WHERE id = ?";
        ChatVO chat = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                chat = mapearChat(rs);
                chat.setMensajes(obtenerMensajesDelChat(id));
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener chat: " + e.getMessage());
        }

        return chat;
    }

    @Override
    public List<ChatVO> obtenerChatsPorPerfilId(int perfilId) {
        LogManager.debug("Obteniendo chats por el id del perfil: " + perfilId);
        String sql = "SELECT * FROM chats WHERE vendedor = ? OR comprador = ?";
        List<ChatVO> lista = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, perfilId);
            stmt.setInt(2, perfilId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ChatVO chat = mapearChat(rs);
                chat.setMensajes(obtenerMensajesDelChat(chat.getId()));
                lista.add(chat);
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener chats por perfil: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public void agregarMensajeAlChat(int chatId, String mensaje) {
        LogManager.debug("Agregando al chat '" + chatId + "' el mensaje '" + mensaje + "'");
        String sql = "INSERT INTO TABLE(SELECT mensajes FROM chats WHERE id = ?) VALUES (?, ?, ?)";
        Date ahora = new Date();
        java.text.SimpleDateFormat sdfHora = new java.text.SimpleDateFormat("HH:mm:ss");

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, chatId);
            stmt.setDate(2, new java.sql.Date(ahora.getTime()));
            stmt.setString(3, sdfHora.format(ahora));
            stmt.setString(4, mensaje);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LogManager.error("Error al agregar mensaje al chat: " + e.getMessage());
        }
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
        String sql = "SELECT * FROM TABLE(SELECT mensajes FROM chats WHERE id = ?)";
        List<MensajeVO> mensajes = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, chatId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MensajeVO m = new MensajeVO();
                m.setFechaEnvio(rs.getDate("fecha_envio"));
                m.setHoraEnvio(rs.getString("hora_envio"));
                m.setTexto(rs.getString("texto"));
                mensajes.add(m);
            }

        } catch (SQLException e) {
            LogManager.error("Error al obtener mensajes del chat: " + e.getMessage());
        }

        return mensajes;
    }
} 