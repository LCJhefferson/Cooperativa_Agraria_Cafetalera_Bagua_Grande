package Clases;

import Conexion.Conexion;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TablaCompras {

    // Método para cargar datos desde la BD
    public void cargarDatos(JTable tblCompras) {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo botones (PDF, Eliminar, Modificar) son "editables" para permitir el click
                return column >= 9;
            }
        };

        // Orden de columnas (ya sin Cobase)
        modelo.setColumnIdentifiers(new Object[]{
            "ID", "Producto", "Cantidad", "Humedad", "Precio", "Rendimiento",
            "Guía de remisión", "Socio", "Fecha", "PDF", "Eliminar", "Modificar"
        });

        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT c.id, p.nombre AS producto, c.cantidad, c.precio, " +
                 "c.humedad, c.rendimiento, c.guia_ingreso, s.nombre AS socio, c.fecha_registro " +
                 "FROM compra c " +
                 "JOIN producto p ON c.id_producto = p.id " +
                 "JOIN socio s ON c.id_socio = s.id")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),                 // 0 ID
                    rs.getString("producto"),        // 1 Producto
                    rs.getDouble("cantidad"),        // 2 Cantidad
                    rs.getDouble("humedad"),         // 3 Humedad
                    rs.getDouble("precio"),          // 4 Precio
                    rs.getDouble("rendimiento"),     // 5 Rendimiento
                    rs.getString("guia_ingreso"),    // 6 Guía de remisión
                    rs.getString("socio"),           // 7 Socio
                    rs.getTimestamp("fecha_registro"), // 8 Fecha
                    crearBoton("PDF", "b_pdf"),      // 9 PDF
                    crearBoton("Eliminar", "b_eliminar"), // 10 Eliminar
                    crearBoton("Modificar", "b_modificar") // 11 Modificar
                });
            }

            tblCompras.setModel(modelo);
            tblCompras.setRowHeight(30);
            tblCompras.setDefaultRenderer(Object.class, new RenderTabla());

            // Ocultar columna ID visualmente
            if (tblCompras.getColumnModel().getColumnCount() > 0) {
                tblCompras.getColumnModel().getColumn(0).setMinWidth(0);
                tblCompras.getColumnModel().getColumn(0).setMaxWidth(0);
                tblCompras.getColumnModel().getColumn(0).setWidth(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para crear botones
    private JButton crearBoton(String texto, String nombre) {
        JButton boton = new JButton(texto);
        boton.setName(nombre);
        return boton;
    }

    // Lógica del botón PDF
    public void generarPDF_Fila(JTable tblCompras, int fila) {
        try {
            String ruta = "C:/reportes/Compra_Fila_" + fila + ".pdf";

            Document documento = new Document();
            PdfWriter.getInstance(documento, new FileOutputStream(ruta));
            documento.open();

            documento.add(new Paragraph("REPORTE DE COMPRA (FILA " + fila + ")\n\n"));

            PdfPTable tablaPDF = new PdfPTable(tblCompras.getColumnCount());

            // Encabezados
            for (int c = 0; c < tblCompras.getColumnCount(); c++) {
                tablaPDF.addCell(tblCompras.getColumnName(c));
            }

            // Datos de la fila
            for (int c = 0; c < tblCompras.getColumnCount(); c++) {
                Object valor = tblCompras.getValueAt(fila, c);
                if (valor instanceof JButton btn) {
                    tablaPDF.addCell(btn.getText());
                } else {
                    tablaPDF.addCell(valor != null ? valor.toString() : "");
                }
            }

            documento.add(tablaPDF);
            documento.close();

            JOptionPane.showMessageDialog(null, "PDF generado correctamente en: " + ruta);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el PDF: " + e.getMessage());
        }
    }

    // Lógica del botón Eliminar
    private void eliminarCompra(int idCompra, JTable tabla) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "¿Seguro que deseas eliminar la compra ID: " + idCompra + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection con = Conexion.getConexion();
                 PreparedStatement ps = con.prepareStatement("DELETE FROM compra WHERE id = ?")) {
                ps.setInt(1, idCompra);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Compra eliminada correctamente.");
                cargarDatos(tabla); // refrescar tabla
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Lógica del botón Modificar
    private void modificarCompra(int idCompra, JTable tabla, int fila) {
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(
                 "UPDATE compra SET cantidad=?, humedad=?, precio=?, rendimiento=? WHERE id=?")) {

            double cantidad = Double.parseDouble(tabla.getValueAt(fila, 2).toString());   // Cantidad
            double humedad = Double.parseDouble(tabla.getValueAt(fila, 3).toString());    // Humedad
            double precio = Double.parseDouble(tabla.getValueAt(fila, 4).toString());     // Precio
            double rendimiento = Double.parseDouble(tabla.getValueAt(fila, 5).toString()); // Rendimiento

            ps.setDouble(1, cantidad);
            ps.setDouble(2, humedad);
            ps.setDouble(3, precio);
            ps.setDouble(4, rendimiento);
            ps.setInt(5, idCompra);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Compra modificada correctamente.");
            cargarDatos(tabla); // refrescar tabla

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
