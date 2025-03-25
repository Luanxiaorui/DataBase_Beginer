package Frm;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Dao.User_Dao;
import Subject.PlaneFly;
import Subject.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class User_PicketQueryFrm extends JPanel implements ActionListener
{

    int cnt = 0;
    JLabel l1 = new JLabel("出发地");
    JLabel l2 = new JLabel("到达地");
    JLabel l3 = new JLabel("出发日期");
    JLabel l4 = new JLabel("舱位选择");

    JComboBox<String> cm1 = new JComboBox<>();
    JComboBox<String> cm2 = new JComboBox<>();
    JComboBox<String> cm3 = new JComboBox<>();
    JComboBox<String> cm4 = new JComboBox<>();

    JPanel jp1 = new JPanel();
    JPanel jp2 = new JPanel(new BorderLayout());

    JButton jb1 = new JButton("查询");

    ArrayList<PlaneFly> info = new ArrayList<>();

    JButton[] jb_j;
    JButton[] jb_s;
    Box bx1;
    User use2;
    User_PicketQueryFrm(User use)
    {

        use2 = use;
        jp1.add(l1);
        jp1.add(cm1);

        jp1.add(l2);
        jp1.add(cm2);

        cm1.addItem("请选择出发地");
        cm2.addItem("请选择目的地");
        cm3.addItem("请选择出发日期");
        cm4.addItem("请选择舱位");
        cm4.addItem("不限舱室");
        cm4.addItem("经济舱");
        cm4.addItem("商务舱");

        User_Dao.addAddress(cm1);
        User_Dao.addAddress(cm2);
        User_Dao.addDate(cm3);

        jp1.add(l3);
        jp1.add(cm3);

        jp1.add(l4);
        jp1.add(cm4);

        jp1.add(jb1);
        jb1.addActionListener(this);

        add(jp1, BorderLayout.NORTH);
        add(jp2, BorderLayout.CENTER);

        setVisible(true);
        setBounds(100, 100, 1000, 500);
    }

    public void updateInfo()
    {
        if (info.size() == 0)
        {
            JOptionPane.showMessageDialog(null, "您所查询的航班不存在或者已经无票", "提示", JOptionPane.PLAIN_MESSAGE);
        }
        else
        {
            int n = info.size();
            PlaneFly plf;
            JPanel wrapperPanel = new JPanel();
            wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
            jb_j = new JButton[n];
            jb_s = new JButton[n];
            jp2.removeAll();
            for (int i = 0; i < n; i++)
            {
                plf = info.get(i);
                wrapperPanel.add(makPanel(i, plf));
            }
            JScrollPane scrollPane = new JScrollPane(wrapperPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jp2.add(scrollPane, BorderLayout.CENTER);
            jp2.revalidate();
            jp2.repaint();
        }
    }

    public JPanel makPanel(int i, PlaneFly plf)
    {
        JPanel jp = new JPanel(new FlowLayout());
        JLabel labs1 = new JLabel(plf.pc_name + "   ");
        JLabel labs2 = new JLabel(plf.st_time + "   ");
        JLabel labs3 = new JLabel("----->");
        JLabel labs4 = new JLabel(plf.ar_time + "   ");
        JLabel labs5 = new JLabel(plf.st_position + "站   ");
        JLabel labs6 = new JLabel(plf.ar_position + "站   ");
        JLabel labs7 = new JLabel(Double.toString(plf.picket) + "元起");

        System.out.println(plf.st_position + "站--->" + plf.ar_position + "站");

        jb_j[i] = new JButton("订购经济舱");
        jb_s[i] = new JButton("订购商务舱");
        jp.add(labs1);
        jp.add(labs2);
        jp.add(labs5);
        jp.add(labs3);
        jp.add(labs6);
        jp.add(labs4);
        jp.add(labs7);
        labs1.setFont(new Font("宋体", Font.BOLD, 16));
        labs2.setFont(new Font("宋体", Font.BOLD, 16));
        labs5.setFont(new Font("宋体", Font.BOLD, 16));
        labs3.setFont(new Font("宋体", Font.BOLD, 16));
        labs4.setFont(new Font("宋体", Font.BOLD, 16));
        labs6.setFont(new Font("宋体", Font.BOLD, 16));
        labs7.setFont(new Font("宋体", Font.BOLD, 16));

        jp.add(jb_j[i]);
        jp.add(jb_s[i]);
        jb_j[i].addActionListener(this);
        jb_s[i].addActionListener(this);
        jp.setSize(750, 200);
        jp.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        return jp;
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == jb1)
        {
            cnt++;
            String start = cm1.getSelectedItem().toString(), arrive = cm2.getSelectedItem().toString(),
                    date = cm3.getSelectedItem().toString(), position = cm4.getSelectedItem().toString();
            if (start.equals("请选择出发地") || arrive.equals("请选择目的地") || date.equals("请选择出发日期"))
            {
                JOptionPane.showMessageDialog(null, "请先选择地点信息或日期信息", "提示", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                position = position.equals("请选择舱位") || position.equals("不限舱室") ? "%" : position;
                // 处理查到的飞机信息，做成面版展示
                info = User_Dao.queryPlane(start, arrive, date, position);
                updateInfo();
            }
        }
        for(int i = 0; i < info.size(); i++)
        {
            if(jb_j[i] == e.getSource())
            {
                new User_PicketPurchaseFrm(info.get(i), use2, 0);
                //this.setEnabled(false);
                break;
            }
            if(jb_s[i] == e.getSource())
            {
                new User_PicketPurchaseFrm(info.get(i), use2, 1);
                //this.setEnabled(false);
                break;
            }
        }
    }
    

    public static void main(String[] args)
    {
        new User_PicketQueryFrm(new User());
    }

}
