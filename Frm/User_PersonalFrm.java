package Frm;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import Subject.User;

public class User_PersonalFrm extends JFrame
{
    JTabbedPane tabbedPane;

    User_PersonalFrm(User use)
    {
        User_OrderFrm order = new User_OrderFrm(use);
        User_InformFrm info = new User_InformFrm(use);
        User_PicketQueryFrm query = new User_PicketQueryFrm(use);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("个人资料", info);
        tabbedPane.addTab("我的订单", order);
        tabbedPane.addTab("查询航班", query);

        tabbedPane.setPreferredSize(new Dimension(430, 340));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
        setBounds(500, 500, 900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    // public static void main(String[] args)
    // {

    // }

}
