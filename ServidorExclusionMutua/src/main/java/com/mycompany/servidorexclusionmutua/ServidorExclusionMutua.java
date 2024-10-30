/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.servidorexclusionmutua;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

class HiloComunicacion extends Thread {

    Socket s;
    BufferedReader br;
    BufferMensajeRespuestaSEM mr;

    public HiloComunicacion(Socket s, BufferMensajeRespuestaSEM bmr) {        
        try {
            this.s = s;
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            mr = bmr;
        } catch (IOException ex) {
            Logger.getLogger(HiloComunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

    @Override
    public void run() {
        try {
            /*
            * Tipos de mensajes esperados:
            * GetToken,ip
            * DevolverToken,ip
             */
            String mensaje = br.readLine();
            String[] strs = mensaje.split(",");
            String operacion = strs[0];

            switch (operacion) {
                case "GetToken":
                    mr.RecibirSolicitud(strs[1]);
                    break;
                case "DevolverToken":
                    mr.RecibirToken();
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloComunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

/**
 *
 * @author Germán
 */
public class ServidorExclusionMutua {

    public static void main(String[] args) {
        try {
            boolean token = true;
            Queue<String> cola = new LinkedList<>();
            ServerSocket server = new ServerSocket(30000);
            BufferMensajeRespuestaSEM bmr = new BufferMensajeRespuestaSEM(token, cola);
            while (true) {
                Socket s = server.accept();
                HiloComunicacion canal = new HiloComunicacion(s, bmr);
                canal.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorExclusionMutua.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
