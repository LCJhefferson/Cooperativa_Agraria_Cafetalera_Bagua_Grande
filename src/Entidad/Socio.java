/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidad;

import java.util.Date;

/**
 *
 * @author LENOVO
 */
public class Socio {
    private int id;
    private String nombre;
    private String dni;
    private String estado;
    private Date fechaIngreso;

    public Socio() {
    }

    public Socio(int id, String nombre, String dni, String cobase, String estado, Date fechaIngreso) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.estado = estado;
        this.fechaIngreso = fechaIngreso;
    }


    public int getId() {
        return id;
    }
    public String getNombre() { 
        return nombre;
    }
    public String getDni() { 
        return dni;
    }
 
    public String getEstado() { 
        return estado; 
    }
    public Date getFechaIngreso() { 
        return fechaIngreso; 
    }

  
    public void setId(int id) {
        this.id = id;
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre;
    }
    public void setDni(String dni) { 
        this.dni = dni;
    }
   
    public void setEstado(String estado) { 
        this.estado = estado;
    }
    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso; 
    }

    @Override
    public String toString() {
        return nombre + " (" + dni + ")";
    }
}
