package View;

import Controller.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PreparationUI extends JFrame {
    private Graphics gf = null;
    private Game game;
    private List<JButton> buttons = new ArrayList<>();
    private int clickNumber;
    private int[] scores;
    private int maxIndex;
    private boolean startGame;

    public PreparationUI(Game game) {
        this.game = game;

        clickNumber = 0;
        scores = new int[4];
        startGame = false;

    }

    public void initializeUI() {
        this.setLayout(null);

        JButton startButton = new JButton("Start the game");
        startButton.setBounds(450, 200, 300, 50);
        startButton.setBackground(new Color(50, 205, 50));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Courier New", Font.BOLD, 20));
        startButton.setActionCommand("Start");
        startButton.addActionListener(new ButtonClickListener());
        this.add(startButton);


        JButton helpButton = new JButton("Help");
        helpButton.setBounds(500, 500, 200, 50);
        helpButton.setBackground(new Color(50, 205, 50));
        helpButton.setForeground(Color.WHITE); // Set button text color to white
        helpButton.setActionCommand("Help");
        helpButton.addActionListener(new ButtonClickListener());
        this.add(helpButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(500, 550, 200, 50);
        exitButton.setBackground(new Color(50, 205, 50));
        exitButton.setForeground(Color.WHITE); // Set button text color to white
        exitButton.setActionCommand("Exit");
        exitButton.addActionListener(new ButtonClickListener());
        this.add(exitButton);


        // Add buttons to JFrame

        // Set JFrame properties
        this.setSize(GameUI.width, GameUI.height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Mahjong");
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(34, 139, 34));

        this.setVisible(true);


    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("Start")) {
                startGame = true;
                buttons.clear();
                new DiceRollUI(game);
                PreparationUI.this.dispose();

            } else if (command.equals("Roll the dice")) {
                scores[clickNumber] = (int) (Math.random() * 11) + 2;
                clickNumber++;
                if (clickNumber == 4) {
                    buttons.clear();
                    int maxScore = scores[0];
                    for (int i = 1; i != 4; i++) {
                        if (scores[i] > maxScore) {
                            maxIndex = i;
                        }
                    }
                    JButton button = new JButton("Continue game");
                    button.setBounds(450, 650, 255, 50);
                    button.setActionCommand("Continue game");
                    button.addActionListener(new ButtonClickListener());
                    buttons.add(button);
                }
            } else if (command.equals("Continue game")) {
                game.setStart();
                game.setZhuang(maxIndex);
                game.startGame();
                dispose();

            } else if (command.equals("Exit")) {
                System.exit(0);
            } else if (command.equals("Help")) {
                new HowToPlayUI();
            }
        }
    }
}
