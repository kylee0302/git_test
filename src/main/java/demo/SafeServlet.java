package demo;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SafeServlet {

    // CWE-79 대응: 사용자 입력을 그대로 반영하지 않음
    public void render(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println("Hello, ScanOps!");
    }

    // CWE-89 대응: PreparedStatement 파라미터 바인딩
    public boolean login(HttpServletRequest req, Connection db) throws Exception {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        PreparedStatement stmt = db.prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=?");
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    // CWE-78 대응: 사용자 입력을 커맨드에 넣지 않고 허용목록만 검증
    public boolean isHostAllowed(HttpServletRequest req) {
        String host = req.getParameter("host");
        return "127.0.0.1".equals(host) || "localhost".equals(host);
    }

    // CWE-328 대응: SHA-256 사용
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return new String(md.digest(password.getBytes()));
    }
}
