package proyectobazarfei.system.objects.vo;

import java.util.List;

public class ProductoVO {
    private int id;
    private String titulo;
    private String descripcion;
    private double precio;
    private String categoria;
    private List<String> imagenes;
    private String portada;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public List<String> getImagenes() { return imagenes; }
    public void setImagenes(List<String> imagenes) { this.imagenes = imagenes; }
    
    public String getPortada() { return portada; }
    public void setPortada(String portada) { this.portada = portada; }
}
