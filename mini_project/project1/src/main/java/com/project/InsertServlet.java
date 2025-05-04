package com.project;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/InsertServlet")
public class InsertServlet extends HttpServlet {
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
            out.println("<h2 style='color:red;'>You must be logged in to insert data.</h2>");
            out.println("<a href='login.html'>Go to Login</a>");
            return;
        }

        // Step 2: Get parameters
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String room = request.getParameter("room");
        String status = request.getParameter("status");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");

        // Step 3: DB Connection and insert
        String url = "jdbc:mysql://localhost:3306/project1";
        String dbUser = "root";
        String dbPassword = "manoj123";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);

            String sql = "INSERT INTO userdata (username, name, phone, email, room, status, rent_from, rent_to) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username); // logged-in user
            stmt.setString(2, name);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, room);
            stmt.setString(6, status);
            stmt.setString(7, fromDate);
            stmt.setString(8, toDate);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                out.println("<html><body>");
                out.println("<h2 style='color:green;'>Data inserted successfully!</h2>");
                out.println("<br>");
                out.println("<form action='body.html'>");
                out.println("<button type='submit'>Go Back</button>");
                out.println("</form>");
                out.println("</body></html>");
            }
            stmt.close();
            conn.close();

        } catch (SQLException e) {
                out.println("<h2 style='color:red;'>Database Error: " + e.getMessage() + "</h2>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2 style='color:red;'>Error: " + e.getMessage() + "</h2>");
        }
    }
}
