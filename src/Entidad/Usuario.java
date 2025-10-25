/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidad;

/**
 *
 * @author LENOVO
 */
public class Usuario {
    private int id;
    private String usuario;
    private String contrasena;
    private Rol rol;

    public Usuario() {
    }

    public Usuario(int id, String usuario, String contrasena, Rol rol) {
        this.id = id;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public Usuario(int aInt, String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    public int getId() { 
        return id; 
    }
    public String getUsuario() { 
        return usuario; 
    }
    public String getContrasena() { 
        return contrasena;
    }
    public Rol getRol() { 
        return rol; 
    }


    public void setId(int id) { 
        this.id = id; 
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario; 
    }
    public void setContrasena(String contrasena) { 
        this.contrasena = contrasena;
    }
    public void setRol(Rol rol) { 
        this.rol = rol; 
    }

    @Override
    public String toString() {
        return usuario + " (" + rol + ")";
    }
}
