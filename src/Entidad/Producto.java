/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidad;

/**
 *
 * @author LENOVO
 */
public class Producto {
    private int id;
    private String nombreProducto;
    private String unidad;
    private double precioBase;

    public Producto() {
    }

    public Producto(int id, String nombreProducto, String unidad, double precioBase) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.unidad = unidad;
        this.precioBase = precioBase;
    }

    public Producto(int aInt, String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

 
    public int getId() { 
        return id; 
    }
    public String getNombreProducto() { 
        return nombreProducto; 
    }
    public String getUnidad() {
        return unidad;
    }
    public double getPrecioBase() {
        return precioBase; 
    }


    public void setId(int id) { 
        this.id = id;
    }
    public void setNombreProducto(String nombreProducto) { 
        this.nombreProducto = nombreProducto; 
    }
    public void setUnidad(String unidad) { 
        this.unidad = unidad; 
    }
    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    @Override
    public String toString() {
        return nombreProducto + " (" + unidad + ")";
    }
}
