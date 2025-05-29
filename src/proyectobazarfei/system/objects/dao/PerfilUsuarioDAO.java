package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.PerfilUsuarioVO;

public interface PerfilUsuarioDAO {
    PerfilUsuarioVO obtenerPerfilPorUsuarioId(int usuarioId);
    void actualizarPerfil(PerfilUsuarioVO perfil);
    void crearPerfil(PerfilUsuarioVO perfil);
    void eliminarPerfil(int perfilId);
    PerfilUsuarioVO obtenerPerfilPorId(int id);
    PerfilUsuarioVO obtenerPerfilPorProductoId(int productoId);
}
