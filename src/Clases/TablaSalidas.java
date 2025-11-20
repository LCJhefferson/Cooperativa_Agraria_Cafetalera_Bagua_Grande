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
public class TablaSalidas {
    //creamos un metodo para ver la tabla 
    public void ver_tabla(JTable tablaSalidas){
       
        //AQUI ESTOMS RENDRESISANDO LA TABLA
        tablaSalidas.setDefaultRenderer(Object.class, new RenderTabla());
        //
        JButton btnpdf = new JButton("pdf");
        btnpdf.setName("b_pdf");
        
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setName("b_eliminar");
        
        JButton btnModificar = new JButton("Modificar");
        btnModificar.setName("b_modificar");
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setName("b_guardar");

        
     DefaultTableModel v_tablaSalidas = new DefaultTableModel(
    new Object[][] {
        {"FTO","120","000023","planta de procesamiento","ninguna","12-08-2025",btnpdf,btnEliminar,btnModificar},
        {"FT", "85", "000024", "planta de procesamiento","muy humedo", "12-08-2025",btnpdf, btnEliminar, btnModificar},
    },
    new Object[]{"Producto","Cantidad","Numero de orden","Destino","Observacions","Fecha","Pdf","Eliminar","Modificar"}
) {
    boolean editable = false; // bandera para saber si se puede editar

    @Override
    public boolean isCellEditable(int row, int column) {
        // permite editar solo si está en modo edición y no es una columna de botones
        return editable && column < 6;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
};

    
        
        tablaSalidas.setModel(v_tablaSalidas);
        
        
        //hacemos las celdas mas grandes
        tablaSalidas.setRowHeight(30);
        
                
        
       for (int column = 0; column < tablaSalidas.getColumnCount(); column++) {
    TableColumn tableColumn = tablaSalidas.getColumnModel().getColumn(column);
    int preferredWidth = 50; 
    int maxWidth = 300;

    for (int row = 0; row < tablaSalidas.getRowCount(); row++) {
        TableCellRenderer cellRenderer = tablaSalidas.getCellRenderer(row, column);
        Component c = tablaSalidas.prepareRenderer(cellRenderer, row, column);
        int width = c.getPreferredSize().width + 10;
        preferredWidth = Math.max(preferredWidth, width);

        if (preferredWidth >= maxWidth) {
            preferredWidth = maxWidth;
            break;
        }
    }
    tableColumn.setPreferredWidth(preferredWidth);
}

    }       
  
}
