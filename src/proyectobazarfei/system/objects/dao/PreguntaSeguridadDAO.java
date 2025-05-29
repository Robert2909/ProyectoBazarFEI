package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.PreguntaSeguridadVO;
import java.util.List;

public interface PreguntaSeguridadDAO {
    List<PreguntaSeguridadVO> obtenerTodasLasPreguntas();
    PreguntaSeguridadVO obtenerPreguntaPorId(int id);
    PreguntaSeguridadVO obtenerPreguntaAleatoria();
}
