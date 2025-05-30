package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.ChatVO;
import java.util.List;
import proyectobazarfei.system.objects.vo.MensajeVO;

public interface ChatDAO {
    void crearChat(ChatVO chat);
    ChatVO obtenerChatPorId(int id);
    List<ChatVO> obtenerChatsPorPerfilId(int perfilId);
    void agregarMensajeAlChat(int chatId, MensajeVO mensaje);
    List<MensajeVO> obtenerMensajesPorChatId(int chatId); 
}
