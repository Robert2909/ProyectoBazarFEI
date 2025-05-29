package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.ArticuloAyudaVO;
import java.util.List;

public interface ArticuloAyudaDAO {
    List<ArticuloAyudaVO> obtenerTodosLosArticulos();
    ArticuloAyudaVO obtenerArticuloPorId(int id);
}
