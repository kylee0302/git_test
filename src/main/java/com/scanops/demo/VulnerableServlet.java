package com.scanops.demo;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 데모용 취약 서블릿 (ScanOps 시연용, 실서비스 코드 아님)
 * SafeServlet.java와 짝을 이루는 bad 버전.
 */
public class VulnerableServlet extends HttpServlet {

    private Connection db;

    // CWE-79: Reflected XSS
    // source: getParameter, sink: getWriter().println(...)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        response.getWriter().println("<h1>Hello, " + name + "</h1>");
    }

    // CWE-89: SQL Injection
    // source: getParameter, sink: Statement.executeQuery (문자열 이어붙이기)
    public boolean login(HttpServletRequest request) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Statement stmt = db.createStatement();
        String query = "SELECT * FROM users WHERE username='" + username
                + "' AND password='" + password + "'";
        ResultSet rs = stmt.executeQuery(query);
        return rs.next();
    }

    // CWE-78: OS Command Injection
    // source: getParameter, sink: Runtime.exec
    public void pingHost(HttpServletRequest request) throws IOException {
        String host = request.getParameter("host");
        Runtime.getRuntime().exec("ping -c 1 " + host);
    }

    // CWE-328: Use of a Broken/Risky Cryptographic Hash (MD5)
    // arg_literal 매칭: getInstance("MD5")
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(password.getBytes());
        return new String(digest);
    }
}
