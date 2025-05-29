package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.ChatVO;
import java.util.List;

public interface ChatDAO {
    void crearChat(ChatVO chat);
    ChatVO obtenerChatPorId(int id);
    List<ChatVO> obtenerChatsPorPerfilId(int perfilId);
    void agregarMensajeAlChat(int chatId, String mensaje);
}
