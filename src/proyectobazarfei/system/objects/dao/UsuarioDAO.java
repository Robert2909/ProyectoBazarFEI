package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.UsuarioVO;
import java.util.List;

public interface UsuarioDAO {
    UsuarioVO registrarUsuario(UsuarioVO usuario);
    UsuarioVO obtenerUsuarioPorCorreo(String correo);
    UsuarioVO obtenerUsuarioPorId(int id);
    List<UsuarioVO> obtenerTodosLosUsuarios();
    void actualizarUsuario(UsuarioVO usuario);
    void eliminarUsuario(int id);
    UsuarioVO iniciarSesion(String correo, String contrasena);
    UsuarioVO actualizarContrasena(String correo, String nuevaContrasena);
    UsuarioVO obtenerUsuarioPorPerfilId(int perfilId);
}
