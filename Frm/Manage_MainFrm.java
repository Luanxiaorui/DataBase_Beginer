package Frm;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import Subject.Manager;

public class Manage_MainFrm extends JFrame 
{
    JTabbedPane tabbedPane;


    Manage_MainFrm(Manager m)
    {


        Manage_MultiFrm manage_MultiFrm = new Manage_MultiFrm();
        Manage_PlaneFrm planeFrm = new Manage_PlaneFrm();
        Manage_PlyFrm plyFrm = new Manage_PlyFrm();

        tabbedPane = new JTabbedPane();
        if(m.iden.equals("一级管理员"))
            tabbedPane.addTab("飞机管理", planeFrm);
        tabbedPane.addTab("分析", manage_MultiFrm);
        tabbedPane.addTab("航班管理", plyFrm);

        tabbedPane.setPreferredSize(new Dimension(430, 340));
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
        setBounds(500, 500, 1200, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // public static void main(String[] args) {
    //     new Manage_MainFrm();
    // }

}
