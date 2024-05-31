package View;

import Controller.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * The UI for rolling dice to determine the banker
 * The player who rolls the highest number will be the banker
 */
public class DiceRollUI extends JFrame {
    private JLabel diceResultLabel;
    private JTextArea diceResultArea;
    private int[] diceResults;
    private int rollCount;
    private int maxIndex;
    private Game game;

    public DiceRollUI(Game game) {
        diceResults = new int[4];
        rollCount = 0;
        initializeUI();
        this.game = game;
    }


    private void initializeUI() {
        this.setLayout(null);
        this.getContentPane().setBackground(new Color(34, 139, 34));

        diceResultLabel = new JLabel();
        diceResultLabel.setHorizontalAlignment(JLabel.CENTER);
        diceResultLabel.setFont(new Font("Arial", Font.BOLD, 48));

        this.setBounds(GameUI.width / 2 - 200, GameUI.height / 2 - 100, 400, 200);
        this.add(diceResultLabel, BorderLayout.CENTER);

        diceResultArea = new JTextArea();
        diceResultArea.setEditable(false);
        diceResultArea.setFont(new Font("Arial", Font.PLAIN, 25));
        diceResultArea.setBackground(new Color(34, 139, 34));
        diceResultArea.setForeground(Color.WHITE);
        diceResultArea.setLineWrap(false);
        JScrollPane scrollPane = new JScrollPane(diceResultArea);
        scrollPane.setBounds(GameUI.width / 2 - 200, GameUI.height / 2 - 300, 400, 200);
        this.add(scrollPane, BorderLayout.NORTH);

        JButton rollDiceButton = new JButton("Roll the dice");
        rollDiceButton.setFont(new Font("Arial", Font.BOLD, 36));
        rollDiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rollCount < 4) {
                    rollDice(false);
                } else if (rollCount == 4) {
                    rollDice(true);
                }
            }
        });

        rollDiceButton.setBounds(GameUI.width / 2 - 200, GameUI.height - 300, 400, 50);
        this.add(rollDiceButton, BorderLayout.SOUTH);

        this.setTitle("Mahjong Game");

        this.setSize(GameUI.width, GameUI.height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Roll the dice and display the result
     * @param finish whether the game is finished
     */
    private void rollDice(boolean finish) {
        if (!finish) {
            int diceResult = new Random().nextInt(6) + 1;
            diceResults[rollCount] = diceResult;
            diceResultArea.append("Player " + (rollCount + 1) + " rolled " + diceResult + "\n");
            diceResultLabel.setText("Player " + (rollCount + 1) + " rolled " + diceResult);
            rollCount++;
        }

        if (finish) {
            maxIndex = 0;
            for (int i = 1; i < diceResults.length; i++) {
                if (diceResults[i] > diceResults[maxIndex]) {
                    maxIndex = i;
                }
            }
            diceResultArea.append("Player " + (maxIndex + 1) + " is the banker!\n");
            diceResultLabel.setText("Player " + (maxIndex + 1) + " is the banker!");
            rollCount++;

            JButton continueButton = new JButton("Continue game");
            continueButton.setBounds(GameUI.width / 2 - 200, GameUI.height - 200, 400, 50);
            continueButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Start the game
                    game.setStart();
                    game.setZhuang(maxIndex);
                    game.startGame();
                    DiceRollUI.this.dispose();
                }
            });
            this.add(continueButton, BorderLayout.SOUTH);

            JButton returnButton = new JButton("Return to menu");
            returnButton.setBounds(GameUI.width / 2 - 200, GameUI.height - 150, 400, 50);
            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Return to the preparation UI
                    new Game().startGame();
                    DiceRollUI.this.dispose();
                }
            });
            this.add(returnButton, BorderLayout.SOUTH);

            this.revalidate();
            this.repaint();
        }
    }


}
