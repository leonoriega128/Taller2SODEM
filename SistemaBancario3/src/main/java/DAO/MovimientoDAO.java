/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DTO.CuentaDTO;
import DTO.MovimientoDTO;
import java.sql.DriverManager;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Germán
 */
public class MovimientoDAO {

    public void AgregarMovimiento(MovimientoDTO mov){
        try {
            Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3308/dbbanco",
                    "root", "root");
            String sql = "INSERT INTO movimientos (fecha, tipoMovimiento, monto, cuentas_id) "
                    + "VALUES (?,?,?,?)";            
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setTimestamp(1, new java.sql.Timestamp(mov.getFecha().getTime()));
            stmt.setString(2, mov.getTipoMovimiento());
            stmt.setFloat(3, mov.getMonto());
            stmt.setInt(4, mov.getCuentas_id());
            stmt.executeUpdate();
            
            stmt = cnx.prepareCall("SELECT max(id) FROM movimientos");
            ResultSet res = stmt.executeQuery();
            res.next();
            mov.setId(res.getInt(1));                        
        } catch (SQLException ex) {
            Logger.getLogger(MovimientoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void EliminarMovimiento(MovimientoDTO mov){
        try {
            Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbbanco",
                    "root", "SoD#2024");
            PreparedStatement pstmt = cnx.prepareStatement("DELETE FROM movimientos WHERE id=?");
            pstmt.setInt(1, mov.getId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MovimientoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
