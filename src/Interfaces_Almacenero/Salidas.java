/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Interfaces_Almacenero;

import Conexion.Conexion;
import DAO.SalidaDAO;
import Modelos.Salida;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jheff
 */
public class Salidas extends javax.swing.JInternalFrame {
    
 private static Salidas instancia;
    private SalidaDAO salidaDAO = new SalidaDAO();
    private int filaEnEdicion = -1;
    // Opción B: id de usuario fijo por ahora
    private int idUsuarioActual = 7; // <- Cambia aquí si deseas otro id

    public Salidas() {
        initComponents();

        // Inicializar
        cargarProductos();
        configurarTabla();
        cargarDatosTabla();

        // Quitar bordes y título
        setBorder(null);
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);

        try {
            setMaximum(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public static Salidas getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new Salidas();
        }
        return instancia;
    }

    // -------------------- métodos nuevos --------------------

    private void cargarProductos() {
        cbxProducto.removeAllItems();
        String sql = "SELECT nombre FROM producto WHERE estado='activo'";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement pst = cn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                cbxProducto.addItem(rs.getString("nombre"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando productos: " + e.getMessage());
        }
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Producto", "Cantidad", "Número Orden", "Destino", "Observaciones", "Fecha", "PDF", "Eliminar", "Modificar"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 7;
            }
        };
        tblSalidas.setModel(modelo);
        tblSalidas.setRowHeight(30);

        // ocultar id
        if (tblSalidas.getColumnModel().getColumnCount() > 0) {
            tblSalidas.getColumnModel().getColumn(0).setMinWidth(0);
            tblSalidas.getColumnModel().getColumn(0).setMaxWidth(0);
        }
    }

    private void cargarDatosTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tblSalidas.getModel();
        modelo.setRowCount(0);

        List<Salida> lista = salidaDAO.listarSalidas();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        for (Salida s : lista) {
            JButton btnPdf = new JButton("PDF");
            btnPdf.setName("b_pdf");
            JButton btnEliminar = new JButton("Eliminar");
            btnEliminar.setName("b_eliminar");
            JButton btnModificar = new JButton("Modificar");
            btnModificar.setName("b_modificar");

            String fechaStr = s.getFechaSalida() == null ? "" : sdf.format(s.getFechaSalida());

            modelo.addRow(new Object[]{
                s.getId(),
                s.getNombreProducto(),
                s.getCantidadSalida(),
                s.getNumeroOrden() != null ? s.getNumeroOrden() : "",
                s.getDestino(),
                s.getObservaciones(),
                fechaStr,
                btnPdf,
                btnEliminar,
                btnModificar
            });
        }
    }

    private void limpiarCampos() {
        filaEnEdicion = -1;
        jButton2.setText("REGISTRAR");
        txtCantidad.setText("");
        txtNumeroOrden.setText("");
        txtDestino.setText("");
        txtObservaciones.setText("");
        if (cbxProducto.getItemCount() > 0) cbxProducto.setSelectedIndex(0);
    }

    private boolean validarCampos() {
        if (cbxProducto.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto");
            return false;
        }
        if (txtCantidad.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la cantidad");
            txtCantidad.requestFocus();
            return false;
        }
        try {
            double c = Double.parseDouble(txtCantidad.getText().trim());
            if (c <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0");
                txtCantidad.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida");
            txtCantidad.requestFocus();
            return false;
        }
        if (txtDestino.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el destino");
            txtDestino.requestFocus();
            return false;
        }
        return true;
    }

    // Generar PDF para una salida (usa iText)
    private void generarPDF_Salida(Salida s) {
        if (s == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la salida para generar PDF");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar PDF de Salida");
        String fileName = "salida_" + (s.getNumeroOrden() != null ? s.getNumeroOrden() : s.getId()) + ".pdf";
        chooser.setSelectedFile(new File(fileName));
        chooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF", "pdf"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File archivo = chooser.getSelectedFile();

        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            Document doc = new Document();
            PdfWriter.getInstance(doc, fos);
            doc.open();

            Paragraph titulo = new Paragraph("COOPERATIVA AGRARIA CAFETALERA BAGUA GRANDE\nSALIDA DE PRODUCTO\n\n",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);

            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(100);

            tabla.addCell("Producto:");
            tabla.addCell(s.getNombreProducto());

            tabla.addCell("Cantidad:");
            tabla.addCell(String.valueOf(s.getCantidadSalida()));

            tabla.addCell("Número de Orden:");
            tabla.addCell(s.getNumeroOrden() != null ? s.getNumeroOrden() : "N/A");

            tabla.addCell("Destino:");
            tabla.addCell(s.getDestino());

            tabla.addCell("Fecha de Salida:");
            tabla.addCell(s.getFechaSalida() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(s.getFechaSalida()) : "");

            tabla.addCell("Observaciones:");
            tabla.addCell(s.getObservaciones() != null ? s.getObservaciones() : "");

            doc.add(tabla);
            doc.close();

            JOptionPane.showMessageDialog(this, "PDF generado correctamente:\n" + archivo.getAbsolutePath());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar PDF: " + e.getMessage());
            e.printStackTrace();
        }
}
     
      
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        cbxProducto = new javax.swing.JComboBox<>();
        txtNumeroOrden = new javax.swing.JTextField();
        txtObservaciones = new javax.swing.JTextField();
        txtCantidad = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtDestino = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSalidas = new javax.swing.JTable();

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton2.setText("REGISTRAR");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        cbxProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FTO", "FT", "C" }));
        cbxProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxProductoActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Producto");
        jLabel1.setToolTipText("");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Observaciones");
        jLabel11.setToolTipText("");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Destino");
        jLabel14.setToolTipText("");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("cantidad");
        jLabel17.setToolTipText("");

        jLabel12.setText("Numero de orden ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(cbxProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 21, Short.MAX_VALUE)
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addComponent(txtCantidad, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtObservaciones)
                            .addComponent(txtDestino)
                            .addComponent(txtNumeroOrden))))
                .addGap(20, 20, 20))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel17)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtNumeroOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(54, 54, 54)
                .addComponent(jButton2)
                .addGap(500, 500, 500))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tblSalidas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSalidas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSalidasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblSalidas);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxProductoActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (!validarCampos()) return;

        try {
            String productoNombre = cbxProducto.getSelectedItem().toString();
            double cantidad = Double.parseDouble(txtCantidad.getText().trim());
            String numeroOrden = txtNumeroOrden.getText().trim();
            String destino = txtDestino.getText().trim();
            String observ = txtObservaciones.getText().trim();

            int idProducto = salidaDAO.obtenerIdProductoPorNombre(productoNombre);
            if (idProducto == -1) {
                JOptionPane.showMessageDialog(this, "Producto no encontrado en BD");
                return;
            }

            Salida s = new Salida();
            s.setIdUsuario(idUsuarioActual);
            s.setIdProducto(idProducto);
            s.setCantidadSalida(cantidad);
            s.setNumeroOrden(numeroOrden.isEmpty() ? null : numeroOrden);
            s.setDestino(destino);
            s.setObservaciones(observ);

            if (filaEnEdicion == -1) {
                if (salidaDAO.registrarSalida(s)) {
                    JOptionPane.showMessageDialog(this, "Salida registrada correctamente");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar salida");
                }
            } else {
                // obtener id desde la fila seleccionada
                int idSalida = (int) tblSalidas.getValueAt(filaEnEdicion, 0);
                s.setId(idSalida);
                if (salidaDAO.actualizarSalida(s)) {
                    JOptionPane.showMessageDialog(this, "Salida actualizada correctamente");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar salida");
                }
                filaEnEdicion = -1;
                jButton2.setText("REGISTRAR");
            }

            limpiarCampos();
            cargarDatosTabla();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tblSalidasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalidasMouseClicked
        int fila = tblSalidas.rowAtPoint(evt.getPoint());
        int col = tblSalidas.columnAtPoint(evt.getPoint());

        if (fila == -1) return;

        int idSalida = (int) tblSalidas.getValueAt(fila, 0);

        // PDF (col 7)
        if (col == 7) {
            Salida s = salidaDAO.obtenerSalidaPorId(idSalida);
            generarPDF_Salida(s);
            return;
        }

        // Eliminar (col 8)
        if (col == 8) {
            int r = JOptionPane.showConfirmDialog(this, "¿Eliminar esta salida? Se restituirá el stock.", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                if (salidaDAO.eliminarSalida(idSalida)) {
                    JOptionPane.showMessageDialog(this, "Salida eliminada");
                    cargarDatosTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar salida");
                }
            }
            return;
        }

        // Modificar (col 9)
        if (col == 9) {
            Salida s = salidaDAO.obtenerSalidaPorId(idSalida);
            if (s != null) {
                cbxProducto.setSelectedItem(s.getNombreProducto());
                txtCantidad.setText(String.valueOf(s.getCantidadSalida()));
                txtNumeroOrden.setText(s.getNumeroOrden() != null ? s.getNumeroOrden() : "");
                txtDestino.setText(s.getDestino());
                txtObservaciones.setText(s.getObservaciones() != null ? s.getObservaciones() : "");
                filaEnEdicion = fila;
                jButton2.setText("ACTUALIZAR");
            }
            return;
        }
    }//GEN-LAST:event_tblSalidasMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbxProducto;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblSalidas;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtDestino;
    private javax.swing.JTextField txtNumeroOrden;
    private javax.swing.JTextField txtObservaciones;
    // End of variables declaration//GEN-END:variables


}