package demo;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VulnerableServlet {

    // CWE-79: Reflected XSS
    public void render(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println(req.getParameter("q"));
    }

    // CWE-89: SQL Injection
    public boolean login(HttpServletRequest req, Connection db) throws Exception {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Statement stmt = db.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'");
        return rs.next();
    }

    // CWE-78: OS Command Injection
    public void pingHost(HttpServletRequest req) throws IOException {
        String host = req.getParameter("host");
        Runtime.getRuntime().exec("ping -c 1 " + host);
    }

    // CWE-328: Use of a Broken/Risky Cryptographic Hash (MD5)
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return new String(md.digest(password.getBytes()));
    }
}
