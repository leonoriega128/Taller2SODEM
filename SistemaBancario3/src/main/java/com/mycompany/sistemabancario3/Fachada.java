/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemabancario3;

import DAO.CuentaDAO;
import DAO.MovimientoDAO;
import DTO.CuentaDTO;
import DTO.MovimientoDTO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Germán
 */
public class Fachada {
    
    File archivo;
    FileOutputStream fileOutputStream;
    BufferMensajeRespuestaSB bmr;
    String ip;
    String puerto;

    public Fachada(BufferMensajeRespuestaSB mr, String ip, String puerto) {
        try {
            this.ip = ip;
            this.puerto = puerto;
            bmr = mr;
            archivo = new File("C:\\logs.txt");
            fileOutputStream = new FileOutputStream(archivo, true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Depositar(int idCuenta, float monto){
        try {
            CuentaDTO cuenta = new CuentaDTO();
            CuentaDAO cuentaDAO = new CuentaDAO();
            MovimientoDTO mov = new MovimientoDTO();
            MovimientoDAO movDAO = new MovimientoDAO();
            
            cuenta.setId(idCuenta);
            cuenta = cuentaDAO.RecuperarCuenta(cuenta);
            cuentaDAO.ActualizarSaldo(monto, cuenta);
            mov.setCuentas_id(idCuenta);
            mov.setFecha(new Date());
            mov.setMonto(monto);
            mov.setTipoMovimiento("Depósito");
            movDAO.AgregarMovimiento(mov);
            
            bmr.SolicitarToken(ip+":"+puerto);
            String s = "En Sistema Bancario: \n";
            s += "Se deposito en cuenta " + idCuenta +
                    " la cantidad de pesos " + monto + "\n";
            fileOutputStream.write(s.getBytes());
            fileOutputStream.close();
            bmr.DevolverToken();
        } catch (IOException ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Transferir(int idCuentaExtraer, int idCuentaDepositar, float monto){
        try {
            CuentaDTO cuentaExtraer = new CuentaDTO();
            CuentaDTO cuentaDepositar = new CuentaDTO();
            CuentaDAO cuentaDAO = new CuentaDAO();
            MovimientoDTO mov1 = new MovimientoDTO();
            MovimientoDTO mov2 = new MovimientoDTO();
            MovimientoDAO movDAO = new MovimientoDAO();
            
            cuentaExtraer.setId(idCuentaExtraer);
            cuentaExtraer = cuentaDAO.RecuperarCuenta(cuentaExtraer);
            cuentaDepositar.setId(idCuentaDepositar);
            cuentaDepositar = cuentaDAO.RecuperarCuenta(cuentaDepositar);
            
            cuentaDAO.ActualizarSaldo(monto * -1, cuentaExtraer);
            cuentaDAO.ActualizarSaldo(monto, cuentaDepositar);
            
            mov1.setCuentas_id(idCuentaExtraer);
            mov1.setFecha(new Date());
            mov1.setMonto(monto * -1);
            mov1.setTipoMovimiento("Transferencia");
            movDAO.AgregarMovimiento(mov1);
            
            mov2.setCuentas_id(idCuentaDepositar);
            mov2.setFecha(new Date());
            mov2.setMonto(monto);
            mov2.setTipoMovimiento("Transferencia");
            movDAO.AgregarMovimiento(mov1);
            
            bmr.SolicitarToken(ip+":"+puerto);
            String s = "En Sistema Bancario: \n";
            s += "Se transfirió de cuenta " + idCuentaExtraer +
                    " la cantidad de pesos " + monto + " ";
            s += "a la cuenta " + idCuentaDepositar + "\n";
            fileOutputStream.write(s.getBytes());
            fileOutputStream.close();
            bmr.DevolverToken();
        } catch (IOException ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Extraer(int idCuenta, float monto){
        try {
            CuentaDTO cuenta = new CuentaDTO();
            CuentaDAO cuentaDAO = new CuentaDAO();
            MovimientoDTO mov = new MovimientoDTO();
            MovimientoDAO movDAO = new MovimientoDAO();
            
            cuenta.setId(idCuenta);
            cuenta = cuentaDAO.RecuperarCuenta(cuenta);
            cuentaDAO.ActualizarSaldo(monto * -1, cuenta);
            mov.setCuentas_id(idCuenta);
            mov.setFecha(new Date());
            mov.setMonto(monto * -1);
            mov.setTipoMovimiento("Extracción");
            movDAO.AgregarMovimiento(mov);
            
            bmr.SolicitarToken(ip+":"+puerto);
            String s = "En Sistema Bancario: \n";
            s += "Se extrajo de cuenta " + idCuenta +
                    " la cantidad de pesos " + monto + "\n";
            fileOutputStream.write(s.getBytes());
            fileOutputStream.close();
            bmr.DevolverToken();
        } catch (IOException ex) {
            Logger.getLogger(Fachada.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
