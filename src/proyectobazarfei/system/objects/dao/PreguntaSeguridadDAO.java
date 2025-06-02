package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.PreguntaSeguridadVO;

import java.util.List;

/**
 * Interfaz DAO para gestionar las preguntas de seguridad del sistema.
 * Proporciona métodos para consultar preguntas almacenadas en la base de datos.
 */
public interface PreguntaSeguridadDAO {

    /**
     * Obtiene todas las preguntas de seguridad disponibles.
     *
     * @return Lista de objetos PreguntaSeguridadVO
     */
    List<PreguntaSeguridadVO> obtenerTodasLasPreguntas();

    /**
     * Obtiene una pregunta de seguridad por su identificador.
     *
     * @param id ID de la pregunta
     * @return Objeto PreguntaSeguridadVO correspondiente o null si no existe
     */
    PreguntaSeguridadVO obtenerPreguntaPorId(int id);

    /**
     * Obtiene una pregunta aleatoria de seguridad.
     *
     * @return PreguntaSeguridadVO seleccionada aleatoriamente
     */
    PreguntaSeguridadVO obtenerPreguntaAleatoria();
}
