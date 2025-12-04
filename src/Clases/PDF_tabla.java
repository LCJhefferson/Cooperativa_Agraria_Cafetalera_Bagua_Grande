/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author jheff
 */
public class PDF_tabla {
 public void generarPDF_FilaTabla(JTable tabla, int fila, String tituloReporte) {
    Document documento = new Document();
    try {
        if (fila < 0 || fila >= tabla.getRowCount()) {
            JOptionPane.showMessageDialog(null, "La fila seleccionada no es válida.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar " + tituloReporte.toLowerCase());
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos PDF", "pdf");
        fileChooser.setFileFilter(filtro);

        int seleccion = fileChooser.showSaveDialog(null);
        if (seleccion != JFileChooser.APPROVE_OPTION) return;

        File archivoSeleccionado = fileChooser.getSelectedFile();
        String ruta = archivoSeleccionado.getAbsolutePath();
        if (!ruta.toLowerCase().endsWith(".pdf")) {
            ruta += ".pdf";
        }

        PdfWriter.getInstance(documento, new FileOutputStream(ruta));
        documento.open();

        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
        Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.LIGHT_GRAY);
        Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        Font fuentePequena = FontFactory.getFont(FontFactory.HELVETICA, 8);

        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        Paragraph titulo = new Paragraph("REPORTE DE " + tituloReporte.toUpperCase() + "\n", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);

        Paragraph subtitulo = new Paragraph("Generado el: " + fechaHora + "\n\n", fuenteSubtitulo);
        subtitulo.setAlignment(Element.ALIGN_RIGHT);
        documento.add(subtitulo);

        documento.add(new Paragraph("Datos de la fila seleccionada:\n\n", fuentePequena));

        int columnas = tabla.getColumnCount();
        List<Integer> columnasValidas = new ArrayList<>();
        for (int c = 0; c < columnas; c++) {
            String nombre = tabla.getColumnName(c);
            if (!nombre.equalsIgnoreCase("PDF") &&
                !nombre.equalsIgnoreCase("ELIMINAR") &&
                !nombre.equalsIgnoreCase("MODIFICAR")) {
                columnasValidas.add(c);
            }
        }

        PdfPTable tablaPDF = new PdfPTable(columnasValidas.size());
        tablaPDF.setWidthPercentage(100);
        tablaPDF.setSpacingBefore(15f);
        tablaPDF.setSpacingAfter(15f);

        for (int c : columnasValidas) {
            PdfPCell encabezado = new PdfPCell(new Phrase(tabla.getColumnName(c), fuenteEncabezado));
            encabezado.setBackgroundColor(new BaseColor(102,153,0)); // Verde institucional
            encabezado.setHorizontalAlignment(Element.ALIGN_CENTER);
            encabezado.setBorderWidth(1f);
            tablaPDF.addCell(encabezado);
        }

        for (int c : columnasValidas) {
            Object valor = tabla.getValueAt(fila, c);
            String texto = (valor instanceof JButton) ? ((JButton) valor).getText() : (valor != null ? valor.toString() : "");
            PdfPCell celda = new PdfPCell(new Phrase(texto, fuentePequena));
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setBorderWidth(0.5f);
            tablaPDF.addCell(celda);
        }

        documento.add(tablaPDF);

        Paragraph pie = new Paragraph("Cooperativa Agraria Cafetalera Bagua Grande",
            FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, BaseColor.GRAY));
        pie.setAlignment(Element.ALIGN_CENTER);
        documento.add(pie);

        JOptionPane.showMessageDialog(null, "PDF generado correctamente:\n" + ruta);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al generar PDF: " + e.getMessage());
        e.printStackTrace();
    } finally {
        if (documento.isOpen()) {
            documento.close();
        }
    }
}

 
  
   // Método para exportar TODA la tabla
    public void generarPDF_TablaCompleta(JTable tabla, String tituloReporte) {
        Document documento = new Document();
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar reporte de " + tituloReporte.toLowerCase());
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos PDF", "pdf");
            fileChooser.setFileFilter(filtro);

            int seleccion = fileChooser.showSaveDialog(null);
            if (seleccion != JFileChooser.APPROVE_OPTION) return;

            File archivoSeleccionado = fileChooser.getSelectedFile();
            String ruta = archivoSeleccionado.getAbsolutePath();
            if (!ruta.toLowerCase().endsWith(".pdf")) {
                ruta += ".pdf";
            }

            PdfWriter.getInstance(documento, new FileOutputStream(ruta));
            documento.open();

            Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
            Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
            Font fuentePequena = FontFactory.getFont(FontFactory.HELVETICA, 8);

            String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            // Título
            Paragraph titulo = new Paragraph("REPORTE COMPLETO DE " + tituloReporte.toUpperCase() + "\n", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            // Subtítulo
            Paragraph subtitulo = new Paragraph("Generado el: " + fechaHora + "\n\n", fuenteSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_RIGHT);
            documento.add(subtitulo);

            // --- LÓGICA DE TABLA ---
            int columnas = tabla.getColumnCount();
            List<Integer> columnasValidas = new ArrayList<>();
            for (int c = 0; c < columnas; c++) {
                String nombre = tabla.getColumnName(c);
                if (!nombre.equalsIgnoreCase("PDF") &&
                    !nombre.equalsIgnoreCase("ELIMINAR") &&
                    !nombre.equalsIgnoreCase("MODIFICAR")) {
                    columnasValidas.add(c);
                }
            }

            PdfPTable tablaPDF = new PdfPTable(columnasValidas.size());
            tablaPDF.setWidthPercentage(100);
            tablaPDF.setSpacingBefore(15f);
            tablaPDF.setSpacingAfter(15f);

            // Encabezados
            for (int c : columnasValidas) {
                PdfPCell encabezado = new PdfPCell(new Phrase(tabla.getColumnName(c), fuenteEncabezado));
                encabezado.setBackgroundColor(new BaseColor(102,153,0)); // Verde institucional
                encabezado.setHorizontalAlignment(Element.ALIGN_CENTER);
                encabezado.setBorderWidth(1f);
                tablaPDF.addCell(encabezado);
            }

            // Todas las filas
            int filas = tabla.getRowCount();
            for (int f = 0; f < filas; f++) {
                for (int c : columnasValidas) {
                    Object valor = tabla.getValueAt(f, c);
                    String texto = (valor instanceof JButton) ? ((JButton) valor).getText() : (valor != null ? valor.toString() : "");
                    PdfPCell celda = new PdfPCell(new Phrase(texto, fuentePequena));
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda.setBorderWidth(0.5f);
                    tablaPDF.addCell(celda);
                }
            }

            documento.add(tablaPDF);

            // Pie
            Paragraph pie = new Paragraph("Cooperativa Agraria Cafetalera Bagua Grande",
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, BaseColor.GRAY));
            pie.setAlignment(Element.ALIGN_CENTER);
            documento.add(pie);

            documento.close();

            JOptionPane.showMessageDialog(null, "PDF generado correctamente en:\n" + ruta);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar PDF: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
 
 
    }
}
 
 
 
 
 
 
 
 
