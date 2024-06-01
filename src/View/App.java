package View;

import Controller.Game;
import Network.GameServer;
import Network.PlayerClient;
import Sound.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class App extends JFrame implements MouseListener {
    public static void main(String[] args) {
        new App().initializeUI();
    }

    private static final int Width = 1200;
    private static final int Height = 800;
    private SoundPlayer soundPlayer = new SoundPlayer();

    public App() {
        buttons = new ArrayList<>();
    }

    private final List<Button> buttons;

    public void initializeUI() {
        soundPlayer.playMusic("src/Resources/music.wav");

        this.setSize(App.Width, App.Height);

        this.setTitle("Mahjong Game");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLocationRelativeTo(null);

        this.addMouseListener(this);

        this.setContentPane(new JPanel() {
            {
                setOpaque(false);
            }
        });
        this.getContentPane().setBackground(new Color(0, 100, 0));

        this.setVisible(true);

        Button button = new Button("Single-player mode");
        button.setBounds(430, 320, 255, 50);
        buttons.add(button);

        button = new Button("Multiplayer mode");
        button.setBounds(450, 460, 255, 50);
        buttons.add(button);

        button = new Button("Connect to server");
        button.setBounds(450, 580, 255, 50);
        buttons.add(button);

        button = new Button("Help");
        button.setBounds(800, 640, 100, 50);
        buttons.add(button);

        button = new Button("Exit");
        button.setBounds(570, 700, 100, 50);
        buttons.add(button);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(0, 100, 0));
        g.fillRect(0, 0, App.Width, App.Height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Welcome to Mahjong Game!", 360, 200);

        for (Button button : buttons) {
            g.drawString(button.getLabel(), button.getX(), button.getY() + 36);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            int xPos = e.getX();
            int yPos = e.getY();

            String name = "";

            for (Button button : buttons) {
                if (button.getBounds().contains(xPos, yPos)) {
                    name = button.getLabel();
                }
            }

            switch (name) {
                case "Single-player mode":
                    this.dispose();
                    new Game().startGame();
                    return;
                case "Multiplayer mode":
                    this.dispose();
                    new ServerThread().start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException exc) {
                        exc.printStackTrace();
                    }
                    new PlayerClientThread().start();
                    return;
                case "Connect to server":
                    this.dispose();
                    new PlayerClientThread().start();
                    return;
                case "Help":
                    HowToPlayUI howToPlayUI = new HowToPlayUI();
                    howToPlayUI.initializeUI();
                    return;
                case "Exit":
                    System.exit(0);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

class PlayerClientThread extends Thread {
    @Override
    public void run() {
        new PlayerClient().connectServer();
    }
}

class ServerThread extends Thread {
    @Override
    public void run() {
        new GameServer().startServer();
    }
}