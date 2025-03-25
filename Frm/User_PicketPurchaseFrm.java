package Frm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import Dao.User_Dao;
import Subject.PlaneFly;
import Subject.User;

public class User_PicketPurchaseFrm extends JFrame implements ActionListener
{

    int cnt = 0;
    JCheckBox[] chb;
    Boolean[] is_selected = new Boolean[100];

    JPanel jp1 = new JPanel();
    JPanel jp2 = new JPanel(new BorderLayout());
    JPanel jp3 = new JPanel();

    JTable table;
    JButton jb1 = new JButton("确定订票");
    JButton jb2 = new JButton("新建联系人");

    Object[] column = {"乘客姓名", "身份证号", "手机号码", "原价", "折扣后应付"};
    Object[][] rows = new Object[1][5];
    ArrayList<User> liaise;
    PlaneFly pfy;
    User user;
    int flag = -1;

    User_PicketPurchaseFrm(PlaneFly pfy, User user, int flag)
    {
        this.pfy = pfy;
        this.user = user;
        this.flag = flag;

        Arrays.fill(is_selected, false);

        updateCheckBox();
        updateTable();

        jp3.add(jb1);
        jp3.add(jb2);

        jb1.addActionListener(this);
        jb2.addActionListener(this);

        table.addMouseListener(new mouse());

        this.add(jp1, BorderLayout.NORTH);
        this.add(jp2, BorderLayout.CENTER);
        this.add(jp3, BorderLayout.SOUTH);

        this.setSize(600, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

    public void updateTable()
    {
        rows = new Object[cnt][5];
        System.out.println(cnt);
        liaise = User_Dao.FindLiaise(user);
        for (int i = 0, cn = 0; i < liaise.size() && cn < cnt; i++)
        {
            User t = liaise.get(i);
            if (chb[i].isSelected())
            {
                System.out.println("我找到一个被选中的" + t.name);
                rows[cn][0] = t.name;
                rows[cn][1] = t.iden_num;
                rows[cn][2] = t.ph_num;
                rows[cn][3] = pfy.picket;
                rows[cn++][4] = pfy.picket * (t.iden.equals("普通会员") ? 1.0 : 0.95);
            }
        }
        table = new JTable(rows, column);
        table.addMouseListener(new mouse());
        jp2.removeAll();
        jp2.add(new JScrollPane(table), BorderLayout.CENTER);

        // this.add(jp2, BorderLayout.CENTER);
        // this.revalidate();
        jp2.revalidate();
        jp2.repaint();
    }

    public void updateCheckBox()
    {
        liaise = User_Dao.FindLiaise(user);
        chb = new JCheckBox[liaise.size()];
        jp1.removeAll();
        for (int i = 0; i < liaise.size(); i++)
        {
            String name = liaise.get(i).name;
            chb[i] = new JCheckBox(name);
            if(is_selected[i])
            {
                chb[i].setSelected(true);
            }
            chb[i].addActionListener(this);
            jp1.add(chb[i]);
        }
        jp1.setBorder(new TitledBorder(new EtchedBorder(), "可选乘机人"));
        jp1.revalidate();
        jp1.repaint();
    }

    public void tableRowSelected()
    {
        table.setSelectionBackground(Color.BLUE);
        table.setSelectionForeground(Color.WHITE);
        int row = table.getSelectedRow();
        int colCount = table.getColumnCount();

        String[] data = new String[colCount];
        User us = getTheUser(row);
        System.out.println(row + "," + colCount);
        for (int i = 0; i < colCount; i++)
        {
            data[i] = table.getModel().getValueAt(row, i).toString();
        }
        if (data[1].length() != 18 || data[2].length() != 11 || colCount == 4 || colCount == 3)
        {
            JOptionPane.showMessageDialog(null, "信息有误", "提示", JOptionPane.WARNING_MESSAGE);
            table.getModel().setValueAt(us.name, row, 0);
            table.getModel().setValueAt(us.iden_num, row, 1);
            table.getModel().setValueAt(us.ph_num, row, 2);
            table.getModel().setValueAt(pfy.picket, row, 3);
            table.getModel().setValueAt(pfy.picket * (us.iden.equals("普通会员") ? 1.0 : 0.95), row, 4);
        }
        // update语句
        else if(!data[0].equals(us.name) || !data[1].equals(us.iden_num) || !data[2].equals(us.ph_num))
        {
            User_Dao.updateRelation(liaise.get(row).r_no, data);
        }
    }

    private User getTheUser(int row)
    {
        User ans = new User();
        for(int i = 0; i < chb.length; i++)
        {
            if(chb[i].isSelected())
            {
                row--;
                if(row == 0)
                {
                    ans = liaise.get(i);
                    break;
                }
            }
        }
        return ans;
    }

    class mouse extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e)
        {
            if (e.getSource() == table)
                tableRowSelected();
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == jb1)
        {
            // 订票，插入购买记录
            int row = table.getModel().getRowCount();
            PlaneFly ply = User_Dao.getPlaneFly(pfy.pf_no);
            int picket_num = flag == 1 ? ply.s_num : ply.j_num;
            if (row > picket_num)
            {
                JOptionPane.showMessageDialog(null, "余票不足,仅剩" + Integer.toString(picket_num) + "张", "提示",
                        JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                double total = 0;
                for (int i = 0; i < row; i++)
                {
                    total += (double) table.getModel().getValueAt(i, 4);
                }
                int ans = JOptionPane.showConfirmDialog(null, "共计" + Double.toString(total) + "元, 确认购买吗?", "Title",
                        JOptionPane.YES_NO_OPTION);
                if (ans == 0)
                {
                    User_Dao.PurchasePicket(rows, pfy, user, flag);
                }
            }
        }
        else if (source == jb2)
        {
            // 弹出新界面，提供新建，插入relation
            new User_CreateRelation_Frm(user.ph_num, this);
        }
        else
        {
            for (int i = 0; i < liaise.size(); i++)
            {
                if (source == chb[i])
                {
                    cnt += chb[i].isSelected() ? 1 : -1;
                    updateTable();
                    break;
                }
            }
        }
    }


    public static void main(String[] args)
    {
        new User_PicketPurchaseFrm(null, null, -1);
    }

}
