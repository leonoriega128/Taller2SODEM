/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cajeroautomatico;

import com.mycompany.sistemabancario.BufferMensajeRespuestaSB;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

 
public class HiloServidor extends Thread{
    
    ServerSocket ss;
    BufferMensajeRespuestaSB bmr;

    public HiloServidor(BufferMensajeRespuestaSB mr) {
        try {
            ss = new ServerSocket(40000);
            bmr = mr;
        } catch (IOException ex) {
            Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        while(true){
            try {
                Socket sock = ss.accept();
                ManejoComunicacion canal = new ManejoComunicacion(sock, bmr);
                canal.start();
            } catch (IOException ex) {
                Logger.getLogger(HiloServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
