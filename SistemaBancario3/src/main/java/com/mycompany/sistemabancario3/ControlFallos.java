/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemabancario3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Germán
 */
class HiloComunicacion extends Thread {

    Socket canal;


    public HiloComunicacion(Socket s) {
        canal = s;
    }

    @Override
    public void run() {
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(canal.getInputStream()));
            PrintWriter pw = new PrintWriter(canal.getOutputStream(), true);
            String mensaje = bf.readLine();
            System.out.println(mensaje + "");
            if (mensaje.compareTo("Hola!") == 0) {
                pw.println("Hola!");
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloComunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class Saludar extends TimerTask {
    String idControlar;
    String id;
    HiloComunicacion canal;
    static List<String> idsProceso = new ArrayList<>();
    static String idCoordinador = null;
    public Saludar(String id, String idControl) {
        this.id = id;
        idControlar = idControl;
        idsProceso.add(id); // Agregar cada proceso al anillo
    }

    @Override
    public void run() {
        if (idControlar.compareTo(id) == 0) {
            try {
                ServerSocket server = new ServerSocket(25001);
                while (true) {
                    Socket s = server.accept();
                    canal = new HiloComunicacion(s);
                    canal.start();                    
                }
            } catch (IOException ex) {
                Logger.getLogger(Saludar.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try { 
                Socket s = new Socket(InetAddress.getByName("127.0.0.1"), 25001);

                // Leer mensaje del servidor
                BufferedReader bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String mensaje = bf.readLine();
                System.out.println("Mensaje recibido: " + mensaje);
                
                if (mensaje.compareTo("Hola!") != 0) {
                    System.out.println("Servidor caído..."); 
                }
            } catch (UnknownHostException ex) {                
                Logger.getLogger(Saludar.class.getName()).log(Level.SEVERE, null, ex);
            } catch(SocketException ex){
                //System.out.println("Servidor caído"); 
                invocarAlgoritmoAnillo();
            } catch (IOException ex) {                
                Logger.getLogger(Saludar.class.getName()).log(Level.SEVERE, null, ex);
            } }
        
    }
     private void activarServidorAlternativo() {
         try {
                ServerSocket server = new ServerSocket(25200);
                while (true) {
                    Socket s = server.accept();
                    canal = new HiloComunicacion(s);
                    canal.start();
                }
            } catch (IOException ex) {
                Logger.getLogger(Saludar.class.getName()).log(Level.SEVERE, null, ex);
            } 
    }
    private void invocarAlgoritmoAnillo() { 
        System.out.println("Iniciando el algoritmo de anillo para elegir un nuevo coordinador.");
        idsProceso.add("2");
        List<String> idsEleccion = new ArrayList<>(idsProceso);
        
        // Determinar el nuevo coordinador basado en el id más alto
        String nuevoCoordinador = idsEleccion.stream().max(String::compareTo).orElse(null);
        if (nuevoCoordinador != null) {
            idCoordinador = nuevoCoordinador;
            System.out.println("Nuevo coordinador elegido: " + idCoordinador);
            // Activar el nuevo servidor coordinador
            if (id.equals(idCoordinador)) {
                activarServidorAlternativo();
            }
        }
    }
}
 

public class ControlFallos extends Thread {

    Timer planificador;
    Saludar tareaSaludar;
    String idControlar;
    String id;

    public ControlFallos(String id, String idControl) {
        this.id = id;
        idControlar = idControl;
        planificador = new Timer();
        
        tareaSaludar = new Saludar(this.id, idControlar);
    }

    @Override
    public void run() {
        planificador.schedule(tareaSaludar, 0, 2000);
    }
}
