package proyectobazarfei.system.objects.vo;

import java.util.Date;

public class TransaccionVO {
    private int id;
    private ProductoVO producto;
    private String metodoPagoElegido;
    private String metodoEntregaElegido;
    private String lugarEntrega;  // Puede ser null si "Voy a recogerlo"
    private String horaEntrega;   // Puede ser null si "Voy a recogerlo"
    private Date fechaCreacion;
    private String horaCreacion;
    private String estado;
    private Integer calificacion;
    private String comentario;
    private PerfilUsuarioVO vendedor;
    private PerfilUsuarioVO comprador;

    public TransaccionVO() {}

    // Getters y Setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public ProductoVO getProducto() { return producto; }
    public void setProducto(ProductoVO producto) { this.producto = producto; }

    public String getMetodoPagoElegido() { return metodoPagoElegido; }
    public void setMetodoPagoElegido(String metodoPagoElegido) { this.metodoPagoElegido = metodoPagoElegido; }

    public String getMetodoEntregaElegido() { return metodoEntregaElegido; }
    public void setMetodoEntregaElegido(String metodoEntregaElegido) { this.metodoEntregaElegido = metodoEntregaElegido; }

    public String getLugarEntrega() { return lugarEntrega; }
    public void setLugarEntrega(String lugarEntrega) { this.lugarEntrega = lugarEntrega; }

    public String getHoraEntrega() { return horaEntrega; }
    public void setHoraEntrega(String horaEntrega) { this.horaEntrega = horaEntrega; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getHoraCreacion() { return horaCreacion; }
    public void setHoraCreacion(String horaCreacion) { this.horaCreacion = horaCreacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public PerfilUsuarioVO getVendedor() { return vendedor; }
    public void setVendedor(PerfilUsuarioVO vendedor) { this.vendedor = vendedor; }

    public PerfilUsuarioVO getComprador() { return comprador; }
    public void setComprador(PerfilUsuarioVO comprador) { this.comprador = comprador; }
}
