package Toolkit;

import java.sql.*;

public class SqlHelper
{

    private static final String JDBC_DRIVE = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433; DatabaseName= Plane_Ticket_Service; encrypt=true; trustServerCertificate=true";
    private static final String USER = "20221536";
    private static final String PASSWORD = "123456";
    private static Connection conn = null;

    public SqlHelper()
    {
        conn = null;
        try
        {
            Class.forName(JDBC_DRIVE);
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("连接成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage() + " " + "连接失败");
        }
    }

    public static ResultSet executeQuery(String query)
    {
        ResultSet rs = null;
        try
        {
            Statement ps = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery(query);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return rs;
    }

    public static int executeUpdate(String query)
    {
        int res = 0;
        try
        {
            Statement ps = conn.createStatement();
            res = ps.executeUpdate(query);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return res;
    }

    public static void closeConnection()
    {
        try
        {
            conn.close();
            System.out.println("连接已关闭");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    /*
    public static void main(String[] args) {
        new SqlHelper();
    }
    */
}