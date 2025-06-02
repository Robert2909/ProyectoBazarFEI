package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.TransaccionVO;

import java.util.List;

/**
 * Interfaz DAO para manejar operaciones relacionadas con transacciones en el sistema.
 */
public interface TransaccionDAO {

    /**
     * Registra una nueva transacción en la base de datos.
     *
     * @param transaccion Objeto con la información de la transacción
     */
    void registrarTransaccion(TransaccionVO transaccion);

    /**
     * Obtiene todas las transacciones activas asociadas a un perfil.
     *
     * @param perfilId ID del perfil
     * @return Lista de transacciones activas
     */
    List<TransaccionVO> obtenerProductosActivosPorPerfilId(int perfilId);

    /**
     * Obtiene todas las compras realizadas por un perfil.
     *
     * @param perfilId ID del perfil comprador
     * @return Lista de transacciones de compra
     */
    List<TransaccionVO> obtenerComprasPorPerfilId(int perfilId);

    /**
     * Obtiene todas las ventas realizadas por un perfil.
     *
     * @param perfilId ID del perfil vendedor
     * @return Lista de transacciones de venta
     */
    List<TransaccionVO> obtenerVentasPorPerfilId(int perfilId);

    /**
     * Actualiza el estado de una transacción (ej. "Completada", "Cancelada").
     *
     * @param transaccionId ID de la transacción
     * @param nuevoEstado   Nuevo estado a asignar
     */
    void actualizarEstadoTransaccion(int transaccionId, String nuevoEstado);

    /**
     * Registra una calificación y comentario sobre una transacción completada.
     *
     * @param transaccionId ID de la transacción
     * @param calificacion  Valor numérico de la calificación
     * @param comentario    Comentario del usuario
     */
    void calificarTransaccion(int transaccionId, int calificacion, String comentario);

    /**
     * Obtiene los detalles de una transacción específica por su ID.
     *
     * @param id ID de la transacción
     * @return Objeto TransaccionVO o null si no se encuentra
     */
    TransaccionVO obtenerTransaccionPorId(int id);
}
