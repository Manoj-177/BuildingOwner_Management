package com.project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/FilterServlet")
public class FilterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;

        if (username == null) {
            out.println("<h2 style='color:red;'>You must be logged in to view your data.</h2>");
            out.println("<a href='index.html'>Go to Login</a>");
            return;
        }

        String status = request.getParameter("status");
        String rentFrom = request.getParameter("rent_from");
        String rentTo = request.getParameter("rent_to");

        String url = "jdbc:mysql://localhost:3306/project1";
        String dbUser = "root";
        String dbPassword = "manoj123";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {

            	String sql = "SELECT id, name, phone, email, room, status, rent_from, rent_to FROM userdata " +
                        "WHERE username = ? AND status = ? AND rent_from >= ? AND rent_to <= ?";

                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, status);
                    stmt.setString(3, rentFrom);
                    stmt.setString(4, rentTo);

                    try (ResultSet rs = stmt.executeQuery()) {
                        out.println("<html><body>");
                        out.println("<h2>Room Filled Data</h2>");
                        out.println("<table border='1'>");
                        out.println("<tr><th>Id</th><th>Name</th><th>Phone</th><th>Email</th><th>Room</th><th>Status</th><th>Rent From</th><th>Rent To</th></tr>");

                        boolean hasData = false;
                        while (rs.next()) {
                            hasData = true;
                            out.println("<tr>");
                            out.println("<td>" + rs.getString("id") + "</td>");
                            out.println("<td>" + rs.getString("name") + "</td>");
                            out.println("<td>" + rs.getString("phone") + "</td>");
                            out.println("<td>" + rs.getString("email") + "</td>");
                            out.println("<td>" + rs.getString("room") + "</td>");
                            out.println("<td>" + rs.getString("status") + "</td>");
                            out.println("<td>" + rs.getString("rent_from") + "</td>");
                            out.println("<td>" + rs.getString("rent_to") + "</td>");
                            out.println("</tr>");
                        }

                        if (!hasData) {
                            out.println("<tr><td colspan='8'>No data found for given filters.</td></tr>");
                        }

                        out.println("</table>");
                        out.println("<br><form action='filter.html'><button type='submit'>Go Back</button></form>");
                        out.println("</body></html>");
                    }
                }
            }

        } catch (Exception e) {
            out.println("<pre style='color:red;'>");
            e.printStackTrace(out);
            out.println("</pre>");
        }
    }
}
