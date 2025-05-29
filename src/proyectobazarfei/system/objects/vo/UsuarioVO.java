package proyectobazarfei.system.objects.vo;

public class UsuarioVO {
    private int id;
    private String nombre;
    private String apodo;
    private String correo;
    private String contrasena;
    private PreguntaSeguridadVO preguntaSeguridad;
    private String respuestaSeguridad;
    
    public UsuarioVO(String nombre, String apodo, String correo, String contrasena, PreguntaSeguridadVO preguntaSeguridad, String respuestaSeguridad) {
        this.nombre = nombre;
        this.apodo = apodo;
        this.correo = correo;
        this.contrasena = contrasena;
        this.preguntaSeguridad = preguntaSeguridad;
        this.respuestaSeguridad = respuestaSeguridad;
    }
    
    public UsuarioVO(int id, String nombre, String apodo, String correo, String contrasena, PreguntaSeguridadVO preguntaSeguridad, String respuestaSeguridad) {
        this.id = id;
        this.nombre = nombre;
        this.apodo = apodo;
        this.correo = correo;
        this.contrasena = contrasena;
        this.preguntaSeguridad = preguntaSeguridad;
        this.respuestaSeguridad = respuestaSeguridad;
    }
    
    public UsuarioVO() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApodo() { return apodo; }
    public void setApodo(String apodo) { this.apodo = apodo; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public PreguntaSeguridadVO getPreguntaSeguridad() { return preguntaSeguridad; }
    public void setPreguntaSeguridad(PreguntaSeguridadVO preguntaSeguridad) { this.preguntaSeguridad = preguntaSeguridad; }

    public String getRespuestaSeguridad() { return respuestaSeguridad; }
    public void setRespuestaSeguridad(String respuestaSeguridad) { this.respuestaSeguridad = respuestaSeguridad; }
}