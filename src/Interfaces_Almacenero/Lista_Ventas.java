/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Interfaces_Almacenero;

import Clases.PDF_tabla;
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
public class Lista_Ventas extends javax.swing.JInternalFrame {

    
    private static Lista_Ventas instancia;
    private SalidaDAO salidaDAO = new SalidaDAO();

    public Lista_Ventas() {
        initComponents();

        // Inicializar UI
        configurarTabla();
        cargarDatosTabla(null); // null => sin filtro, carga todo

        // quitar bordes y título
        setBorder(null);
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);

        try {
            setMaximum(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public static Lista_Ventas getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new Lista_Ventas();
        }
        return instancia;
    }

    // ---------------------- Configurar tabla ----------------------
    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Producto", "Cantidad", "Número Orden", "Destino",
                    "Observaciones", "Fecha", "PDF", "Eliminar", "Modificar"
                }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 7; // solo columnas de botones "clickables"
            }
        };

        tblListaSalidas.setModel(modelo);
        tblListaSalidas.setRowHeight(30);

        // ocultar id
        if (tblListaSalidas.getColumnModel().getColumnCount() > 0) {
            tblListaSalidas.getColumnModel().getColumn(0).setMinWidth(0);
            tblListaSalidas.getColumnModel().getColumn(0).setMaxWidth(0);
        }
    }

    // ---------------------- Cargar datos desde BD ----------------------
    /**
     * Carga la tabla desde la base de datos.
     * @param numeroOrdenFiltro si es null o vacío: carga todo; si no, filtra por numero_orden LIKE '%filtro%'
     */
    private void cargarDatosTabla(String numeroOrdenFiltro) {
        DefaultTableModel modelo = (DefaultTableModel) tblListaSalidas.getModel();
        modelo.setRowCount(0);

        List<Salida> lista;
        if (numeroOrdenFiltro == null || numeroOrdenFiltro.trim().isEmpty()) {
            lista = salidaDAO.listarSalidas();
        } else {
            // aplicar filtro simple haciendo listar y filtrar en Java (sencillo) o crear DAO con filtro
            lista = salidaDAO.listarSalidas();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        for (Salida s : lista) {
            // si hay filtro, aplicarlo aquí (por numeroOrden)
            if (numeroOrdenFiltro != null && !numeroOrdenFiltro.trim().isEmpty()) {
                String nro = s.getNumeroOrden() == null ? "" : s.getNumeroOrden();
                if (!nro.toLowerCase().contains(numeroOrdenFiltro.trim().toLowerCase())) continue;
            }

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
                s.getObservaciones() != null ? s.getObservaciones() : "",
                fechaStr,
                btnPdf,
                btnEliminar,
                btnModificar
            });
        }
    }

    // ---------------------- Generar PDF de una salida (iText) ----------------------
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

    // ---------------------- Eliminar una salida ----------------------
    private void eliminarSalidaPorId(int idSalida) {
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar esta salida? Se restituirá el stock (si tus triggers lo hacen).", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;

        boolean ok = salidaDAO.eliminarSalida(idSalida);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Salida eliminada correctamente");
            cargarDatosTabla(txtGuiaRemisionBuscar1.getText().trim());
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar salida");
        }
    }

    // ---------------------- Modificar una salida (dialogo simple) ----------------------
    private void editarSalidaDialog(int idSalida) {
        Salida s = salidaDAO.obtenerSalidaPorId(idSalida);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Salida no encontrada");
            return;
        }

        // Pedir nuevos valores con dialogs; puedes reemplazar por un formulario más amigable
        String nuevoProducto = (String) JOptionPane.showInputDialog(this,
                "Producto (nombre):", "Editar producto", JOptionPane.PLAIN_MESSAGE,
                null, null, s.getNombreProducto());
        if (nuevoProducto == null) return; // canceló

        String cantidadStr = JOptionPane.showInputDialog(this, "Cantidad:", s.getCantidadSalida());
        if (cantidadStr == null) return;
        double nuevaCantidad;
        try {
            nuevaCantidad = Double.parseDouble(cantidadStr);
            if (nuevaCantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida");
            return;
        }

        String nuevoNumeroOrden = JOptionPane.showInputDialog(this, "Número de orden:", s.getNumeroOrden() != null ? s.getNumeroOrden() : "");
        if (nuevoNumeroOrden == null) return;

        String nuevoDestino = JOptionPane.showInputDialog(this, "Destino:", s.getDestino() != null ? s.getDestino() : "");
        if (nuevoDestino == null) return;

        String nuevasObs = JOptionPane.showInputDialog(this, "Observaciones:", s.getObservaciones() != null ? s.getObservaciones() : "");
        if (nuevasObs == null) return;

        // convertir nombreProducto a idProducto usando DAO
        int idProducto = salidaDAO.obtenerIdProductoPorNombre(nuevoProducto);
        if (idProducto == -1) {
            JOptionPane.showMessageDialog(this, "Producto no válido o no activo");
            return;
        }

        // actualizar objeto
        s.setIdProducto(idProducto);
        s.setCantidadSalida(nuevaCantidad);
        s.setNumeroOrden(nuevoNumeroOrden.isEmpty() ? null : nuevoNumeroOrden);
        s.setDestino(nuevoDestino);
        s.setObservaciones(nuevasObs);

        boolean ok = salidaDAO.actualizarSalida(s);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Salida actualizada correctamente");
            cargarDatosTabla(txtGuiaRemisionBuscar1.getText().trim());
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar salida");
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

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblListaSalidas = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        txtGuiaRemisionBuscar1 = new javax.swing.JTextField();
        btnDescargarTodo = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tblListaSalidas.setModel(new javax.swing.table.DefaultTableModel(
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
        tblListaSalidas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListaSalidasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblListaSalidas);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        jLabel4.setText("Numero de orden");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnDescargarTodo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pdf_2.png"))); // NOI18N
        btnDescargarTodo.setText("Descargar todo");
        btnDescargarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarTodoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(0, 101, Short.MAX_VALUE))
                            .addComponent(txtGuiaRemisionBuscar1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(btnDescargarTodo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGuiaRemisionBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(269, 269, 269)
                .addComponent(btnDescargarTodo)
                .addContainerGap(265, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String filtro = txtGuiaRemisionBuscar1.getText().trim();
        cargarDatosTabla(filtro);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnDescargarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarTodoActionPerformed
  
     // Mantengo tu función actual para generar PDF de la tabla completa
        PDF_tabla pdf = new PDF_tabla();
        pdf.generarPDF_TablaCompleta(tblListaSalidas, "salidas");

    }//GEN-LAST:event_btnDescargarTodoActionPerformed

    private void tblListaSalidasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListaSalidasMouseClicked
        int fila = tblListaSalidas.rowAtPoint(evt.getPoint());
        int col = tblListaSalidas.columnAtPoint(evt.getPoint());

        if (fila == -1) return;

        int idSalida = (int) tblListaSalidas.getValueAt(fila, 0);

        // PDF (col 7)
        if (col == 7) {
            Salida s = salidaDAO.obtenerSalidaPorId(idSalida);
            generarPDF_Salida(s);
            return;
        }

        // Eliminar (col 8)
        if (col == 8) {
            eliminarSalidaPorId(idSalida);
            return;
        }

        // Modificar (col 9)
        if (col == 9) {
            editarSalidaDialog(idSalida);
            return;
        }

    }//GEN-LAST:event_tblListaSalidasMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDescargarTodo;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblListaSalidas;
    private javax.swing.JTextField txtGuiaRemisionBuscar1;
    // End of variables declaration//GEN-END:variables



}
