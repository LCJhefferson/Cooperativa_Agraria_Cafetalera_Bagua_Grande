/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author jheff
 */
public class TablaCompras {
    //creamos un metodo para ver la tabla 
    public void ver_tabla(JTable tablaCompras){
       
        //AQUI ESTOMS RENDRESISANDO LA TABLA
        tablaCompras.setDefaultRenderer(Object.class, new RenderTabla());
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
        {"CO","120","150","95","45","57","AK125JK12","Juan Lopez","12-02-2024:12:00",btnpdf,btnEliminar,btnModificar},
        {"CA", "85", "135", "88", "42", "60", "BX982LK45", "Maria Perez","31-04-2025:20:00", btnpdf, btnEliminar, btnModificar},
        {"CO","120","150","95","45","57","AK125JK12","Juan Lopez","12-02-2024:12:00",btnpdf,btnEliminar,btnModificar},
        {"CA", "85", "135", "88", "42", "60", "BX982LK45", "Maria Perez","31-04-2025:20:00", btnpdf, btnEliminar, btnModificar},
        {"CO","120","150","95","45","57","AK125JK12","Juan Lopez","12-02-2024:12:00",btnpdf,btnEliminar,btnModificar},
        {"CA", "85", "135", "88", "42", "60", "BX982LK45", "Maria Perez","31-04-2025:20:00", btnpdf, btnEliminar, btnModificar},
        {"CO","120","150","95","45","57","AK125JK12","Juan Lopez","12-02-2024:12:00",btnpdf,btnEliminar,btnModificar},
        {"CA", "85", "135", "88", "42", "60", "BX982LK45", "Maria Perez","31-04-2025:20:00", btnpdf, btnEliminar, btnModificar},
        {"CO","120","150","95","45","57","AK125JK12","Juan Lopez","12-02-2024:12:00",btnpdf,btnEliminar,btnModificar},
        {"CA", "85", "135", "88", "42", "60", "BX982LK45", "Maria Perez","31-04-2025:20:00", btnpdf, btnEliminar, btnModificar},
    },
    new Object[]{"Producto","Cantidad","precio","Cobase","Humedad","Rendimiento","Guia de remision","Socio","Fecha","PDF","ELIMINAR","MODIFICAR"}
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

    
        
        tablaCompras.setModel(v_tabla);
        
        //codigo par ahacer la trnasicion de botones de modificar a guardar 
        
        
       for (int column = 0; column < tablaCompras.getColumnCount(); column++) {
    TableColumn tableColumn = tablaCompras.getColumnModel().getColumn(column);
    int preferredWidth = 50; 
    int maxWidth = 300;

    for (int row = 0; row < tablaCompras.getRowCount(); row++) {
        TableCellRenderer cellRenderer = tablaCompras.getCellRenderer(row, column);
        Component c = tablaCompras.prepareRenderer(cellRenderer, row, column);
        int width = c.getPreferredSize().width + 10;
        preferredWidth = Math.max(preferredWidth, width);

        if (preferredWidth >= maxWidth) {
            preferredWidth = maxWidth;
            break;
        }
    }
    tableColumn.setPreferredWidth(preferredWidth);
}

        
      
        //hacemos las celdas mas grandes
        tablaCompras.setRowHeight(30);

    }       
  
}
