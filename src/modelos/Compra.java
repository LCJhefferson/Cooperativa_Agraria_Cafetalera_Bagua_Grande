package modelos;

public class Compra {
    private int id;
    private int idUsuario;
    private int idProducto;
    private int idPrecioDia;
    private int idSocio;
    private double rendimiento;
    private double humedad;
    private String guiaIngreso;
    private double cantidad;
    private double precio;
    private double precioCalculado;
    private String fechaRegistro;

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdPrecioDia() {
        return idPrecioDia;
    }

    public void setIdPrecioDia(int idPrecioDia) {
        this.idPrecioDia = idPrecioDia;
    }

    public int getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }

    public double getRendimiento() {
        return rendimiento;
    }

    public void setRendimiento(double rendimiento) {
        this.rendimiento = rendimiento;
    }

    public double getHumedad() {
        return humedad;
    }

    public void setHumedad(double humedad) {
        this.humedad = humedad;
    }

    public String getGuiaIngreso() {
        return guiaIngreso;
    }

    public void setGuiaIngreso(String guiaIngreso) {
        this.guiaIngreso = guiaIngreso;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPrecioCalculado() {
        return precioCalculado;
    }

    public void setPrecioCalculado(double precioCalculado) {
        this.precioCalculado = precioCalculado;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
}
