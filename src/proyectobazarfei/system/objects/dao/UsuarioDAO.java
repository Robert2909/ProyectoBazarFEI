package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.UsuarioVO;

import java.util.List;

/**
 * Interfaz DAO para gestionar las operaciones relacionadas con usuarios.
 */
public interface UsuarioDAO {

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param usuario Objeto UsuarioVO con los datos del nuevo usuario
     * @return UsuarioVO registrado (puede incluir ID u otros datos generados)
     */
    UsuarioVO registrarUsuario(UsuarioVO usuario);

    /**
     * Obtiene un usuario por su dirección de correo electrónico.
     *
     * @param correo Correo del usuario
     * @return Objeto UsuarioVO correspondiente o null si no existe
     */
    UsuarioVO obtenerUsuarioPorCorreo(String correo);

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario
     * @return Objeto UsuarioVO correspondiente o null si no existe
     */
    UsuarioVO obtenerUsuarioPorId(int id);

    /**
     * Devuelve todos los usuarios registrados en el sistema.
     *
     * @return Lista de objetos UsuarioVO
     */
    List<UsuarioVO> obtenerTodosLosUsuarios();

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param usuario Objeto UsuarioVO con los datos actualizados
     */
    void actualizarUsuario(UsuarioVO usuario);

    /**
     * Elimina un usuario de la base de datos por su ID.
     *
     * @param id ID del usuario a eliminar
     */
    void eliminarUsuario(int id);

    /**
     * Inicia sesión con el correo y contraseña proporcionados.
     *
     * @param correo     Correo del usuario
     * @param contrasena Contraseña del usuario
     * @return UsuarioVO si las credenciales son válidas, null en caso contrario
     */
    UsuarioVO iniciarSesion(String correo, String contrasena);

    /**
     * Actualiza la contraseña de un usuario.
     *
     * @param correo           Correo del usuario
     * @param nuevaContrasena  Nueva contraseña
     * @return UsuarioVO actualizado o null si el usuario no existe
     */
    UsuarioVO actualizarContrasena(String correo, String nuevaContrasena);

    /**
     * Obtiene un usuario a partir del ID de su perfil asociado.
     *
     * @param perfilId ID del perfil
     * @return Objeto UsuarioVO correspondiente o null si no existe
     */
    UsuarioVO obtenerUsuarioPorPerfilId(int perfilId);
}
