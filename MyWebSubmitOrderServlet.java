import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/submit_order")
public class SubmitOrderServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/tvoje_databaze";
    private static final String DB_USER = "tvuj_uzivatel";
    private static final String DB_PASSWORD = "tve_heslo";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            // Připojení k databázi
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "INSERT INTO objednavky (jmeno, email, zprava) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, name);
                    stmt.setString(2, email);
                    stmt.setString(3, message);

                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) {
                        out.println("<p>Objednávka byla úspěšně odeslána.</p>");
                    } else {
                        out.println("<p>Nepodařilo se uložit objednávku.</p>");
                    }
                }
            } catch (SQLException e) {
                out.println("<p>Chyba při práci s databází: " + e.getMessage() + "</p>");
            }
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC Driver nenalezen", e);
        }
    }
}
