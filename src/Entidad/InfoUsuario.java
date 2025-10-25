/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidad;
import java.sql.Date;
/**
 *
 * @author LENOVO
 */

public class InfoUsuario {
    private int id;
    private Usuario usuario;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private Date fechaIngreso;
    private String estado;

    public InfoUsuario() {
    }

    public InfoUsuario(int id, Usuario usuario, String nombre, String apellido,
                       String telefono, String correo, Date fechaIngreso, String estado) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }

  
    public int getId() { return id; 
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public String getNombre() {
        return nombre;
    }
    public String getApellido() { 
        return apellido; 
    }
    public String getTelefono() { 
        return telefono; 
    }
    public String getCorreo() { 
        return correo;
    }
    public Date getFechaIngreso() { 
        return fechaIngreso; 
    }
    public String getEstado() { 
        return estado; 
    }

    
    public void setId(int id) {
        this.id = id;  
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    public void setApellido(String apellido) {
        this.apellido = apellido; 
    }
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }
    public void setCorreo(String correo) { 
        this.correo = correo; 
    }
    public void setFechaIngreso(Date fechaIngreso) { 
        this.fechaIngreso = fechaIngreso; 
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}
