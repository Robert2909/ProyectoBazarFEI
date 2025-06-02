package proyectobazarfei.system.objects.dao;

import proyectobazarfei.system.objects.vo.ProductoVO;

import java.util.List;

/**
 * Interfaz DAO para gestionar operaciones sobre productos en el sistema.
 */
public interface ProductoDAO {

    /**
     * Crea un nuevo producto en la base de datos.
     *
     * @param producto Objeto ProductoVO con los datos del producto
     * @return true si se creó correctamente, false si falló
     */
    boolean crearProducto(ProductoVO producto);

    /**
     * Obtiene un producto a partir de su ID.
     *
     * @param id ID del producto
     * @return ProductoVO correspondiente o null si no existe
     */
    ProductoVO obtenerProductoPorId(int id);

    /**
     * Obtiene todos los productos asociados a un perfil específico.
     *
     * @param perfilId ID del perfil
     * @return Lista de productos publicados por ese perfil
     */
    List<ProductoVO> obtenerProductosPorPerfilId(int perfilId);

    /**
     * Busca productos cuyo nombre o descripción coincidan con un criterio.
     *
     * @param criterioBusqueda Texto a buscar
     * @return Lista de productos que coinciden con el criterio
     */
    List<ProductoVO> buscarProductos(String criterioBusqueda);

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param producto Objeto con los nuevos datos del producto
     */
    void actualizarProducto(ProductoVO producto);

    /**
     * Elimina un producto por su ID.
     *
     * @param id ID del producto a eliminar
     */
    void eliminarProducto(int id);

    /**
     * Obtiene todos los productos disponibles en el sistema.
     *
     * @return Lista de todos los productos
     */
    List<ProductoVO> obtenerTodosLosProductos();

    /**
     * Busca productos aplicando múltiples filtros.
     *
     * @param texto      Texto en nombre o descripción
     * @param categoria  Categoría del producto
     * @param precioMin  Precio mínimo
     * @param precioMax  Precio máximo
     * @return Lista de productos que cumplen con los filtros
     */
    List<ProductoVO> buscarProductosConFiltros(String texto, String categoria, Double precioMin, Double precioMax);
}
