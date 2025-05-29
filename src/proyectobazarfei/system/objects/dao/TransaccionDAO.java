package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.TransaccionVO;
import java.util.List;

public interface TransaccionDAO {
    void registrarTransaccion(TransaccionVO transaccion);
    List<TransaccionVO> obtenerProductosActivosPorPerfilId(int perfilId);
    List<TransaccionVO> obtenerComprasPorPerfilId(int perfilId);
    List<TransaccionVO> obtenerVentasPorPerfilId(int perfilId);
    void actualizarEstadoTransaccion(int transaccionId, String nuevoEstado);
    void calificarTransaccion(int transaccionId, int calificacion, String comentario);
    TransaccionVO obtenerTransaccionPorId(int id);
}
