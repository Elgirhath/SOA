
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Servlet1")
public class Servlet1 extends HttpServlet {
    @java.lang.Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("windows-1250");
        PrintWriter out = resp.getWriter();
        double sum = 0;
        for (int i = 1; i <= 5; i++) {
            sum += Integer.parseInt(req.getParameter(String.valueOf(i)));
        }
        double avg = sum / 5.0;

        out.println("<html>");
        out.println("<head><title>Wynik</title></head>");
        out.println("<body>");
        out.println("<p>" + avg + "</p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}