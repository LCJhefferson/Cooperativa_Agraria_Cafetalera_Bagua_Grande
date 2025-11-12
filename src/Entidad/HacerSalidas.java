package Entidad;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Representa la salida de un producto hacia un destino.
 */
public class HacerSalidas {
    private int id;
    private Producto producto;
    private Usuario usuario;
    private double cantidad;
    private String destino;
    private String observaciones;
    private Timestamp fechaRegistro;
    private String guiaSalida;

    public HacerSalidas() {
         // Establece la fecha actual al momento de crear el objeto
        this.fechaRegistro = Timestamp.valueOf(LocalDateTime.now());
    }

    public HacerSalidas(int id, Producto producto, Usuario usuario, double cantidad,
                          String destino, String observaciones, Timestamp fechaRegistro, String guiaSalida) {
        this.id = id;
        this.producto = producto;
        this.usuario = usuario;
        this.cantidad = cantidad;
        this.destino = destino;
        this.observaciones = observaciones;
        // Si la fecha no se pasa, usa la del sistema
        this.fechaRegistro = (fechaRegistro != null) ? fechaRegistro : Timestamp.valueOf(LocalDateTime.now());
        this.guiaSalida = guiaSalida;
    }


    public int getId() { 
        return id; 
    }
    public Producto getProducto() { 
        return producto; 
    }
    public Usuario getUsuario() { 
        return usuario; 
    }
    public double getCantidad() { 
        return cantidad;
    }
    public String getDestino() { 
        return destino;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }
    public String getGuiaSalida() { 
        return guiaSalida;
    }

    
    
    public void setId(int id) { this.id = id; }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario; 
    }
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad; 
    }
    public void setDestino(String destino) { 
        this.destino = destino; 
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones; 
    }
    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    
    }
    public void setGuiaSalida(String guiaSalida) { 
        this.guiaSalida = guiaSalida; 
    }

    @Override
    public String toString() {
        return producto + " → " + destino;
    }
}
