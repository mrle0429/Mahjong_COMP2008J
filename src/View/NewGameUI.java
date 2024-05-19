package View;

/**
 * @author Le Liu
 * @create 2024-05
 */

import javax.swing.*;
import java.awt.*;

public class NewGameUI extends JFrame {
    // 添加您需要的组件，例如菜单栏，状态栏，按钮等
    private JMenuBar menuBar;
    private JLabel statusBar;
    private JPanel mainPanel;

    public NewGameUI() {
        // 初始化组件
        menuBar = new JMenuBar();
        statusBar = new JLabel("Status");
        mainPanel = new JPanel();

        // 设置菜单栏
        setJMenuBar(menuBar);

        // 添加主面板
        add(mainPanel, BorderLayout.CENTER);

        // 添加状态栏
        add(statusBar, BorderLayout.SOUTH);

        // 设置窗口属性
        setTitle("New Game UI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // 添加其他方法，例如创建菜单，更新状态栏，处理事件等
}