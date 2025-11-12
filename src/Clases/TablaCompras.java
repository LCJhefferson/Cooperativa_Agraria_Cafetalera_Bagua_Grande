/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jheff
 */
public class TablaCompras {
    //creamos un metodo para ver la tabla 
    public void ver_tabla(JTable tabla){
        
        
        //AQUI ESTOMS RENDRESISANDO LA TABLA
        tabla.setDefaultRenderer(Object.class, new RenderTabla());
        //
        JButton btnpdf = new JButton("pdf");
        btnpdf.setName("b_pdf");
        
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setName("b_eliminar");
        
        JButton btnModificar = new JButton("Modificar");
        btnModificar.setName("b_modificar");
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setName("b_guardar");

        
     DefaultTableModel v_tabla = new DefaultTableModel(
    new Object[][] {
        {"CO","120","150","95","45","57","AK125JK12","Juan Lopez",btnpdf,btnEliminar,btnModificar},
        {"CA", "85", "135", "88", "42", "60", "BX982LK45", "Maria Perez", btnpdf, btnEliminar, btnModificar},
    },
    new Object[]{"PRODUCTO","CANTIDAD","PRECIO","COBASE","HUMEDAD","RENDIMIENTO","GUIA DE REMISION","SOCIO","PDF","ELIMINAR","MODIFICAR"}
) {
    boolean editable = false; // bandera para saber si se puede editar

    @Override
    public boolean isCellEditable(int row, int column) {
        // permite editar solo si está en modo edición y no es una columna de botones
        return editable && column < 8;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
};

    
        
        tabla.setModel(v_tabla);
        
        //codigo par ahacer la trnasicion de botones de modificar a guardar 
        
        
       
        
        //hacemos las celdas mas grandes
        tabla.setRowHeight(30);
    }
}
