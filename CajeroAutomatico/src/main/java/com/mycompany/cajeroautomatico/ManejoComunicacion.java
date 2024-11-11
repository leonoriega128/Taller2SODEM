/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cajeroautomatico;

import com.mycompany.sistemabancario.BufferMensajeRespuestaSB;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

 
public class ManejoComunicacion extends Thread{
    
    Socket sock;
    BufferMensajeRespuestaSB bmr;

    public ManejoComunicacion(Socket sock, BufferMensajeRespuestaSB mr) {
        this.sock = sock;
        bmr = mr;
    }
   
    @Override
    public void run(){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String cadena = br.readLine();
            String[] entrada = cadena.split(",");
            
            switch(entrada[0]){
                case "token":
                    bmr.RecibirToken();
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(ManejoComunicacion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(ManejoComunicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
