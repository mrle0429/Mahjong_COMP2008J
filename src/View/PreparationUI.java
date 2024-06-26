package View;

import Controller.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The PreparationUI class provides a user interface for the preparation phase of the game.
 * It includes buttons for starting the game and going back to the menu. It also includes a mechanism
 * for rolling the dice to determine the order of play. The class keeps track of the scores from the dice rolls,
 * the number of times the dice have been rolled, and the index of the player with the highest score.
 * The class also interacts with the Game class to set the start of the game and the banker based on the dice rolls.
 *
 * @author Le Liu
 * @version 2024-05
 */
public class PreparationUI extends JFrame {
    private final Game game;
    private final List<JButton> buttons = new ArrayList<>();
    private int clickNumber;
    private final int[] scores;
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
        startButton.setBounds(450, 400, 300, 50);
        startButton.setBackground(new Color(0, 100, 0));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Courier New", Font.BOLD, 20));
        startButton.setActionCommand("Start");
        startButton.addActionListener(new ButtonClickListener());
        this.add(startButton);



        JButton exitButton = new JButton("Back to Menu");
        exitButton.setBounds(450, 550, 300, 50);
        exitButton.setBackground(new Color(0, 100, 0));
        exitButton.setForeground(Color.WHITE); // Set button text color to white
        exitButton.setFont(new Font("Courier New", Font.BOLD, 20));

        exitButton.setActionCommand("Back");
        exitButton.addActionListener(new ButtonClickListener());
        this.add(exitButton);




        // Set JFrame properties
        this.setSize(GameUI.width, GameUI.height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Mahjong");
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(0, 100, 0));

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
                game.setBanker(maxIndex);
                game.startGame();
                dispose();

            } else if (command.equals("Back")) {
                PreparationUI.this.dispose();
                new App().initializeUI();
            }
        }
    }
}
