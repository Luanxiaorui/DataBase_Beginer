package Frm;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Dao.User_Dao;

public class User_CreateRelation_Frm extends JFrame implements ActionListener
{

    JPanel jp1 = new JPanel(new GridLayout(3, 3));
    JPanel jp2 = new JPanel();

    static int ans;
    JLabel l1 = new JLabel("真实姓名");
    JLabel l2 = new JLabel("身份证号");
    JLabel l3 = new JLabel("手机号");

    JTextField tf1 = new JTextField();
    JTextField tf2 = new JTextField();
    JTextField tf3 = new JTextField();

    JButton jb1 = new JButton("确认");
    JButton jb2 = new JButton("取消");
    String buy_user_ph_no;
    User_PicketPurchaseFrm now;
    
    User_CreateRelation_Frm(String buy_user_ph_no, User_PicketPurchaseFrm now)
    { 

        this.now = now;
        this.buy_user_ph_no = buy_user_ph_no;

        jp1.add(l1);
        jp1.add(tf1);
        jp1.add(l2);
        jp1.add(tf2);
        jp1.add(l3);
        jp1.add(tf3);

        l1.setFont(new Font("宋体", Font.BOLD, 24));
        l2.setFont(new Font("宋体", Font.BOLD, 24));
        l3.setFont(new Font("宋体", Font.BOLD, 24));


        add(jp1, BorderLayout.CENTER);
        jp2.add(jb1);
        jp2.add(jb2);
        add(jp2, BorderLayout.SOUTH);

        jb1.addActionListener(this);
        jb2.addActionListener(this);

        this.setVisible(true);
        this.setBounds(500, 500, 600,300);
        setTitle("新建联系人");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == jb1)
        {
            String name = tf1.getText(), iden_num = tf2.getText(), ph_no = tf3.getText();
            if(name.isEmpty() || iden_num.isEmpty() || ph_no.isEmpty())
            {
                JOptionPane.showMessageDialog(null, "信息不完整", "提示", JOptionPane.WARNING_MESSAGE);
            }
            else if(iden_num.length() != 18 || ph_no.length() != 11)
            {
                JOptionPane.showMessageDialog(null, "输入的信息不规范", "提示", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                //执行插入信息
                int res = User_Dao.Insert_Relation(buy_user_ph_no, name, ph_no, iden_num);
                if (res == 1)
                {
                    JOptionPane.showMessageDialog(null, "插入成功", "提示", JOptionPane.WARNING_MESSAGE);
                    now.updateCheckBox();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "已存在该联系人", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        if(e.getSource() == jb2)
        {
            this.dispose();
        }
    }

    public static void main(String[] args)
    {
        // new User_Relation_create();
    }
    
}
