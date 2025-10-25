package Entidad;

import java.sql.Timestamp; 


/**
 * Representa un ingreso de producto en el sistema.
 * 
 * @author LENOVO
 */
public class IngresoProducto {
    private int id;
    private Producto producto;
    private Socio socio;
    private Usuario usuario;
    private double cantidad;
    private String cobase;
    private Timestamp fechaRegistro;
    private String guiaIngreso;
    private double humedad;
    private double precio;
    private String rendimiento;

    public IngresoProducto() {
    }

    public IngresoProducto(int id, Producto producto, Socio socio, Usuario usuario, double cantidad,
                           String cobase, Timestamp fechaRegistro, String guiaIngreso,
                           double humedad, double precio, String rendimiento) {
        this.id = id;
        this.producto = producto;
        this.socio = socio;
        this.usuario = usuario;
        this.cantidad = cantidad;
        this.cobase = cobase;
        this.fechaRegistro = fechaRegistro;
        this.guiaIngreso = guiaIngreso;
        this.humedad = humedad;
        this.precio = precio;
        this.rendimiento = rendimiento;
    }

    public int getId() {
        return id; 
    }
    public Producto getProducto() {
        return producto;
    }
    public Socio getSocio() {
        return socio; 
    }
    public Usuario getUsuario() { 
        return usuario;
    }
    public double getCantidad() { 
        return cantidad; 
    }
    public String getCobase() { 
        return cobase;
    }
    public Timestamp getFechaRegistro() {
        return fechaRegistro; 
    }
    public String getGuiaIngreso() { 
        return guiaIngreso; 
    }
    public double getHumedad() { 
        return humedad; 
    }
    public double getPrecio() {
        return precio;
    }
    public String getRendimiento() { 
        return rendimiento;
    }

    public void setId(int id) { 
        this.id = id;
    }
    public void setProducto(Producto producto) { 
        this.producto = producto; 
    }
    public void setSocio(Socio socio) {
        this.socio = socio;
    }
    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario; 
    }
    public void setCantidad(double cantidad) { 
        this.cantidad = cantidad; 
    }
    public void setCobase(String cobase) { 
        this.cobase = cobase;
    }
    public void setFechaRegistro(Timestamp fechaRegistro) { 
        this.fechaRegistro = fechaRegistro;
    }
    public void setGuiaIngreso(String guiaIngreso) {
        this.guiaIngreso = guiaIngreso; 
    }
    public void setHumedad(double humedad) {
        this.humedad = humedad; 
    }
    public void setPrecio(double precio) { 
        
        this.precio = precio;
    }
    public void setRendimiento(String rendimiento) { 
        this.rendimiento = rendimiento; 
    }

    @Override
    public String toString() {
        return producto + " - " + cantidad + " unidades";
    }
}