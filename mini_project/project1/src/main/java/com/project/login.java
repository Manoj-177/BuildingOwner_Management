package com.project;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String name = request.getParameter("user");
        String password = request.getParameter("password");
        
        String url = "jdbc:mysql://localhost:3306/project1"; 
        String dbUser = "root"; 
        String dbPassword = "manoj123"; 

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, password);

            ResultSet res = stmt.executeQuery();

            if (res.next()) {
                // ✅ Create session and store username
                HttpSession session = request.getSession();
                session.setAttribute("username", name); // store username for future use

                response.sendRedirect("body.html"); // or dashboard
            } else {
                out.println("<h2 style='color:red;'>Login Failed. Invalid username or password.</h2>");
                out.println("<a href='index.html'>Try Again</a>");
            }
            res.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
        }
	}
}
