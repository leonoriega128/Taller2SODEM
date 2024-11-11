/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import DTO.UsuarioDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Germán
 */
public class UsuarioDAO {
    public void AgregarUsuario(UsuarioDTO usuario){
        
        try {
            Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3308/dbbanco",
                    "root", "root");
            String sql = "INSERT INTO usuarios (nombre, apellido, username, password) "
                    + "VALUES (?,?,?,?)";
            PreparedStatement stmt = cnx.prepareStatement(sql);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getUsername());
            stmt.setString(4, usuario.getPassword());
            stmt.executeUpdate();
            
            stmt = cnx.prepareStatement("SELECT MAX(id) FROM usuarios");
            ResultSet rs = stmt.executeQuery();
            rs.next();
            usuario.setId(rs.getInt(1));
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ActualizarPassword(UsuarioDTO usuario){
    
        try {
            Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3308/dbbanco",
                    "root", "root");
            String sql = "UPDATE usuarios SET password=? WHERE id = ?";
            PreparedStatement pstmt = cnx.prepareStatement(sql);
            pstmt.setString(1, usuario.getPassword());
            pstmt.setInt(2, usuario.getId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void EliminarUsuario(UsuarioDTO usuario){
        try {
            Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3308/dbbanco",
                    "root", "root");
            PreparedStatement pstmt = cnx.prepareStatement("DELETE FROM usuarios WHERE id = ?");
            pstmt.setInt(1, usuario.getId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<UsuarioDTO> RecuperarUsuarios(String cadenaBusqueda){
        List<UsuarioDTO> usuarios = new ArrayList<>();
        try {
            Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3308/dbbanco",
                    "root", "root");
            PreparedStatement pstmt = cnx.prepareStatement("SELECT * FROM "
                    + "usuarios WHERE nombre LIKE ? OR apellido LIKE ? OR "
                    + "username LIKE ?");
            pstmt.setString(1, "%" + cadenaBusqueda + "%");
            pstmt.setString(2, "%" + cadenaBusqueda + "%");
            pstmt.setString(3, "%" + cadenaBusqueda + "%");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                UsuarioDTO usuario = new UsuarioDTO();
                usuario.setId(rs.getInt(1));
                usuario.setNombre(rs.getString(2));
                usuario.setApellido(rs.getString(3));
                usuario.setUsername(rs.getString(4));
                usuario.setPassword(rs.getString(5));
                usuarios.add(usuario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarios;
    }
}
