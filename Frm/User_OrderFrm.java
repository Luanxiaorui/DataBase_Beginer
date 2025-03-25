package Frm;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import Dao.User_Dao;
import Subject.Order;
import Subject.PlaneFly;
import Subject.User;

public class User_OrderFrm extends JPanel implements ActionListener
{

    JPanel not_use_order, nu_wpanel;
    JPanel used_order, use_wpanel;
    JPanel all_order, all_wpanel;

    JComboBox<String> cmb1;
    JComboBox<String> cmb2;

    JButton jb1;

    JPanel jp1 = new JPanel();

    ArrayList<Order> all = new ArrayList<>();
    ArrayList<Order> used = new ArrayList<>();
    ArrayList<Order> not_use = new ArrayList<>();

    JButton[] tp;
    User use;

    User_OrderFrm(User use)
    {
        this.use = use;
        used_order = new JPanel(new BorderLayout());
        not_use_order = new JPanel(new BorderLayout());
        all_order = new JPanel(new BorderLayout());

        nu_wpanel = new JPanel();
        use_wpanel = new JPanel();
        all_wpanel = new JPanel();

        nu_wpanel.setLayout(new BoxLayout(nu_wpanel, BoxLayout.Y_AXIS));
        use_wpanel.setLayout(new BoxLayout(use_wpanel, BoxLayout.Y_AXIS));
        all_wpanel.setLayout(new BoxLayout(all_wpanel, BoxLayout.Y_AXIS));

        used_order.add(new JScrollPane(use_wpanel), BorderLayout.CENTER);
        not_use_order.add(new JScrollPane(nu_wpanel), BorderLayout.CENTER);
        all_order.add(new JScrollPane(all_wpanel), BorderLayout.CENTER);

        cmb1 = new JComboBox<>();
        addItemOwn(cmb1);
        cmb2 = new JComboBox<>();
        cmb2.addItem("全部订单");
        cmb2.addItem("未使用订单");
        cmb2.addItem("已使用订单");

        jb1 = new JButton("查询");

        jb1.addActionListener(this);

        jp1.add(cmb1);
        jp1.add(cmb2);
        jp1.add(jb1);

        updateFrm("任意时间段", "全部订单");
        setVisible(true);
        setBounds(500, 500, 800, 500);
        
    }

    public void addItemOwn(JComboBox<String> cmb)
    {
        cmb.addItem("任意时间段");
        cmb.addItem("近7天内");
        cmb.addItem("近30天内");
        cmb.addItem("近一年内");
    }

    public void updateFrm(String time, String solution) {
        // 查询订单数据
        ArrayList<Order> all = User_Dao.FindOrder(use.ph_num, time);
    
        if (!all.isEmpty()) 
        {
            // 根据订单状态分类
            User_Dao.getUseOrNotUse(all, used, not_use);
            
            System.out.println("数量" + all.size() + "  " + not_use.size() + "  " + used.size());

            // 清空旧数据
            all_order.removeAll();
            not_use_order.removeAll();
            used_order.removeAll();

            //加边框
            all_order.setBorder(new TitledBorder(new EtchedBorder(), "可选乘机人"));
            not_use_order.setBorder(new TitledBorder(new EtchedBorder(), "可选乘机人"));
            used_order.setBorder(new TitledBorder(new EtchedBorder(), "可选乘机人"));
    
            // 创建订单面板
            makeOrderPanel(not_use_order, 1, not_use);
            makeOrderPanel(used_order, 0, used);
            makeOrderPanel(all_order, 2, all);

            // 刷新界面
            all_order.revalidate();
            not_use_order.revalidate();
            used_order.revalidate();
        }
    
        // 根据解决方案显示相应的订单面板
        switch (solution) {
            case "全部订单":
                showPanel(all_order);
                break;
            case "未使用订单":
                showPanel(not_use_order);
                break;
            case "使用过的订单":
                showPanel(used_order);
                break;
            default:
                // 默认显示全部订单
                showPanel(all_order);
                break;
        }
    }

    private void showPanel(JPanel panelToShow)
    {
        removeAll();
        add(jp1, BorderLayout.NORTH);
        add(panelToShow, BorderLayout.SOUTH);
        revalidate();
        repaint();    
    }

    private void makeOrderPanel(JPanel wrapperPanel, int idx, ArrayList<Order> orders)
    {
        int n = orders == null ? 0 :orders.size();
        if (idx >=  1)
            tp = new JButton[n];
        wrapperPanel.removeAll();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < n; i++)
        {
            JPanel tmp = makeOnePanel(orders.get(i));
            if (idx >= 1)
            {
                tp[i] = new JButton("退订");
                tmp.add(tp[i]);
                //tp[i].setBounds(560, 100, 100, 50);
                tp[i].addActionListener(this);
            }
            wrapperPanel.add(tmp);
        }
        if(idx == 2 && used != null && used.size() > 0)
        {
            for (Order order:used)
            {
                JPanel tmp = makeOnePanel(order);
                wrapperPanel.add(tmp);
            }
        }
    }

    private JPanel makeOnePanel(Order order)
    {
        JPanel jp = new JPanel(new FlowLayout());
        PlaneFly ply = User_Dao.getPlaneFly(order.pf_no);
        JLabel st_d = new JLabel(ply.st_position);
        JLabel ar_d = new JLabel(ply.ar_position);
        JLabel line1 = new JLabel("----->");
        JLabel line2 = new JLabel("----->");
        JLabel st_time = new JLabel(ply.st_time);
        JLabel ar_time = new JLabel(ply.ar_time);
        JLabel name = new JLabel(order.use_name);
        JLabel cost = new JLabel(Double.toString(order.cost));
        JLabel s_or_j = new JLabel(order.position);

        System.out.println(order.use_name);

        jp.add(st_d);
        jp.add(line1);
        jp.add(ar_d);
        jp.add(st_time);
        jp.add(line2);
        jp.add(ar_time);
        jp.add(name);
        jp.add(cost);
        jp.add(s_or_j);
        jp.setSize(600, 200);
        st_d.setBounds(150, 50, 100, 50);
        ar_d.setBounds(350, 50, 100, 50);
        line1.setBounds(250, 50, 100, 50);
        st_time.setBounds(150, 100, 100, 50);
        ar_time.setBounds(350, 100, 100, 50);
        name.setBounds(450, 50, 100, 50);
        cost.setBounds(450, 100, 100, 50);
        s_or_j.setBounds(450, 150, 100, 50);
        return jp;
    }

    public void actionPerformed(ActionEvent e)
    {
        String time = cmb1.getSelectedItem().toString();
        String fanwei = cmb2.getSelectedItem().toString();
        if(e.getSource() == jb1)
        {
            updateFrm(time, fanwei);
        }
        else
        {
            for (int i = 0; i < not_use.size(); i++)
            {
                if (e.getSource() == tp[i])
                {
                    //退订业务
                    User_Dao.deletePurchasePicket(not_use.get(i));
                    updateFrm(time, fanwei);
                    break;
                }
            }
        }
    }

    public static void main(String[] args)
    {

    }

}
