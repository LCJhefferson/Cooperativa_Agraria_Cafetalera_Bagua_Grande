package Clases;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;

public class BotonesTablas {

    private int filaEnEdicion = -1;

    // Método para manejar clics en la tabla
    public void manejarEventoTabla(JTable tblListaCompras, MouseEvent evt) {
        int fila = tblListaCompras.rowAtPoint(evt.getPoint());
        int columna = tblListaCompras.columnAtPoint(evt.getPoint());

        if (fila >= 0 && columna >= 0) {
            Object value = tblListaCompras.getValueAt(fila, columna);

            if (value instanceof JButton) {
                JButton boton = (JButton) value;
                String nombre = boton.getName();

                // === BOTÓN PDF ===
                if (nombre.equals("b_pdf")) {
                    PDF_tabla pdf = new PDF_tabla();
                    pdf.generarPDF_FilaTabla(tblListaCompras, fila, "Compra");
                }

                // === BOTÓN ELIMINAR ===
                if (nombre.equals("b_eliminar")) {
                    System.out.println("Click en el botón Eliminar");
                    ((DefaultTableModel) tblListaCompras.getModel()).removeRow(fila);
                }

                // === BOTÓN MODIFICAR ===
                if (nombre.equals("b_modificar")) {
                    System.out.println("Click en el botón Modificar");

                    // Si hay otra fila en edición, la restauramos
                    if (filaEnEdicion != -1 && filaEnEdicion != fila) {
                        JButton btnModificarAntiguo = new JButton("Modificar");
                        btnModificarAntiguo.setName("b_modificar");
                        tblListaCompras.setValueAt(btnModificarAntiguo, filaEnEdicion, columna);
                    }

                    // Activamos el modo Guardar solo para la fila actual
                    JButton btnGuardar = new JButton("Guardar");
                    btnGuardar.setName("b_guardar");
                    tblListaCompras.setValueAt(btnGuardar, fila, columna);
                    tblListaCompras.repaint();

                    filaEnEdicion = fila; // guardamos la fila que se está editando
                }

                // === BOTÓN GUARDAR ===
                if (nombre.equals("b_guardar")) {
                    System.out.println("Click en el botón Guardar");

                    String producto = tblListaCompras.getValueAt(fila, 0).toString();
                    String cantidad = tblListaCompras.getValueAt(fila, 1).toString();
                    String precio = tblListaCompras.getValueAt(fila, 2).toString();

                    System.out.println("Guardando cambios: " + producto + " - " + cantidad + " - " + precio);

                    // Restauramos el botón a "Modificar"
                    JButton btnModificar = new JButton("Modificar");
                    btnModificar.setName("b_modificar");
                    tblListaCompras.setValueAt(btnModificar, fila, columna);
                    tblListaCompras.repaint();

                    filaEnEdicion = -1; // limpiamos la fila activa
                }
            }
        }
    }
}
