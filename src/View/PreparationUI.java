package View;

import Controller.Game;
import Model.MeldType;
import Model.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PreparationUI extends JFrame implements MouseListener {
    private Graphics gf = null;
    private Game game;
    private List<Button> buttons = new ArrayList<>();
    private int clickNumber;
    private int[] scores;
    private int maxIndex;
    private boolean startGame;

    public PreparationUI(Game game){
        this.game = game;

        clickNumber = 0;
        scores = new int[4];
        startGame = false;
        // Test
        //Test test = new Test();
        // Test
    }

    public void initializeUI(){
        this.setSize(GameUI.width, GameUI.height);

        this.setTitle("Mahjong Game");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLocationRelativeTo(null);

        this.addMouseListener(this);

        this.setVisible(true);

        Button button = new Button("Start the game");
        button.setBounds(450, 400, 255, 50);
        buttons.add(button);

        button = new Button("Exit");
        button.setBounds(550, 600, 100, 50);
        buttons.add(button);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GameUI.width, GameUI.height);
        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.BOLD, 36));
        if (!startGame) {
            g.drawString("Welcome to Mahjong Game!", 360, 200);
        }else{
            g.drawString("Now decide who is Banker", 360, 200);
        }

        for (Button button : buttons) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString(button.getLabel(), button.getX(), button.getY() + 36);
        }

        for (int i = 0; i != 4; i++) {
            if (scores[i] != 0){
                g.drawString("Player " + PlayerType.getPlayerType(i).toString() + " gets " + scores[i], 100, 300 + (i * 100));
            }
        }

        if (clickNumber == 4){
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Player " + PlayerType.getPlayerType(maxIndex).toString() + " is banker!", 600, 400);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1){
            int xPos = e.getX();
            int yPos = e.getY();

            String name = "";

            for (Button button : buttons) {
                if (button.getBounds().contains(xPos, yPos)) {
                    name = button.getLabel();
                }
            }

            if ("Exit".equals(name)){
                System.exit(0);
                return;
            } else if ("Start the game".equals(name)) {
                startGame = true;

                buttons.clear();

                Button button = new Button("Roll the dice");
                button.setBounds(450, 400, 255, 50);
                buttons.add(button);
            } else if ("Roll the dice".equals(name)) {
                scores[clickNumber] = (int) (Math.random() * 11) + 2;
                clickNumber++;



                if (clickNumber == 4){
                    buttons.clear();

                    int maxScore = scores[0];
                    for (int i = 1; i != 4; i++) {
                        if (scores[i] > maxScore){
                            maxIndex = i;
                        }
                    }

                    Button button = new Button("Continue game");
                    button.setBounds(450, 650, 255, 50);
                    buttons.add(button);
                }
            } else if ("Continue game".equals(name)) {
                game.setStart();
                game.setZhuang(maxIndex);
                game.startGame();

                dispose();
            }
        }

        repaint();
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
