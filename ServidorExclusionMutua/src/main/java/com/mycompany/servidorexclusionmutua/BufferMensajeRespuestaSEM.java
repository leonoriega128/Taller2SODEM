/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.servidorexclusionmutua;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Germán
 */
public class BufferMensajeRespuestaSEM {
    boolean token;
    Queue<String> solicitudes;

    public BufferMensajeRespuestaSEM(boolean token, Queue<String> solicitudes) {
        this.token = token;
        this.solicitudes = solicitudes;
    }
    
    public void RecibirSolicitud(String ipYPuerto) {
        if (token) {
            // Proveo token
            this.EnviarToken(ipYPuerto);
        } else {
            // Colocar solicitud en cola
            solicitudes.add(ipYPuerto);
        }
    }

    public void RecibirToken() {
        token = true;
        if (!solicitudes.isEmpty()) {
            String ipOtorgarToken = solicitudes.remove();
            this.EnviarToken(ipOtorgarToken);
        }
    }

    public void EnviarToken(String ipYPuerto) {
        try {
            String[] ipPuerto = ipYPuerto.split(":");
            String ip = ipPuerto[0];
            String puerto = ipPuerto[1];
            Socket sA = new Socket(InetAddress.getByName(ip), Integer.parseInt(puerto));
            PrintWriter pwSA = new PrintWriter(sA.getOutputStream(), true);
            pwSA.println("token,1");
        } catch (UnknownHostException ex) {
            Logger.getLogger(BufferMensajeRespuestaSEM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BufferMensajeRespuestaSEM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
