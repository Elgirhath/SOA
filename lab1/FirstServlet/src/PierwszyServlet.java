
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/PierwszyServlet")
public class PierwszyServlet extends HttpServlet {
    @java.lang.Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("windows-1250");
        PrintWriter out = resp.getWriter();
        String name = req.getParameter("imie");
        int age = Integer.parseInt(req.getParameter("wiek"));
        out.println("<html>");
        out.println("<head><title>Pierwszy Servlet</title></head>");
        out.println("<body>");
        out.println("<p>Witaj, " + name + ", masz " + age + "lat</p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
