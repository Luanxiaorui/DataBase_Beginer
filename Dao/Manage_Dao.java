package Dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;

import Subject.Manager;
import Toolkit.SqlHelper;

public class Manage_Dao
{

    public static Manager FindUser(String user)
    {
        Manager m = new Manager();
        String sql = "select * from manager where m_id = '" + user + "'";
        new SqlHelper();
        ResultSet res = SqlHelper.executeQuery(sql);
        try
        {
            if (res.next())
            {
                m.iden = res.getString(1);
                m.m_id = res.getString(2);
               // m.pwd = res.getString(3);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            SqlHelper.closeConnection();
        }
        return m;
    }

    public static int check_Login(String user, String pwd)
    {
        int ans = 0;
        String sql = "execute login_check '" + user + "','" + pwd + "'";
        new SqlHelper();
        ResultSet res = SqlHelper.executeQuery(sql);
        try
        {
            if (res.next())
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            SqlHelper.closeConnection();
        }
        return ans;
    }

    public static int deletePly(String pf_no)
    {
        int ans = 0;
        new SqlHelper();
        String sql = "delete from plane_fly where pf_no = " + pf_no + "";
        System.out.println(sql);
        ans = SqlHelper.executeUpdate(sql);
        SqlHelper.closeConnection();
        return ans;
    }

    // Object[] colum = {"出发站", "到达站", "出发时间", "到站时间", "航班号", "花费", "航班编号"};
    public static Object[][] QueryFly(String s1, String s2, String s3, String s4, String s5, String s7)
    {
        new SqlHelper();
        Object[][] res = null;
        String sql = "execute ply_ComQuery '" + s1 + "','" + s2 + "','" + s3 + "','" + s4 + "','" + s5 + "',"
                + (s7.isEmpty() ? "''" : s7);
        System.out.println(sql);
        ResultSet rs = SqlHelper.executeQuery(sql);
        try
        {
            rs.last();
            int row = rs.getRow(), cn = 0;
            rs.beforeFirst();
            res = new Object[row][7];
            while (rs.next())
            {
                res[cn][0] = rs.getString("st_d");
                res[cn][1] = rs.getString("ar_d");
                res[cn][2] = rs.getString("st_time");
                res[cn][3] = rs.getString("ar_time");
                res[cn][4] = rs.getString("p_no");
                res[cn][5] = rs.getFloat("picket");
                res[cn++][6] = rs.getInt("pf_no");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            SqlHelper.closeConnection();
        }
        return res;
    }

    public static void UpdateFly(String t1, String t2, String t3, String t4, String t5, String t6,
            String t7)
    {
        String sql = "execute ply_ComUpdate " + t7 + ",'" + t3 + "','" + t4 + "','" + t5 + "','" + t1 + "','" + t2
                + "','" + t6 + "'";
        System.out.println(sql);
        new SqlHelper();
        int ans = SqlHelper.executeUpdate(sql);
        SqlHelper.closeConnection();
    }

    public static int deletePlane(String text)
    {
        new SqlHelper();
        int ans = 0;
        String sql = "delete from plane where p_no = '" + text + "'";
        ans = SqlHelper.executeUpdate(sql);
        SqlHelper.closeConnection();
        return ans;
    }

    // Object[] colum = {"航班号", "航空公司", "航班型号", "经济舱数量", "商务舱数量", "出发站", "到达站",
    // "花费", "所需飞行时间", "出发时刻"};
    public static Object[][] QueryPlane(String text, String text2, String text3, String text4, String text5,
            String text6, String text7, String text8, String text9)
    {
        new SqlHelper();
        String sql = "execute plane_ComQuery '" + text + "','" + text2 + "','" + text3 + "','" + text4
                + "','" + text5 + "','" + text6 + "','" + text7 + "','" + text8 + "','" + text9 + "'";
        //System.out.println(sql);
        Object[][] ans = null;
        ResultSet res = SqlHelper.executeQuery(sql);
        try
        {
            res.last();
            int r = res.getRow(), c = 10, cn = 0;
            ans = new Object[r][c];
            res.beforeFirst();
            while (res.next())
            {
                ans[cn][0] = res.getString("p_no");
                ans[cn][1] = res.getString("pc_name");
                ans[cn][2] = res.getString("p_kind");
                ans[cn][3] = res.getInt("j_num");
                ans[cn][4] = res.getInt("s_num");
                ans[cn][5] = res.getString("st_d");
                ans[cn][6] = res.getString("ar_d");
                ans[cn][7] = res.getString("picket");
                ans[cn][8] = res.getString("need_time");
                ans[cn++][9] = res.getString("st_time");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            SqlHelper.closeConnection();
        }
        return ans;
    }

    public static void UpdatePlane(String text, String text2, String text3, String text4, String text5, String text6,
            String text7, String text8, String text9, String text10)
    {
        new SqlHelper();
        String sql = "execute plane_Update '" + text + "','" + text2 + "','" + text3 + "','" + text4
                + "','" + text5 + "','" + text6 + "','" + text7 + "','" + text8 + "','" + text9 + "','" + text10 + "'";
        System.out.println(sql);
        SqlHelper.executeUpdate(sql);
        SqlHelper.closeConnection();
    }

    public static void InsertPlane(String text, String text2, String text3, String text4, String text5, String text6,
            String text7, String text8, String text9, String text10)
    {
        new SqlHelper();
        String sql = "insert into plane values('" + text + "','" + text2 + "','" + text3 + "','" + text4
                + "','" + text5 + "','" + text6 + "','" + text7 + "','" + text8 + "','" + text9 + "','" + text10 + "')";
        System.out.println(sql);
        SqlHelper.executeUpdate(sql);
        SqlHelper.closeConnection();
    }

    // 给查询界面增加地址
    public static void addAddress(JComboBox<String> cmb)
    {
        String sql = "select * from addr";
        new SqlHelper();
        ResultSet res = SqlHelper.executeQuery(sql);
        try
        {
            while (res.next())
            {
                cmb.addItem(res.getString(1));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            SqlHelper.closeConnection();
        }
    }

    public static Object[][] QuerySeat(String t2)
    {
        Object[][] res = null;
        String sql = "execute AnalyzeFlightOccupancyUpdated '" + t2 + "'";
        new SqlHelper();
        ResultSet rs = SqlHelper.executeQuery(sql);
        try
        {
            rs.last();
            int r  = rs.getRow(), c = t2.equals("航线") ? 4 : 3, cn = 0;
            res = new Object[r][c];
            rs.beforeFirst();
            while(rs.next())
            {
                res[cn][0] = rs.getString(1);
                res[cn][1] = rs.getString(2);
                res[cn][2] = rs.getString(3);
                if(c == 4)
                    res[cn][3] = rs.getString(4);
                cn++;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            SqlHelper.closeConnection();
        }
        return res;
    }

    public static Object[][] QueryTable(String t1)
    {
        Object[][] res = null;
        String sql = "execute GenerateFlightStatisticsReport '" + t1 + "'";
        new SqlHelper();
        ResultSet rs = SqlHelper.executeQuery(sql);
        try
        {
            rs.last();
            int r  = rs.getRow(), c = t1.equals("航线") ? 5 : 4, cn = 0;
            res = new Object[r][c];
            rs.beforeFirst();
            while(rs.next())
            {
                res[cn][0] = rs.getString(1);
                res[cn][1] = rs.getString(2);
                res[cn][2] = rs.getString(3);
                res[cn][3] = rs.getString(4);
                if(c == 5)
                    res[cn][4] = rs.getString(5);
                cn++;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            SqlHelper.closeConnection();
        }
        return res;
    }

}
