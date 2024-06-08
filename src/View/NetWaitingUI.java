package View;

import javax.swing.*;
import java.awt.*;

/**
 * This class provides a waiting interface for players joining the game
 */

public class NetWaitingUI extends JFrame {
    private final String playerName;

    public NetWaitingUI(String name) {
        this.playerName = name;

        this.setSize(GameUI.width, GameUI.height);

        this.setTitle("Mahjong Game");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLocationRelativeTo(null);

        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(0, 100, 0));
        g.fillRect(0, 0, GameUI.width, GameUI.height);
        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Please wait other players", 360, 500);
    }
}
