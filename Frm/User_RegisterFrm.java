package Frm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import Dao.User_Dao;

public class User_RegisterFrm extends JFrame implements ActionListener
{

    // JLabel l1 = new JLabel("飞机票订购系统");
    JLabel l1 = new JLabel("手机号");
    JLabel l2 = new JLabel("密码");
    JLabel l3 = new JLabel("再次输入密码：");
    JLabel l4 = new JLabel("学历(选填)");
    JLabel l5 = new JLabel("职业(选填)");

    JTextField tf1 = new JTextField();
    JPasswordField tf2 = new JPasswordField();
    JPasswordField tf3 = new JPasswordField();
    JTextField tf4 = new JTextField();
    JTextField tf5 = new JTextField();
    JButton jb1 = new JButton("注册账号");
    JButton jb2 = new JButton("<-返回");

    User_RegisterFrm()
    {

        this.setLayout(null);
        add(jb2);
        jb2.setBounds(0, 0, 100, 30);
        add(l1);
        l1.setBounds(50, 80, 280, 30);
        l1.setFont(new Font("仿宋", Font.BOLD, 21));
        add(l2);
        l2.setBounds(50, 140, 280, 30);
        l2.setFont(new Font("宋体", Font.BOLD, 21));
        add(l3);
        l3.setBounds(50, 200, 280, 30);
        l3.setFont(new Font("宋体", Font.BOLD, 21));
        add(l4);
        l4.setBounds(50, 260, 280, 30);
        l4.setFont(new Font("宋体", Font.BOLD, 21));
        add(l5);
        l5.setBounds(50, 320, 280, 30);
        l5.setFont(new Font("宋体", Font.BOLD, 21));
        add(tf1);
        tf1.setBounds(260, 80, 300, 30);
        tf1.setFont(new Font("宋体", Font.BOLD, 18));
        add(tf2);
        tf2.setBounds(260, 140, 300, 30);
        tf2.setFont(new Font("宋体", Font.BOLD, 15));
        add(tf3);
        tf3.setBounds(260, 200, 300, 30);
        tf3.setFont(new Font("宋体", Font.BOLD, 15));
        add(tf4);
        tf4.setBounds(260, 260, 300, 30);
        tf4.setFont(new Font("宋体", Font.BOLD, 15));
        add(tf5);
        tf5.setBounds(260, 320, 300, 30);
        tf5.setFont(new Font("宋体", Font.BOLD, 15));
        add(jb1);
        jb1.setBounds(290, 380, 230, 30);

        jb1.addActionListener(this);
        tf3.addActionListener(this);
        jb2.addActionListener(this);

        setVisible(true);
        setBounds(100, 100, 600, 500);
        this.setResizable(false);
        setTitle("注册账户");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == jb1 || e.getSource() == tf3)
        {
            String profession = tf5.getText(), stu = tf4.getText();
            String phone = tf1.getText(), pwd1 = new String(tf2.getPassword()), pwd2 = new String(tf3.getPassword());
            if (phone.isEmpty() || pwd1.isEmpty() || pwd2.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "信息不完整", "提示", JOptionPane.WARNING_MESSAGE);
            }
            else if (phone.length() != 11)
            {
                JOptionPane.showMessageDialog(null, "请输入正确的手机号码", "提示", JOptionPane.WARNING_MESSAGE);
            }
            else if (!pwd1.equals(pwd2))
            {
                JOptionPane.showMessageDialog(null, "两次输入的密码不一致，请重新输入", "提示", JOptionPane.WARNING_MESSAGE);
            }
            else if (pwd1.length() < 8 || pwd1.length() > 15)
            {
                JOptionPane.showMessageDialog(null, "请输入8到15位的密码", "提示", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                // 执行插入语句：
                int res = User_Dao.insert_information(phone, pwd2, profession, stu);
                System.out.println(res);
                if (res >= 1)
                {
                    JOptionPane.showMessageDialog(null, "恭喜你，注册成功", "提示", JOptionPane.PLAIN_MESSAGE);
                    this.dispose();
                    new User_LoginFrm();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "该用户已存在", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        if (e.getSource() == jb2)
        {
            this.dispose();
            new User_LoginFrm();
        }
    }

    public static void main(String[] args)
    {
        new User_RegisterFrm();
    }
}
