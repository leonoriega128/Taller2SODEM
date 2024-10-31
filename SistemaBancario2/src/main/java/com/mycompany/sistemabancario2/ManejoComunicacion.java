/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemabancario2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Germán
 */
public class ManejoComunicacion extends Thread {

    Socket s;
    Fachada f;
    BufferMensajeRespuestaSB bmr;
    String ip, puerto;

    public ManejoComunicacion(Socket sc, BufferMensajeRespuestaSB mr, String ip, String puerto) {
        s = sc;
        this.ip = ip;
        this.puerto = puerto;
        bmr = mr;
        f = new Fachada(bmr, ip, puerto);        
    }

    @Override
    public void run() {
        try {
            BufferedReader bf;
            PrintWriter pw;
            bf = new BufferedReader(new InputStreamReader(s.getInputStream()));
            // Devuelve 0 cuando no se pudo ejecutar la operación
            pw = new PrintWriter(s.getOutputStream(), true);
            String cadena = bf.readLine();
            String[] entrada = cadena.split(",");

            System.out.println(cadena);
            /*
            * extraer, idCuenta, monto
            * depositar, idCuenta, monto            
            * transferir, idCuentaOrigen, idCuentaDestino, monto
            * 
            * token,1
             */
            switch (entrada[0]) {
                case "extraer":
                    f.Extraer(Integer.parseInt(entrada[1]),
                            Float.parseFloat(entrada[2]));
                    pw.println(1);

                    break;
                case "depositar":
                    f.Depositar(Integer.parseInt(entrada[1]),
                            Float.parseFloat(entrada[2]));
                    pw.println(1);
                    break;
                case "transferir":
                    f.Transferir(Integer.parseInt(entrada[1]),
                            Integer.parseInt(entrada[2]),
                            Float.parseFloat(entrada[3]));
                    pw.println(1);
                    break;
                    
                case "token":
                    bmr.RecibirToken();
                    break;
                default:
                    //Operación desconocida.
                    pw.println(0);
            }
        } catch (IOException ex) {
            Logger.getLogger(ManejoComunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
