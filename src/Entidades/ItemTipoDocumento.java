/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 *
 * @author jheff
 */
// Archivo: ItemTipoDocumento.java
public class ItemTipoDocumento {
    private int id;
    private String nombre;

    public ItemTipoDocumento(int id, String nombre) {
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
        // Esto es CRÍTICO: el JComboBox usa este método para mostrar el texto al usuario.
        return nombre;
    }
    
    // Método necesario para comparar objetos al seleccionar en modo edición
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemTipoDocumento) {
            ItemTipoDocumento otro = (ItemTipoDocumento) obj;
            return this.id == otro.id;
        }
        return false;
    }
}