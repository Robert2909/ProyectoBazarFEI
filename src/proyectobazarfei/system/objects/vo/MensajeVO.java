package proyectobazarfei.system.objects.vo;

import java.util.Date;

public class MensajeVO {
    private String texto;
    private Date fechaEnvio;
    private String horaEnvio;
    private PerfilUsuarioVO remitente;

    public MensajeVO() {}

    public MensajeVO(String texto, Date fechaEnvio, String horaEnvio, PerfilUsuarioVO remitente) {
        this.texto = texto;
        this.fechaEnvio = fechaEnvio;
        this.horaEnvio = horaEnvio;
        this.remitente = remitente;
    }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Date getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(Date fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public String getHoraEnvio() { return horaEnvio; }
    public void setHoraEnvio(String horaEnvio) { this.horaEnvio = horaEnvio; }

    public PerfilUsuarioVO getRemitente() { return remitente; }
    public void setRemitente(PerfilUsuarioVO remitente) { this.remitente = remitente; }
}
