package Frm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import Dao.Manage_Dao;

public class Manage_PlaneFrm extends JPanel implements ActionListener
{
    JLabel l1 = new JLabel("航班号");
    JLabel l2 = new JLabel("航空公司");
    JLabel l3 = new JLabel("航班型号");
    JLabel l4 = new JLabel("经济舱数量");
    JLabel l5 = new JLabel("商务舱数量");
    JComboBox<String> cmb6 = new JComboBox<>();
    JComboBox<String> cmb7 = new JComboBox<>();
    JLabel l8 = new JLabel("花费");
    JLabel l9 = new JLabel("所需飞行时间");
    JLabel l10 = new JLabel("出发时刻");

    JTextField tf1 = new JTextField(5);
    JTextField tf2 = new JTextField(5);
    JTextField tf3 = new JTextField(5);
    JTextField tf4 = new JTextField(5);
    JTextField tf5 = new JTextField(5);
    JTextField tf8 = new JTextField(5);
    JTextField tf9 = new JTextField(5);
    MaskFormatter formatter = null;
    JFormattedTextField tf10;

    JPanel p1 = new JPanel();
    JPanel p2 = new JPanel();

    //JButton jb1 = new JButton("删除");
    JButton jb2 = new JButton("清空");
    JButton jb3 = new JButton("查询");
    JButton jb4 = new JButton("更新");
    JButton jb5 = new JButton("返回");
    JButton jb6 = new JButton("添加");

    Object[][] row = new Object[0][];
    Object[] colum = {"航班号", "航空公司", "航班型号", "经济舱数量", "商务舱数量", "出发站", "到达站", "花费", "所需飞行时间", "出发时刻"};

    JTable table;
    DefaultTableModel model;

    Manage_PlaneFrm()
    {


        try
        {
            formatter = new MaskFormatter("##:##:##");
            formatter.setPlaceholderCharacter('_'); // 可选：设置占位符
        }
        catch (java.text.ParseException e)
        {
            e.printStackTrace();
        }
        tf10 = new JFormattedTextField(formatter);
        tf3.setColumns(8); // 设置文本框宽度
        cmb6.addItem("请选择出发地");
        cmb7.addItem("请选择到达地");
        Manage_Dao.addAddress(cmb6);
        Manage_Dao.addAddress(cmb7);
        p1.setBorder(new TitledBorder("飞机信息"));
        p1.add(l1);
        p1.add(tf1);
        p1.add(l2);
        p1.add(tf2);
        p1.add(l3);
        p1.add(tf3);
        p1.add(l4);
        p1.add(tf4);
        p1.add(l5);
        p1.add(tf5);
        p1.add(cmb6);
        p1.add(cmb7);
        p1.add(l8);
        p1.add(tf8);
        p1.add(l9);
        p1.add(tf9);
        p1.add(l10);
        p1.add(tf10);

        setLayout(new BorderLayout());

       // p2.add(jb1);
        p2.add(jb3);
        p2.add(jb2);
        p2.add(jb4);
        p2.add(jb5);
        p2.add(jb6);

        //jb1.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);
        jb4.addActionListener(this);
        jb5.addActionListener(this);
        jb6.addActionListener(this);

        model = new DefaultTableModel(row, colum);
        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(p1, BorderLayout.NORTH);
        add(p2, BorderLayout.SOUTH);


        setVisible(true);
        setBounds(300, 300, 1300, 500);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // 更新面板
    public void updateTable()
    {
        if (row == null || row.length == 0)
            JOptionPane.showMessageDialog(null, "不存在", "提示", JOptionPane.PLAIN_MESSAGE);
        else
        {
            model.setDataVector(row, colum);
            table.revalidate();
            table.repaint();
            validate();
        }
    }

    // 按钮ActionEvent事件触发处理
    public void actionPerformed(ActionEvent e)
    {
        // 触发删除
        // if (e.getSource() == jb1)
        // {
        //     if (tf1.getText().isEmpty())
        //         JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);
        //     else if (Manage_Dao.deletePlane(tf1.getText()) > 0)
        //     {
        //         JOptionPane.showMessageDialog(null, "删除成功", "提示", JOptionPane.PLAIN_MESSAGE);
        //         row = Manage_Dao.QueryPlane("", "", "", "", "",
        //         "", "", "", "");
        //         updateTable();
        //     }
        //     else
        //     {
        //         JOptionPane.showMessageDialog(null, "删除失败", "提示", JOptionPane.PLAIN_MESSAGE);
        //     }
        // }
        String t6 = cmb6.getSelectedItem().toString(), t7 = cmb7.getSelectedItem().toString();
        // 触发清除文本框
        if (e.getSource() == jb2)
        {
            delete();
        }
        // 触发更新
        else if (e.getSource() == jb4)
        {
            if (tf1.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);
            // 锁定一个车次
            else
            {
  
                Manage_Dao.UpdatePlane(tf1.getText(), tf2.getText(), tf3.getText(), tf4.getText(), tf5.getText(),
                        t6.equals("请选择出发地")?"" : t6, t7.equals("请选择到达地")?"" : t7,
                        tf8.getText(), tf9.getText(), tf10.getText());
                row = Manage_Dao.QueryPlane("", "", "", "", "",
                        "", "", "", "");
                updateTable();
            }

        }
        // 触发查询
        else if (e.getSource() == jb3)
        {
            if (tf1.getText().isEmpty() && tf2.getText().isEmpty() && tf5.getText().isEmpty() && t6.isEmpty()
                    && tf3.getText().isEmpty() && tf4.getText().isEmpty() && t7.isEmpty()
                    && tf8.getText().isEmpty()
                    && tf9.getText().isEmpty() && tf10.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);
            else
            {
                row = Manage_Dao.QueryPlane(tf1.getText(), tf2.getText(), tf3.getText(), tf4.getText(), tf5.getText(),
                t6.equals("请选择出发地")?"" : t6, t7.equals("请选择到达地")?"" : t7, tf9.getText(), tf10.getText());
                updateTable();
            }
        }
        else if (e.getSource() == jb5)
        {
            row = Manage_Dao.QueryPlane("", "", "", "", "",
            "", "", "", "");
            updateTable();
        }
        else
        {
            if (tf1.getText().isEmpty() || tf2.getText().isEmpty() || tf5.getText().isEmpty() || t6.isEmpty()
                    || tf3.getText().isEmpty() || tf4.getText().isEmpty() || t7.isEmpty()
                    || tf8.getText().isEmpty() || tf9.getText().isEmpty() || tf10.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);
            else
            {
                Manage_Dao.InsertPlane(
                        tf1.getText(), tf2.getText(), tf3.getText(), tf4.getText(), tf5.getText(),
                        t6.equals("请选择出发地")?"" : t6,t7.equals("请选择到达地")?"" : t7, tf8.getText(), tf9.getText(), tf10.getText());
                row = Manage_Dao.QueryPlane("", "", "", "", "",
                        "", "", "", "");
                updateTable();
            }
        }
    }

    public void delete()
    {
        tf1.setText(null);
        tf2.setText(null);
        tf3.setText(null);
        tf4.setText(null);
        tf5.setText(null);
        tf8.setText(null);
        tf9.setText(null);
        tf10.setText(null);
    }

    public static void main(String[] args)
    {
        new Manage_PlaneFrm();
    }
}
