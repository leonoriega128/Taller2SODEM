/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DTO.CuentaDTO;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Germán
 */
public class CuentaDAO {
    public void ActualizarSaldo(float valor, CuentaDTO cuenta){
        try {
            Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3308/dbbanco",
                    "root", "root");
            String sql = "SELECT saldo FROM cuentas WHERE id=?";
            PreparedStatement pstmt = cnx.prepareStatement(sql);
            pstmt.setInt(1, cuenta.getId());
            ResultSet res = pstmt.executeQuery();
            res.next();
            float saldo = res.getFloat("saldo");
            saldo += valor;
            
            sql = "UPDATE cuentas SET saldo=? WHERE id=?";
            pstmt = cnx.prepareStatement(sql);
            pstmt.setFloat(1, saldo);
            pstmt.setInt(2, cuenta.getId());
            pstmt.executeUpdate();
            cuenta.setSaldo(saldo);
        } catch (SQLException ex) {
            Logger.getLogger(CuentaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public CuentaDTO RecuperarCuenta(CuentaDTO cuenta){
        CuentaDTO c = null;
        try {
            Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3308/dbbanco",
                    "root", "root");
            String sql = "SELECT * FROM cuentas WHERE id=?";
            PreparedStatement pstmt = cnx.prepareStatement(sql);
            pstmt.setInt(1, cuenta.getId());
            ResultSet res = pstmt.executeQuery();
            if (res.next()){
                c = new CuentaDTO();
                c.setId(res.getInt(1));
                c.setFechaApertura(res.getTimestamp(2));
                c.setSaldo(res.getFloat(3));
                c.setUsuarios_id(res.getInt(4));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CuentaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return c;
    }
    
    public void AgregarCuenta(CuentaDTO cuenta){
        try {
            Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3308/dbbanco",
                    "root", "root");
            String sql = "INSERT INTO cuentas (fechaApertura, saldo, usuarios_id) "
                    + "VALUES (?,?,?)";
            PreparedStatement pstmt = cnx.prepareStatement(sql);
            pstmt.setTimestamp(1, new java.sql.Timestamp(cuenta.getFechaApertura().getTime()));
            pstmt.setFloat(2, cuenta.getSaldo());
            pstmt.setInt(3, cuenta.getUsuarios_id());
            pstmt.executeUpdate();
            
            sql = "SELECT max(id) FROM movimientos";
            pstmt = cnx.prepareStatement(sql);
            ResultSet res = pstmt.executeQuery();
            if (res.next()){
                cuenta.setId(res.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CuentaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void EliminarCuenta(CuentaDTO cuenta){
        try {
           Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3308/dbbanco",
                    "root", "root");
            PreparedStatement stmt = cnx.prepareStatement("DELETE FROM cuentas WHERE id=?");
            stmt.setInt(1, cuenta.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CuentaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
