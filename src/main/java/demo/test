package demo;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VulnerableServlet {
    private static final String DEMO_PASSWORD = "scanops-demo-only-48371";

    public void render(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println(req.getParameter("q"));
    }
}
