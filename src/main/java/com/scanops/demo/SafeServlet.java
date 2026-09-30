package com.scanops.demo;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.HtmlUtils;

/**
 * 데모용 안전 서블릿 (ScanOps 시연용, 실서비스 코드 아님)
 * VulnerableServlet.java와 같은 기능을 안전하게 구현한 good 버전.
 */
public class SafeServlet extends HttpServlet {

    private Connection db;

    // CWE-79 대응: 출력 전 HTML 이스케이프 (sanitizer: HtmlUtils.htmlEscape)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String safeName = HtmlUtils.htmlEscape(name);
        response.getWriter().println("<h1>Hello, " + safeName + "</h1>");
    }

    // CWE-89 대응: PreparedStatement 파라미터 바인딩 (sanitizer: .setString)
    public boolean login(HttpServletRequest request) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        PreparedStatement stmt = db.prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=?");
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    // CWE-78 대응: 사용자 입력을 커맨드에 넣지 않고 허용목록으로만 검증
    public boolean isHostAllowed(HttpServletRequest request) {
        String host = request.getParameter("host");
        java.util.List<String> allowlist = java.util.Arrays.asList("127.0.0.1", "localhost");
        return allowlist.contains(host);
    }

    // CWE-328 대응: 취약한 MD5 대신 SHA-256 사용
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(password.getBytes());
        return new String(digest);
    }
}
