package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;

import java.util.List;

/**
 * Interfaz DAO para gestionar operaciones relacionadas con los perfiles de usuario.
 */
public interface PerfilUsuarioDAO {

    /**
     * Obtiene un perfil de usuario a partir del ID del usuario.
     *
     * @param usuarioId ID del usuario
     * @return Objeto PerfilUsuarioVO correspondiente o null si no existe
     */
    PerfilUsuarioVO obtenerPerfilPorUsuarioId(int usuarioId);

    /**
     * Actualiza la información de un perfil existente.
     *
     * @param perfil Objeto con los nuevos datos del perfil
     */
    void actualizarPerfil(PerfilUsuarioVO perfil);

    /**
     * Crea un nuevo perfil de usuario en la base de datos.
     *
     * @param perfil Objeto con los datos del perfil a crear
     */
    void crearPerfil(PerfilUsuarioVO perfil);

    /**
     * Elimina un perfil de usuario por su ID.
     *
     * @param perfilId ID del perfil a eliminar
     */
    void eliminarPerfil(int perfilId);

    /**
     * Obtiene un perfil de usuario por su ID.
     *
     * @param id ID del perfil
     * @return Objeto PerfilUsuarioVO correspondiente o null si no existe
     */
    PerfilUsuarioVO obtenerPerfilPorId(int id);

    /**
     * Obtiene el perfil del vendedor de un producto determinado.
     *
     * @param productoId ID del producto
     * @return Perfil del vendedor asociado a ese producto
     */
    PerfilUsuarioVO obtenerPerfilPorProductoId(int productoId);

    /**
     * Agrega un vendedor a la lista de favoritos del perfil actual.
     *
     * @param idPerfilActual   ID del perfil que agrega al favorito
     * @param idPerfilFavorito ID del perfil a agregar como favorito
     */
    void agregarVendedorFavorito(int idPerfilActual, int idPerfilFavorito);

    /**
     * Elimina un vendedor de la lista de favoritos del perfil actual.
     *
     * @param idPerfilActual   ID del perfil que elimina al favorito
     * @param idPerfilEliminar ID del perfil que será eliminado de favoritos
     */
    void eliminarVendedorFavorito(int idPerfilActual, int idPerfilEliminar);

    /**
     * Obtiene la lista de vendedores favoritos de un perfil.
     *
     * @param perfilId ID del perfil
     * @return Lista de perfiles marcados como favoritos
     */
    List<PerfilUsuarioVO> obtenerVendedoresFavoritos(int perfilId);
}
