/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemabancario;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

 
public class BufferMensajeRespuestaSB {
    
    Semaphore sem;

    public BufferMensajeRespuestaSB() {
        sem = new Semaphore(0);
    }

    public void SolicitarToken(String ipYPuerto) {
        try {
            Socket sA = new Socket(InetAddress.getByName("127.0.0.1"), 30000);
            PrintWriter pwSA = new PrintWriter(sA.getOutputStream(), true);
            pwSA.println("GetToken," + ipYPuerto);
            sem.acquire();
        } catch (UnknownHostException ex) {
            Logger.getLogger(BufferMensajeRespuestaSB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BufferMensajeRespuestaSB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(BufferMensajeRespuestaSB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void RecibirToken() {
        sem.release();
    }

    public void DevolverToken() {
        try {
            Socket sA = new Socket(InetAddress.getByName("127.0.0.1"), 30000);
            PrintWriter pwSA = new PrintWriter(sA.getOutputStream(), true);
            pwSA.println("DevolverToken,-");
        } catch (UnknownHostException ex) {
            Logger.getLogger(BufferMensajeRespuestaSB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BufferMensajeRespuestaSB.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
