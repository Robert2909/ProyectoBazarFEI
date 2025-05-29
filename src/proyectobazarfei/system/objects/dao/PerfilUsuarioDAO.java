package proyectobazarfei.system.objects.dao;

import java.util.List;
import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;

public interface PerfilUsuarioDAO {
    PerfilUsuarioVO obtenerPerfilPorUsuarioId(int usuarioId);
    void actualizarPerfil(PerfilUsuarioVO perfil);
    void crearPerfil(PerfilUsuarioVO perfil);
    void eliminarPerfil(int perfilId);
    PerfilUsuarioVO obtenerPerfilPorId(int id);
    PerfilUsuarioVO obtenerPerfilPorProductoId(int productoId);
    void agregarVendedorFavorito(int idPerfilActual, int idPerfilFavorito);
    void eliminarVendedorFavorito(int idPerfilActual, int idPerfilEliminar);
    List<PerfilUsuarioVO> obtenerVendedoresFavoritos(int perfilId);
}
