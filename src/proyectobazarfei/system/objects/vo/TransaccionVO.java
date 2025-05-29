package proyectobazarfei.system.objects.vo;

import java.util.Date;

public class TransaccionVO {
    private int id;
    private ProductoVO producto;
    private Date fechaCreacion;
    private String horaCreacion;
    private String estado;
    private String metodoPago;
    private Integer calificacion;
    private String comentario;
    private PerfilUsuarioVO vendedor;
    private PerfilUsuarioVO comprador;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public ProductoVO getProducto() { return producto; }
    public void setProducto(ProductoVO producto) { this.producto = producto; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getHoraCreacion() { return horaCreacion; }
    public void setHoraCreacion(String horaCreacion) { this.horaCreacion = horaCreacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public PerfilUsuarioVO getVendedor() { return vendedor; }
    public void setVendedor(PerfilUsuarioVO vendedor) { this.vendedor = vendedor; }

    public PerfilUsuarioVO getComprador() { return comprador; }
    public void setComprador(PerfilUsuarioVO comprador) { this.comprador = comprador; }
}