/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidad;

/**
 *
 * @author LENOVO
 */

public class Rol {
    private int id;
    private String nombreRol;

    public Rol() {
    }

    public Rol(int id, String nombreRol) {
        this.id = id;
        this.nombreRol = nombreRol;
    }

    public int getId() { 
        return id; 
    }
    public String getNombreRol() {
        return nombreRol;
    }


    public void setId(int id) { 
        this.id = id;
    }
    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    @Override
    public String toString() {
        return nombreRol;
    }
}
