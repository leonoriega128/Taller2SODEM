/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemabancario3;

import DAO.UsuarioDAO;
import DTO.UsuarioDTO;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Germán
 */
public class SistemaBancario3 {

    public static void main(String[] args) {
        
        try {
            ControlFallos cf = new ControlFallos("3", "1");            
            cf.start();
            ServerSocket ss = new ServerSocket(25900);
            BufferMensajeRespuestaSB bmr = new BufferMensajeRespuestaSB();
            while(true){
                System.out.println("Esperando conexiones");
                Socket s = ss.accept();
                ManejoComunicacion tC = new ManejoComunicacion(s, bmr, "127.0.0.1", "25900");
                tC.start();
            }                        
        } catch (IOException ex) {
            Logger.getLogger(SistemaBancario3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
