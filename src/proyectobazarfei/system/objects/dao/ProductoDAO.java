package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.ProductoVO;
import java.util.List;

public interface ProductoDAO {
    void crearProducto(ProductoVO producto);
    ProductoVO obtenerProductoPorId(int id);
    List<ProductoVO> obtenerProductosPorPerfilId(int perfilId);
    List<ProductoVO> buscarProductos(String criterioBusqueda);
    void actualizarProducto(ProductoVO producto);
    void eliminarProducto(int id);
    List<ProductoVO> obtenerTodosLosProductos();
    List<ProductoVO> buscarProductosConFiltros(String texto, String categoria, Double precioMin, Double precioMax);
}
