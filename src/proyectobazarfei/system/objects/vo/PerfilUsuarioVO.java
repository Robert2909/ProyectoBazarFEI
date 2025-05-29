package proyectobazarfei.system.objects.vo;

import java.util.List;

public class PerfilUsuarioVO {
    private int id;
    private UsuarioVO datosUsuario;
    private String fotoPerfil;
    private String descripcion;
    private List<ProductoVO> productos;
    private double calificacionPromedio;
    private List<PerfilUsuarioVO> vendedoresFavoritos;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public UsuarioVO getDatosUsuario() { return datosUsuario; }
    public void setDatosUsuario(UsuarioVO datosUsuario) { this.datosUsuario = datosUsuario; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<ProductoVO> getProductos() { return productos; }
    public void setProductos(List<ProductoVO> productos) { this.productos = productos; }

    public double getCalificacionPromedio() { return calificacionPromedio; }
    public void setCalificacionPromedio(double calificacionPromedio) { this.calificacionPromedio = calificacionPromedio; }

    public List<PerfilUsuarioVO> getVendedoresFavoritos() { return vendedoresFavoritos; }
    public void setVendedoresFavoritos(List<PerfilUsuarioVO> vendedoresFavoritos) { this.vendedoresFavoritos = vendedoresFavoritos; }
}