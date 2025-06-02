package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.ChatVO;
import proyectobazarfei.system.objects.vo.MensajeVO;

import java.util.List;

/**
 * Interfaz DAO para manejar operaciones relacionadas con chats y mensajes.
 */
public interface ChatDAO {

    /**
     * Crea un nuevo chat en la base de datos.
     *
     * @param chat Objeto ChatVO que contiene la información del chat a crear
     */
    void crearChat(ChatVO chat);

    /**
     * Obtiene un chat específico a partir de su ID.
     *
     * @param id ID del chat
     * @return Objeto ChatVO correspondiente o null si no existe
     */
    ChatVO obtenerChatPorId(int id);

    /**
     * Devuelve todos los chats asociados a un perfil específico.
     *
     * @param perfilId ID del perfil participante en los chats
     * @return Lista de objetos ChatVO
     */
    List<ChatVO> obtenerChatsPorPerfilId(int perfilId);

    /**
     * Agrega un mensaje a un chat existente.
     *
     * @param chatId  ID del chat donde se agregará el mensaje
     * @param mensaje Objeto MensajeVO con los datos del mensaje
     */
    void agregarMensajeAlChat(int chatId, MensajeVO mensaje);

    /**
     * Obtiene todos los mensajes asociados a un chat específico.
     *
     * @param chatId ID del chat
     * @return Lista de objetos MensajeVO
     */
    List<MensajeVO> obtenerMensajesPorChatId(int chatId);
}
