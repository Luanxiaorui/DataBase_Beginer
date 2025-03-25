package Frm;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Dao.Manage_Dao;
import Toolkit.BeautyUI;
import Subject.Manager;

public class Manage_LoginFrm extends JFrame implements ActionListener
{
    JLabel l1 = new JLabel("飞机票订购系统");
    JLabel l2 = new JLabel("管理员登录");
    JLabel l3 = new JLabel("账号");
    JLabel l4 = new JLabel("密码");

    JTextField tf1 = new JTextField();
    JPasswordField tf2 = new JPasswordField();
    JButton jb1 = new JButton("马上登录");

    Manage_LoginFrm()
    {
        this.setLayout(null);
        add(l1);
        l1.setBounds(100, 50, 400, 50);
        l1.setFont(new Font("仿宋", Font.BOLD, 36));
        add(l2);
        l2.setBounds(150, 125, 300, 50);
        l2.setFont(new Font("宋体", Font.PLAIN, 28));
        add(l3);
        l3.setBounds(90, 300, 100, 25);
        add(tf1);
        tf1.setBounds(90, 325, 300, 50);
        tf1.setFont(new Font("宋体", Font.BOLD, 18));
        add(l4);
        l4.setBounds(90, 400, 50, 25);
        add(tf2);
        tf2.setBounds(90, 425, 300, 50);
        tf2.setFont(new Font("宋体", Font.BOLD, 15));
        add(jb1);
        jb1.setBounds(150, 525, 150, 30);

        jb1.addActionListener(this);
        tf2.addActionListener(this);

        setVisible(true);
        setBounds(100, 100, 500, 680);
        this.setResizable(false);
        setTitle("系统登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == jb1 || e.getSource() == tf2)
        {
            String user = tf1.getText(), pwd = new String(tf2.getPassword());
            if (user.isEmpty() || pwd.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "信息不完整", "提示", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                // 比对信息，登陆与否
                if (Manage_Dao.check_Login(user, pwd) <= 0)
                {
                    JOptionPane.showMessageDialog(null, "您输入的账户或密码不正确", "提示", JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    Manager manager = Manage_Dao.FindUser(user);
                    // 进入新的业务处理界面
                    new Manage_MainFrm(manager);
                    this.dispose();
                }
            }
        }
    }

    public static void main(String[] args)
    {
        BeautyUI.beautyUI();
        new Manage_LoginFrm();
    }
}
