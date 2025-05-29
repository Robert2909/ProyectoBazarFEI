package proyectobazarfei.system.objects.vo;

import java.util.List;

public class ChatVO {
    private int id;
    private PerfilUsuarioVO vendedor;
    private PerfilUsuarioVO comprador;
    private List<MensajeVO> mensajes;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public PerfilUsuarioVO getVendedor() { return vendedor; }
    public void setVendedor(PerfilUsuarioVO vendedor) { this.vendedor = vendedor; }

    public PerfilUsuarioVO getComprador() { return comprador; }
    public void setComprador(PerfilUsuarioVO comprador) { this.comprador = comprador; }

    public List<MensajeVO> getMensajes() { return mensajes; }
    public void setMensajes(List<MensajeVO> mensajes) { this.mensajes = mensajes; }
}