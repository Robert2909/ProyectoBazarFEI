package proyectobazarfei.system.objects.vo;

import java.util.Date;

public class MensajeVO {
    private Date fechaEnvio;
    private String horaEnvio;
    private String texto;

    public Date getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(Date fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public String getHoraEnvio() { return horaEnvio; }
    public void setHoraEnvio(String horaEnvio) { this.horaEnvio = horaEnvio; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}