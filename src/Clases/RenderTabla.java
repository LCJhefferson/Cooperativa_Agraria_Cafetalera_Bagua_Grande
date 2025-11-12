
package Clases;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderTabla extends DefaultTableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object o, boolean isSelected, boolean hasFocus, int row, int column) {
      //si el dato que le asignamos es un boton entoncwes que agrege el boton 
        if(o instanceof JButton){
          JButton boton = (JButton)o;
          //RETORNAMOS UN BOTON //SI QUISIERAS QUE SEA OTRA COSA SOLO SE CAMBIARIA EL BOTON POR UN LANERL EJEMPLO
          return boton;
      
      }
        return super.getTableCellRendererComponent(table, o, isSelected, hasFocus, row, column); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
  
}
