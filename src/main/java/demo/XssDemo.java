① XssDemo.java — 입력 → HTML 출력

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XssDemo {
    public void render(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String name = req.getParameter("name");
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println("<h1>" + name + "</h1>");
    }
}
