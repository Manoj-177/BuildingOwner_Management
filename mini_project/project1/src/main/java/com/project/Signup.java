package com.project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/Signup")
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String url = "jdbc:mysql://localhost:3306/project1"; 
		String dbUser = "root";
		String dbPassword = "manoj123";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);

			// Step 1: Check if username already exists
			String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
			PreparedStatement checkStmt = conn.prepareStatement(checkSql);
			checkStmt.setString(1, name);
			ResultSet rs = checkStmt.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			rs.close();
			checkStmt.close();

			if (count > 0) {
				out.println("<h2>Username already exists. Please choose a different one.</h2>");
			} else {
				// Step 2: Insert new user
				String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, name);
				stmt.setString(2, email);
				stmt.setString(3, password);
				int rows = stmt.executeUpdate();

				if (rows > 0) {
					response.sendRedirect("index.html");
				} else {
					out.println("<h2>Signup Failed. Please try again.</h2>");
				}
				stmt.close();
			}

			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
			out.println("<h2>Error: " + e.getMessage() + "</h2>");
		}
	}
}
