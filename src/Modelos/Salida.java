package Modelos;

import java.sql.Timestamp;

public class Salida {
    private int id;
    private int idUsuario;
    private int idProducto;
    private double cantidadSalida;
    private String numeroOrden;
    private String destino;
    private Timestamp fechaSalida;
    private String observaciones;
    private String nombreProducto; // para mostrar en tabla

    public Salida() {}

    // getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public double getCantidadSalida() { return cantidadSalida; }
    public void setCantidadSalida(double cantidadSalida) { this.cantidadSalida = cantidadSalida; }

    public String getNumeroOrden() { return numeroOrden; }
    public void setNumeroOrden(String numeroOrden) { this.numeroOrden = numeroOrden; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public Timestamp getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(Timestamp fechaSalida) { this.fechaSalida = fechaSalida; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
}
