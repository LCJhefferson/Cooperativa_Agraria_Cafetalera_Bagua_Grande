/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author jheff
 */
   public class ITProducto {
    
    private int id;
    private String nombre;

    /**
     * Constructor para ItemProducto
     */
    public ITProducto(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    /**
     * Devuelve el ID real del producto para usarlo en la BD.
     */
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
    
    /**
     * Método que el JComboBox usa para mostrar el texto al usuario.
     */
    @Override
    public String toString() {
        return nombre;
    }
}