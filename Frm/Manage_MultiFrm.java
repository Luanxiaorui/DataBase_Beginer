package Frm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import Dao.Manage_Dao;


public class Manage_MultiFrm extends JPanel implements ActionListener, ItemListener
{
    //
    JComboBox<String> cmb1;
    JComboBox<String> cmb2;

    JPanel p1 = new JPanel();
    JPanel p2 = new JPanel();

    JButton jb1 = new JButton("查询");

    JTable table;
    DefaultTableModel model;
    Object[][] row = new Object[0][];
    Object[][] colum = {{"航空公司", "平均经济舱缺座率", "平均商务舱缺座率"},
                        {"出发地点", "到达地点", "平均经济舱缺座率", "平均商务舱缺座率"},
                        {"航空公司", "航班总数","平均经济舱剩余票数", "平均商务舱剩余票数"},
                        {"出发地点", "到达地点","航班总数" ,"平均经济舱剩余票数", "平均商务舱剩余票数"},
                        {"机型", "航班总数","平均经济舱剩余票数", "平均商务舱剩余票数"}};                                                                       ;
    Manage_MultiFrm()
    {

        cmb1 = new JComboBox<>();
        cmb2 = new JComboBox<>();

        cmb1.addItem("请选择关键字");
        cmb1.addItem("航空公司");
        cmb1.addItem("航线");
        cmb1.addItem("机型");

        cmb2.addItem("请选择要查看的类型");
        cmb2.addItem("报表");
        cmb2.addItem("空座率分析");
        
        cmb2.addItemListener(this);

        p1.setBorder(new TitledBorder("航班信息"));
        setLayout(new BorderLayout());
        p1.add(cmb1);
        p1.add(cmb2);
        p1.add(jb1);

        jb1.addActionListener(this);

        add(p1, BorderLayout.NORTH);

        model = new DefaultTableModel(row, colum[0]);
        table = new JTable(model);
        table.setEnabled(false);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
        setBounds(300, 300, 1300, 500);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    }

    // 更新面板
    public void updateTable(int idx)
    {
        if (row == null || row.length == 0)
            JOptionPane.showMessageDialog(null, "不存在", "提示", JOptionPane.PLAIN_MESSAGE);
        else
        {
            model.setDataVector(row, colum[idx]);
            //System.out.println(row.length);
            table.revalidate();
            table.repaint();
            validate();
        }
    }


    // 按钮ActionEvent事件触发处理
    public void actionPerformed(ActionEvent e)
    {
        // 触发查询
        if (e.getSource() == jb1)
        {
            String t1 = cmb1.getSelectedItem().toString(),
                    t2 = cmb2.getSelectedItem().toString();
            if(t2.equals("空座率分析"))
            {
                if(t1.equals("请选择关键字"))
                {
                    JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);
                }
                else
                {
                    row = Manage_Dao.QuerySeat(t1);
                    updateTable((row[0].length == 3 ? 0 : 1));
                }
            }
            else if(t2.equals("报表"))
            {
                if(t1.equals("请选择关键字"))
                {
                    JOptionPane.showMessageDialog(null, "信息不全", "提示", JOptionPane.PLAIN_MESSAGE);
                }
                else
                {
                    row = Manage_Dao.QueryTable(t1);
                    int idx;
                    if(row[0].length == 5)
                        idx = 3;
                    else
                    {
                        idx = t1.equals("航空公司") ? 2 : 4;
                        System.out.println(idx);
                    }
                    updateTable(idx);
                }
            }
            else
            {
                return;
            }
        }
    }

    public static void main(String[] args)
    {
        new Manage_MultiFrm();
    }

    
    public void itemStateChanged(ItemEvent e)
    {
        if(cmb2.getSelectedItem().toString().equals("空座率分析"))
        {
            cmb1.removeAllItems();
            cmb1.addItem("请选择关键字");
            cmb1.addItem("航线");
            cmb1.addItem("航空公司");
        }
        else
        {
            cmb1.removeAllItems();
            cmb1.addItem("请选择关键字");
            cmb1.addItem("航线");
            cmb1.addItem("航空公司");
            cmb1.addItem("机型");
        }
    }   
}
