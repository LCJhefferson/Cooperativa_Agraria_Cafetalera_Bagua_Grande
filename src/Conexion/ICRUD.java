/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author LENOVO
 */
public interface ICRUD {
    public ArrayList listar() throws Exception;
    public int crear(Object object) throws SQLException;
    public void actualizar(int id, Object object) throws Exception;
    public void eliminar(int id) throws Exception;
    public Object get(int id) throws Exception;
}

