package Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;

import Subject.Order;
import Subject.PlaneFly;
import Subject.User;
import Toolkit.SqlHelper;

public class User_Dao
{

    // 比对登录信息,都正确返回1,账户或者密码不正确返回0
    public static int check_Login(String user, String pwd)
    {
        new SqlHelper();// 连接数据库
        String sql = "select pwd from user_t where ph_num = '" + user + "'";// 要执行的sql语句
        ResultSet res = SqlHelper.executeQuery(sql);// 查询得到的结果
        try
        {
            if (res.next())
            {
                if (res.getString("pwd").equals(pwd))// 比对密码是否一样
                {
                    return 1;// 一样返回1
                }
                else
                {
                    return 0;
                }
            }
            else// 不存在该账户，返回-1
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
        return 0;
    }

    // 插入新用户信息，完成注册
    public static int insert_information(String phone, String pwd, String profession, String stu)
    {
        int ans = 0;
        String sql = "select ph_num from user_t where ph_num = '" + phone + "'";// 要执行的sql语句
        new SqlHelper();// 连接数据库
        ResultSet res = SqlHelper.executeQuery(sql);// 查找得到的结果
        try
        {
            if (res.next())
            {
                // res.next();
                System.out.println(res.getString(0) + " 666");
                return -1;
            }
            else
            {
                sql = "insert into user_t (ph_num, pwd, u_profess, u_stu) values ('" + phone + "','"
                        + pwd + "','" + profession + "','" + stu + "')";
                ans = SqlHelper.executeUpdate(sql);
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

    // 给界面增加正确的日期，从今天起,十四天以内
    public static void addDate(JComboBox<String> cmb)
    {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 15; i++)
        {
            calendar.add(Calendar.DATE, (i > 0 ? 1 : 0));
            date = calendar.getTime();
            cmb.addItem(format.format(date));
        }
    }

    public static ArrayList<PlaneFly> queryPlane(String start, String arrive, String date, String position)
    {
        ArrayList<PlaneFly> info = new ArrayList<>();
        String pos;
        if (position.equals("%"))
            pos = "(s_num > 0 or j_num > 0)";
        else
            pos = position.equals("经济舱") ? " j_num > 0" : " s_num > 0";
        String sql = "select pf_no, pc_name,convert(char(8),st_time,108) as st_t, convert(char(8),ar_time,108) as ar_t, picket, j_num, s_num from plane_fly where Datediff(day,'"
                + date + "', st_time) = 0 and st_d = '" + start + "'and  ar_d = '" + arrive + "' and" + pos;
        // System.out.println(sql);
        new SqlHelper();
        ResultSet res = SqlHelper.executeQuery(sql);
        try
        {
            while (res.next())
            {
                PlaneFly tmp = new PlaneFly();
                tmp.ar_position = arrive;
                tmp.st_position = start;
                tmp.pf_no = res.getInt(1);
                tmp.pc_name = res.getString(2);
                tmp.st_time = res.getString(3);
                tmp.ar_time = res.getString(4);
                tmp.picket = res.getInt(5);// 票价的打折
                tmp.j_num = res.getInt(6);
                tmp.s_num = res.getInt(7);
                info.add(tmp);
            }
        }
        catch (SQLException e)
        {
            e.getStackTrace();
        }
        finally
        {
            SqlHelper.closeConnection();
        }
        return info;
    }

    public static User FindUser(String ph_num)
    {
        User use = null;
        String sql = "select pwd, ph_num, iden, u_profess, u_stu from user_t where ph_num = '" + ph_num + "'";
        new SqlHelper();
        ResultSet res = SqlHelper.executeQuery(sql);
        try
        {
            if (res.next())
            {
                use = new User();
                use.pwd = res.getString(1);
                use.ph_num = res.getString(2);
                use.iden = res.getString(3);
                use.profession = res.getString(4);
                use.stu = res.getString(5);
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
        return use;
    }

    public static ArrayList<User> FindLiaise(User user)
    {
        ArrayList<User> liaise = new ArrayList<>();
        if (user == null)
            return liaise;
        String ph_num = user.ph_num;
        String sql = "select  r_no, use_user, use_iden, use_identity, use_name from relation_user where buy_user = '" +
                ph_num + "'";
        new SqlHelper();
        ResultSet res = SqlHelper.executeQuery(sql);
        try
        {
            while (res.next())
            {
                User tmp = new User();
                tmp.r_no = res.getString("r_no");
                tmp.ph_num = res.getString("use_user");
                tmp.iden = res.getString("use_identity");
                tmp.iden_num = res.getString("use_iden");
                tmp.name = res.getString("use_name");
                liaise.add(tmp);
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
        return liaise;
    }

    public static int Insert_Relation(String buy_user_ph_no, String name, String ph_no, String iden_num)
    {
        int ans = 0;
        String sql = "insert into relation_user values('" + buy_user_ph_no + "','" + ph_no + "','"
                + iden_num + "','" + name + "', '普通会员')";
        new SqlHelper();
        ans = SqlHelper.executeUpdate(sql);
        SqlHelper.closeConnection();
        return ans;
    }

    public static void updateRelation(String r_no, String[] data)
    {
        String name = data[0], iden_num = data[1], ph_num = data[2];
        String sql = "update relation_user set use_name = '" + name + "', use_iden = '" + iden_num + "', use_user = '"
                      + ph_num + "' where r_no = '" + r_no + "'";
        new SqlHelper();
        SqlHelper.executeUpdate(sql);
        SqlHelper.closeConnection();
    }


    public static void  PurchasePicket(Object[][] rows, PlaneFly pfy, User use, int flag)
    {
        new SqlHelper();
        LocalDateTime dateTime = LocalDateTime.now(); // get the current date and time  
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
        String purchase_Id = use.ph_num, purchaseTime = dateTime.format(formatter), 
                pf_no = Integer.toString(pfy.pf_no), position = flag == 0 ? "经济舱" : "商务舱";
        String sql;
        for(int i = 0; i < rows.length; i++)
        {
            sql = "insert into sell values('" + purchase_Id + "','" + rows[i][2] + "', '"
                    + pf_no + "','" + purchaseTime + "','" + rows[i][4] + "','"
                    + position + "','" + rows[i][0] + "')";
            SqlHelper.executeUpdate(sql);   
        }
        SqlHelper.closeConnection();
    }

    public static void deletePurchasePicket(Order order)
    {
        new SqlHelper();
        String sql = "delete from sell where sell_no = '" + order.sell_no + "'";
        SqlHelper.executeQuery(sql);
        SqlHelper.closeConnection();
    }

    public static PlaneFly getPlaneFly(int pf_no)
    {
        PlaneFly ply = null;
        new SqlHelper();
        String sql = "select * from plane_fly where pf_no = " + pf_no;
        ResultSet res = SqlHelper.executeQuery(sql);
        try
        {
            if(res.next())
            {
                ply = new PlaneFly();
                ply.pc_name = res.getString("pc_name");
                ply.pf_no = pf_no;
                ply.st_time = res.getString("st_time");
                ply.ar_time = res.getString("ar_time");
                ply.p_no = res.getString("p_no");
                ply.s_num = res.getInt("s_num");
                ply.j_num = res.getInt("j_num");
                ply.st_position = res.getString("st_d");
                ply.ar_position = res.getString("ar_d");
                ply.picket = res.getFloat("picket");
                ply.need_time = res.getInt("need_time");
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            SqlHelper.closeConnection();
        }
        return ply;
    }

    public static void updateUserPhone(String ph_num, String old_ph)
    {
        String sql = "execute update_user_phone '" + ph_num + "','" + old_ph + "'";
        new SqlHelper();
        SqlHelper.executeUpdate(sql);
        SqlHelper.closeConnection();
    }

    public static void updatePwd(String ph_num, String new_pwd1)
    {
        String sql = "execute update_user_pwd '" + ph_num + "', '" + new_pwd1 + "'";
        new SqlHelper();
        SqlHelper.executeUpdate(sql);
        SqlHelper.closeConnection();
    }

    public static void upgradeUser(String ph_num)
    {
        new SqlHelper();
        String sql = "execute update_identity '" + ph_num + "'";
        SqlHelper.executeUpdate(sql);
        SqlHelper.closeConnection();
    }

    public static ArrayList<Order> FindOrder(String ph_num, String time)
    {
        ArrayList<Order> orders = null;
        new SqlHelper();
        String sql = "execute find_TimeOrder '" + time + "', '" + ph_num + "'";
        ResultSet res = SqlHelper.executeQuery(sql);
        try
        {
            orders = new ArrayList<>();
            while(res.next())
            {
                Order order = new Order();
                order.sell_no = res.getInt("sell_no");
                order.purchase_time = res.getString("purchase_time");
                order.purchase_ID = res.getString("purchase_ID");
                order.use_ID = res.getString("use_ID");
                order.pf_no = res.getInt("pf_no");
                order.cost = res.getFloat("cost");
                order.position = res.getString("s_or_j");
                order.use_name = res.getString("use_name");
                orders.add(order);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            SqlHelper.closeConnection();
        }
        return orders;
    }

    
    public static boolean TimeCmp(String time)
    {
        LocalDateTime dateTime = LocalDateTime.now(); // get the current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time_now = dateTime.format(formatter);
        for (int i = 0; i < time_now.length(); i++)
        {
            if (time_now.charAt(i) > time.charAt(i))
            {
                return true;
            }
            else if (time_now.charAt(i) < time.charAt(i))
            {
                return false;
            }
        }
        return true;
    }

    public static void getUseOrNotUse(ArrayList<Order> all, ArrayList<Order> used, ArrayList<Order> not_use)
    {
        PlaneFly ply;
        for (Order order : all)
        {
            ply = getPlaneFly(order.pf_no);
            String time = ply.st_time;
            if (TimeCmp(time))
            {
                used.add(order);
            }
            else
            {
                not_use.add(order);
            }
        }
    }
}
