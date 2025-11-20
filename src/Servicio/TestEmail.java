/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicio;

/**
 *
 * @author jheff
 */
public class TestEmail {
    public static void main(String[] args) {
        EmailService.enviarCorreo(
                "correo_destino@gmail.com",
                "Prueba de envío",
                "Esto es un correo de prueba desde Java + SMTP2GO"
        );
    }
}
