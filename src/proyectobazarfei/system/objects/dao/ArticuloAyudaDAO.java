package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.ArticuloAyudaVO;
import java.util.List;

/**
 * Interfaz DAO para manejar operaciones relacionadas con artículos de ayuda.
 */
public interface ArticuloAyudaDAO {

    /**
     * Obtiene todos los artículos de ayuda disponibles en la base de datos.
     *
     * @return Lista de objetos ArticuloAyudaVO
     */
    List<ArticuloAyudaVO> obtenerTodosLosArticulos();

    /**
     * Busca un artículo de ayuda específico por su ID.
     *
     * @param id Identificador del artículo
     * @return Objeto ArticuloAyudaVO correspondiente o null si no existe
     */
    ArticuloAyudaVO obtenerArticuloPorId(int id);
}
