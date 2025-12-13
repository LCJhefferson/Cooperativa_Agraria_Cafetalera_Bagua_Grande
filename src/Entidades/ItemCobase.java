/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author jheff
 */
// Archivo: ItemCobase.java
public class ItemCobase {
    private int id;
    private String nombre;

    public ItemCobase(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }
    
    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        // CRÍTICO: Muestra el nombre de la co-base.
        return nombre;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemCobase) {
            ItemCobase otro = (ItemCobase) obj;
            return this.id == otro.id;
        }
        return false;
    }
}