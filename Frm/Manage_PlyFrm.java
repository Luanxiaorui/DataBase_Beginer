package Frm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
import Subject.PlaneFly;

public class Manage_PlyFrm extends JPanel implements ActionListener
{

    JComboBox<String> cmb1 = new JComboBox<>();
    JComboBox<String> cmb2 = new JComboBox<>();
    JLabel l3 = new JLabel("出发时刻");
    JLabel l4 = new JLabel("到达时刻");
    JLabel l5 = new JLabel("航班号");
    JLabel l6 = new JLabel("花费");
    JLabel l7 = new JLabel("航班编号");


    MaskFormatter formatter = null;
    JFormattedTextField tf3;
    JFormattedTextField tf4;
    JTextField tf5 = new JTextField(11);
    JTextField tf6 = new JTextField(11);
    JTextField tf7 = new JTextField(11);

    JPanel p1 = new JPanel();
    JPanel p2 = new JPanel();

    JButton jb1 = new JButton("删除");
    JButton jb2 = new JButton("清空");
    JButton jb3 = new JButton("查询");
    JButton jb4 = new JButton("更新");
    JButton jb5 = new JButton("返回");

    JTable table;
    DefaultTableModel model;
    Object[][] row = new Object[0][];
    Object[] colum = {"出发站", "到达站", "出发时间", "到站时间", "航班号", "花费", "航班编号"};
    ArrayList<PlaneFly> plys;

    Manage_PlyFrm()
    {

        try
        {
            formatter = new MaskFormatter("####-##-## -##:##:##");
            formatter.setPlaceholderCharacter('_'); // 可选：设置占位符
        }
        catch (java.text.ParseException e)
        {
            e.printStackTrace();
        }
        tf3 = new JFormattedTextField(formatter);
        tf4 = new JFormattedTextField(formatter);
        cmb1.addItem("请选择出发地");
        cmb2.addItem("请选择到达地");
        Manage_Dao.addAddress(cmb1);
        Manage_Dao.addAddress(cmb2);
        tf3.setColumns(8); // 设置文本框宽度
        tf4.setColumns(8);
        p1.setBorder(new TitledBorder("航班信息"));
        p1.add(cmb1);
        p1.add(cmb2);
        p1.add(l3);
        p1.add(tf3);
        p1.add(l4);
        p1.add(tf4);
        p1.add(l5);
        p1.add(tf5);
        p1.add(l6);
        p1.add(tf6);
        p1.add(l7);
        p1.add(tf7);

        setLayout(new BorderLayout());

        p2.add(jb1);
        p2.add(jb3);
        p2.add(jb2);
        p2.add(jb4);
        p2.add(jb5);

        jb1.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);
        jb4.addActionListener(this);
        jb5.addActionListener(this);

        add(p1, BorderLayout.NORTH);
        add(p2, BorderLayout.SOUTH);

        model = new DefaultTableModel(row, colum);
        table = new JTable(model);
        table.addMouseListener(new mouse());
        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
        setBounds(300, 300, 1300, 500);
    }

    // 选中变色
    public void tableRowSelected()
    {
        table.setSelectionBackground(Color.BLUE);
        table.setSelectionForeground(Color.white);
        int row = table.getSelectedRow();
        int colCount = table.getColumnCount();
        String[] data = new String[colCount];
        System.out.println(row + "," + colCount);
        for (int i = 0; i < colCount; i++)
            data[i] = table.getModel().getValueAt(row, i).toString();
        cmb1.setSelectedItem(data[0]);
        cmb2.setSelectedItem(data[1]);
        tf3.setText(data[2]);
        tf4.setText(data[3]);
        tf5.setText(data[4]);
        tf6.setText(data[5]);
        tf7.setText(data[6]);

        tf7.setEnabled(false);
    }

    // 更新面板
    public void updateTable()
    {
        if (row == null || row.length == 0)
            JOptionPane.showMessageDialog(null, "不存在", "提示", JOptionPane.PLAIN_MESSAGE);
        else
        {
            model.setDataVector(row, colum);
            System.out.println(row.length);
            table.revalidate();
            table.repaint();
            validate();
        }
    }

    // 点击事件触发
    class mouse extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e)
        {
            if (e.getSource() == table)
                tableRowSelected();
        }
    }

    // 按钮ActionEvent事件触发处理
    public void actionPerformed(ActionEvent e)
    {
        String t1 = cmb1.getSelectedItem().toString();
        String t2 = cmb2.getSelectedItem().toString();
        String t3 = tf3.getText();
        String t4 = tf4.getText();
        String t5 = tf5.getText();
        String t7 = tf7.getText();
        
        // 触发删除
        if (e.getSource() == jb1)
        {
            if (tf7.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);
            else if (Manage_Dao.deletePly(tf7.getText()) > 0)
            {
                JOptionPane.showMessageDialog(null, "删除成功", "提示", JOptionPane.PLAIN_MESSAGE);
                row = Manage_Dao.QueryFly("", "", "", "", "", "");// 无限制查询
                updateTable();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "删除失败", "提示", JOptionPane.PLAIN_MESSAGE);
            }
        }

        // 触发清除文本框
        if (e.getSource() == jb2)
        {
            delete();
        }

        // 触发查询
        else if (e.getSource() == jb3)
        {
            if (tf5.getText().isEmpty() && t1.isEmpty() && t2.isEmpty() && tf3.getText().isEmpty()
                    && tf4.getText().isEmpty() && tf7.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);
            else
            {
                row = Manage_Dao.QueryFly(t1, t2, t3, t4, t5, t7);
                updateTable();
            }
        }

        // 触发更新
        else if (e.getSource() == jb4)
        {
            if (tf7.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);
            else
            {
                // 时间检验格式,航班号检验位数，cost检验数字
                // 执行更新语句
                Manage_Dao.UpdateFly(t1.equals("请选择出发地") ? "" : t1, t2.equals("请选择到达地") ? "" : t2, tf3.getText(), tf4.getText(), tf5.getText(),
                        tf6.getText(), tf7.getText());
                row = Manage_Dao.QueryFly("", "", "", "", "", "");// 无限制查询
                updateTable();
            }
        }
        else
        {
            row = Manage_Dao.QueryFly("", "", "", "", "", "");
            updateTable();
        }
    }

    public void delete()
    {
        tf3.setText(null);
        tf4.setText(null);
        tf5.setText(null);
        tf6.setText(null);
        tf7.setEnabled(true);
        tf7.setText(null);
    }

    public static void main(String[] args)
    {
        new Manage_PlyFrm();
    }
}