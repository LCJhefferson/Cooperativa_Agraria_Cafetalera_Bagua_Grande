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
        return false; // ninguna celda editable
    }
};

        modelo.setColumnIdentifiers(new Object[]{
            "ID", "Producto", "Cantidad", "Precio", "Cobase", "Humedad", "Rendimiento",
            "Guía de remisión", "Socio", "Fecha", "PDF", "Eliminar", "Modificar"
        });

        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT c.id, p.nombre AS producto, c.cantidad, c.precio, cb.nombre AS cobase, " +
                 "c.humedad, c.rendimiento, c.guia_ingreso, s.nombre AS socio, c.fecha_registro " +
                 "FROM compra c " +
                 "JOIN producto p ON c.id_producto = p.id " +
                 "JOIN socio s ON c.id_socio = s.id " +
                 "JOIN cobase cb ON s.id_cobase = cb.id")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("producto"),
                    rs.getDouble("cantidad"),
                    rs.getDouble("precio"),
                    rs.getString("cobase"),
                    rs.getDouble("humedad"),
                    rs.getDouble("rendimiento"),
                    rs.getString("guia_ingreso"),
                    rs.getString("socio"),
                    rs.getTimestamp("fecha_registro"),
                    crearBoton("PDF", "b_pdf"),
                    crearBoton("Eliminar", "b_eliminar"),
                    crearBoton("Modificar", "b_modificar")
                });
            }

            tblCompras.setModel(modelo);
            tblCompras.setRowHeight(30);
            tblCompras.setDefaultRenderer(Object.class, new RenderTabla());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método  para crear botones
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

        // TÍTULO
        documento.add(new Paragraph("REPORTE DE COMPRA (FILA " + fila + ")\n\n"));

        PdfPTable tablaPDF = new PdfPTable(tblCompras.getColumnCount());

        // ENCABEZADOS
        for (int c = 0; c < tblCompras.getColumnCount(); c++) {
            tablaPDF.addCell(tblCompras.getColumnName(c));
        }

        // DATOS DE LA FILA A EXPORTAR
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
                 "UPDATE compra SET cantidad=?, precio=?, humedad=?, rendimiento=? WHERE id=?")) {

            double cantidad = Double.parseDouble(tabla.getValueAt(fila, 2).toString());
            double precio = Double.parseDouble(tabla.getValueAt(fila, 3).toString()); 
            double humedad = Double.parseDouble(tabla.getValueAt(fila, 5).toString());
            double rendimiento = Double.parseDouble(tabla.getValueAt(fila, 6).toString());

            ps.setDouble(1, cantidad);
            ps.setDouble(2, precio);
            ps.setDouble(3, humedad);
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

