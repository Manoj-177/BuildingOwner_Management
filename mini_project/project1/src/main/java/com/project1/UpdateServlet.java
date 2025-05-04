package com.project1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/UpdateServlet")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Step 1: Get session and username
        HttpSession session = request.getSession(false); // don't create a new session
        String username = null;
        if (session != null) {
            username = (String) session.getAttribute("username");
        }
        if (username == null) {
            out.println("<h2 style='color:red;'>You must be logged in to update data.</h2>");
            out.println("<a href='login.html'>Go to Login</a>");
            return;
        }

        String id = request.getParameter("id");
        
        // Form values (may be empty)
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String room = request.getParameter("room");
        String status = request.getParameter("status");
        String rentFrom = request.getParameter("rent_from");
        String rentTo = request.getParameter("rent_to");

        String url = "jdbc:mysql://localhost:3306/project1";
        String dbUser = "root";
        String dbPassword = "manoj123";

        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;

        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, dbUser, dbPassword);
            
            // Prepare select query
            stmt = conn.prepareStatement("SELECT * FROM userdata WHERE id = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                // If field is blank, use existing value
                if (name == null || name.trim().isEmpty()) name = rs.getString("name");
                if (phone == null || phone.trim().isEmpty()) phone = rs.getString("phone");
                if (email == null || email.trim().isEmpty()) email = rs.getString("email");
                if (room == null || room.trim().isEmpty()) room = rs.getString("room");
                if (status == null || status.trim().isEmpty()) status = rs.getString("status");
                if (rentFrom == null || rentFrom.trim().isEmpty()) rentFrom = rs.getString("rent_from");
                if (rentTo == null || rentTo.trim().isEmpty()) rentTo = rs.getString("rent_to");
            } else {
                out.println("<h3 style='color:red;'>Tenant with ID " + id + " not found.</h3>");
                out.println("<form action='body.html'>");
                out.println("<button type='submit'>Go Back</button>");
                out.println("</form>");
                return;
            }

            // Prepare update query
            updateStmt = conn.prepareStatement("UPDATE userdata SET name=?, phone=?, email=?, room=?, status=?, rent_from=?, rent_to=? WHERE id=?");
            updateStmt.setString(1, name);
            updateStmt.setString(2, phone);
            updateStmt.setString(3, email);
            updateStmt.setString(4, room);
            updateStmt.setString(5, status);
            updateStmt.setString(6, rentFrom);
            updateStmt.setString(7, rentTo);
            updateStmt.setString(8, id);

            int updated = updateStmt.executeUpdate();

            if (updated > 0) {
                out.println("<h3 style='color:green;'>Tenant details updated successfully.</h3>");
                out.println("<form action='body.html'>");
                out.println("<button type='submit'>Go Back</button>");
                out.println("</form>");
                
            } else {
                out.println("<h3 style='color:red;'>Update failed. Please try again.</h3>");
                out.println("<form action='body.html'>");
                out.println("<button type='submit'>Go Back</button>");
                out.println("</form>");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        } 
    }
}
