package Clases;

import Conexion.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class BotonesTablaCompras {

    private JTable tblCompras;
    private JTextField txtCantidad, txtPrecio, txtHumedad, txtRendimiento, txtGuia, txtSocio;
    private int filaEnEdicion = -1;
    private int idCompraSeleccionada = -1;

    // Constructor: recibe la tabla y los campos del formulario
    public BotonesTablaCompras(JTable tblCompras,
                               JTextField txtCantidad,
                               JTextField txtPrecio,
                               JTextField txtHumedad,
                               JTextField txtRendimiento,
                               JTextField txtGuia,
                               JTextField txtSocio) {
        this.tblCompras = tblCompras;
        this.txtCantidad = txtCantidad;
        this.txtPrecio = txtPrecio;
        this.txtHumedad = txtHumedad;
        this.txtRendimiento = txtRendimiento;
        this.txtGuia = txtGuia;
        this.txtSocio = txtSocio;
    }

    // Método principal para manejar clics en la tabla
    public void manejarEventoTabla(MouseEvent evt) {
        int fila = tblCompras.rowAtPoint(evt.getPoint());
        int columna = tblCompras.columnAtPoint(evt.getPoint());

        if (fila >= 0 && columna >= 0) {
            Object value = tblCompras.getValueAt(fila, columna);

            if (value instanceof JButton boton) {
                String nombre = boton.getName();

                // === BOTÓN PDF ===
                if (nombre.equals("b_pdf")) {
                    PDF_tabla pdf = new PDF_tabla();
                    pdf.generarPDF_FilaTabla(tblCompras, fila, "Compra");
                }

                // === BOTÓN ELIMINAR ===
                if (nombre.equals("b_eliminar")) {
                    eliminarCompra(fila);
                }

                // === BOTÓN MODIFICAR ===
                if (nombre.equals("b_modificar")) {
                    cargarDatosEnFormulario(fila);
                }
            }
        }
    }

    // Cargar datos de la fila en el formulario (orden correcto)
    private void cargarDatosEnFormulario(int fila) {
        txtCantidad.setText(tblCompras.getValueAt(fila, 2).toString());   // Cantidad
        txtHumedad.setText(tblCompras.getValueAt(fila, 3).toString());    // Humedad
        txtPrecio.setText(tblCompras.getValueAt(fila, 4).toString());     // Precio
        txtRendimiento.setText(tblCompras.getValueAt(fila, 5).toString()); // Rendimiento
        txtGuia.setText(tblCompras.getValueAt(fila, 7).toString());       // Guía de remisión
        txtSocio.setText(tblCompras.getValueAt(fila, 8).toString());      // Socio

        idCompraSeleccionada = Integer.parseInt(tblCompras.getValueAt(fila, 0).toString()); // ID

        filaEnEdicion = fila;
    }

    // Guardar cambios en BD (orden correcto)
    private boolean guardarCambiosEnBD(int idCompra) {
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(
                 "UPDATE compra SET cantidad=?, humedad=?, precio=?,"
                         + " rendimiento=?, guia_ingreso=? WHERE id=?")) {

            ps.setDouble(1, Double.parseDouble(txtCantidad.getText()));   // Cantidad
            ps.setDouble(2, Double.parseDouble(txtHumedad.getText()));    // Humedad
            ps.setDouble(3, Double.parseDouble(txtPrecio.getText()));     // Precio
            ps.setDouble(4, Double.parseDouble(txtRendimiento.getText())); // Rendimiento
            ps.setString(5, txtGuia.getText());                          // Guía
            ps.setInt(6, idCompra);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar: " + e.getMessage());
            return false;
        }
    }

    // Eliminar compra de BD
    public void eliminarCompra(int fila) {
        int id = Integer.parseInt(tblCompras.getValueAt(fila, 0).toString()); // ID en columna 0

        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Eliminar la compra ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con.prepareStatement("DELETE FROM compra WHERE id=?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
                ((DefaultTableModel) tblCompras.getModel()).removeRow(fila);
                JOptionPane.showMessageDialog(null, "Compra eliminada.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al eliminar la compra: " + e.getMessage());
            }
        }
    }
}
