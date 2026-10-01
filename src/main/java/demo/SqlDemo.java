② SqlDemo.java — 입력 → SQL 조합 → 실행

import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;

public class SqlDemo {
    public boolean exists(HttpServletRequest req, Connection db)
            throws Exception {
        String name = req.getParameter("name");
        String sql = "SELECT id FROM users WHERE name='" + name + "'";
        try (var stmt = db.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.nex…
