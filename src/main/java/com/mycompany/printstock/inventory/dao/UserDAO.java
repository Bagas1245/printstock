package com.mycompany.printstock.inventory.dao;

import com.mycompany.printstock.inventory.model.User; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User login(String username, String password) {
        User user = null;
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        Connection conn = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setNama(rs.getString("nama"));      
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
            }
            
            rs.close();

        } catch (SQLException e) {
            System.err.println("Error saat proses login: " + e.getMessage());
            e.printStackTrace();
        }

        return user;
    }
}