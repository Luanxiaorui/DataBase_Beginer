package Frm;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import Dao.User_Dao;
import Subject.User;

public class User_InformFrm extends JPanel implements ActionListener
{

    JLabel l1 = new JLabel("电话：");
    JLabel l2;// 电话
    JLabel l3 = new JLabel("等级：");
    JLabel l4;// 身份

    JButton jb1 = new JButton("修改");
    JButton jb2 = new JButton("升级");

    JLabel l5 = new JLabel("请输入旧密码");
    JLabel l6 = new JLabel("请输入新密码");
    JLabel l7 = new JLabel("请再次输入新密码");

    JPanel jp1 = new JPanel(null);
    JPanel jp2 = new JPanel(null);

    JPasswordField tf1 = new JPasswordField();
    JPasswordField tf2 = new JPasswordField();
    JPasswordField tf3 = new JPasswordField();

    JButton jb3 = new JButton("确认更改");
    User use;

    User_InformFrm(User use)
    {

        this.use = use;
        setLayout(null);
        jp1.add(l1);
        l1.setBounds(50, 50, 100, 50);
        l1.setFont(new Font("宋体", Font.BOLD, 24));
        l2 = new JLabel(use.ph_num);
        jp1.add(l2);
        l2.setBounds(160, 50, 100, 50);
        l2.setFont(new Font("宋体", Font.BOLD, 16));
        jp1.add(jb1);
        jb1.setBounds(270, 50, 100, 35);

        jp1.add(l3);
        l3.setBounds(50, 120, 100, 50);
        l3.setFont(new Font("宋体", Font.BOLD, 24));
        l4 = new JLabel(use.iden);
        jp1.add(l4);
        l4.setBounds(160, 120, 100, 50);
        l4.setFont(new Font("宋体", Font.BOLD, 24));
        jp1.add(jb2);
        jb2.setBounds(270, 120, 100, 35);

        jp1.setBorder(new TitledBorder(new EtchedBorder(), "个人信息编辑"));
        // jp1.setSize(300, 300);

        jp2.add(l5);
        l5.setBounds(70, 50, 150, 50);
        l5.setFont(new Font("宋体", Font.BOLD, 16));

        jp2.add(tf1);
        tf1.setBounds(240, 50, 150, 50);
        jp2.add(l6);
        l6.setBounds(70, 120, 150, 50);
        l6.setFont(new Font("宋体", Font.BOLD, 16));

        jp2.add(tf2);
        tf2.setBounds(240, 120, 150, 50);
        jp2.add(l7);
        l7.setBounds(70, 190, 150, 50);
        l7.setFont(new Font("宋体", Font.BOLD, 16));

        jp2.add(tf3);
        tf3.setBounds(240, 190, 150, 50);
        jp2.add(jb3);
        jb3.setBounds(150, 260, 100, 35);
        jp2.setBorder(new TitledBorder(new EtchedBorder(), "个人密码修改"));

        add(jp1);
        jp1.setBounds(160, 10, 420, 200);
        add(jp2);
        jp2.setBounds(160, 260, 420, 300);

        jb1.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);

        setVisible(true);
        setSize(800, 630);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == jb1)
        {
            String ph_num = JOptionPane.showInputDialog(null, "请输入手机号码", "修改手机号",JOptionPane.INFORMATION_MESSAGE);
            if(ph_num != null)
            {
                if(ph_num.length() != 11 || ph_num.equals(l2.getText()))
                {
                    JOptionPane.showMessageDialog(null, "输入的信息不合法或者和原号码重复", "", JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    //执行更新语句
                    User_Dao.updateUserPhone(ph_num, l2.getText());
                    use = User_Dao.FindUser(ph_num);
                    //更新界面?????
                    l2.setText(ph_num);
                    l2.repaint();
                    l2.revalidate();
                }
            }
        }
        else if(e.getSource() == jb2)
        {
            if(l4.getText().equals("黄金会员"))
            {
                JOptionPane.showMessageDialog(null, "您已是最高等级无需升级", "", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
               int ans = JOptionPane.showConfirmDialog(null, "确定要花费1000元升级吗？升级后可享95折购票", "", JOptionPane.YES_NO_OPTION);
               if(ans == 0)
               {
                    //执行更新升级
                    User_Dao.upgradeUser(use.ph_num);
                    //更新界面?????
               }
            }
        }
        else
        {
            String old_pwd = new String(tf1.getPassword()), new_pwd1 = new String(tf2.getPassword()),
                    new_pwd2 = new String(tf3.getPassword());
            if(!old_pwd.equals(use.pwd))
            {
                JOptionPane.showMessageDialog(null, "输入的旧密码错误", "", JOptionPane.WARNING_MESSAGE);
            }
            else if(new_pwd1.length() > 15 || new_pwd1.length() < 8 || new_pwd2.length() > 15 || new_pwd2.length() < 8)
            {
                JOptionPane.showMessageDialog(null, "输入的新密码不合法", "", JOptionPane.WARNING_MESSAGE);
            }
            else if(!new_pwd1.equals(new_pwd2))
            {
                JOptionPane.showMessageDialog(null, "两次输入的密码不一致", "", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                //更新密码
                User_Dao.updatePwd(use.ph_num, new_pwd1);
            }
        }
    }

    // public static void main(String[] args)
    // {
    //     new User_InformFrm(new User());
    // }
}
