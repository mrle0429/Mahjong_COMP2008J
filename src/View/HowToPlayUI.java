package View;

/**
 * @author Le Liu
 * @create 2024-05
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HowToPlayUI extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    public HowToPlayUI() {

    }

    public void initializeUI() {
        this.setTitle("How to Play");
        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(cardLayout);
        cardPanel.setBackground(Color.LIGHT_GRAY);

        // Create pages
        JPanel mahjongPage = createPage("What are Chinese Mahjong tiles?",
                "Mahjong tiles are classified into five \n types: Dots, Bams, Craks, Winds and \n Dragons.",
                "src/Resources/MahjongTiles.png");
        JPanel chowPage = createPage("Which tile can form a chow?", "Chow refers to a specific\n" +
                "combination of three\n" +
                "consecutive tiles of the same\n" +
                "suit, also known as sequence.", "src/Resources/chow.png");
        JPanel pengPage = createPage("What is Pung?", "Pung refers to a specific\n" +
                "combination of three same tiles,\n" +
                "also known as triplet.\n" +
                "\n", "src/Resources/pung.png");
        JPanel gangPage = createPage("What is Kong?", "Kong refers to a specific\n" +
                "combination of four same tiles,\n" +
                "also known as a quadruplet.", "src/Resources/kong.png");
        JPanel pairPage = createPage("What is Pair?", "Pair refers to a specific\n" +
                "combination of two same tiles,\n" +
                "\n" +
                "also known as a double.", "src/Resources/pair.png");
        JPanel winningHandPage = createPage("What is Winning hand?", "Basic formula for winning is:\n" +
                "m*AAA+n*ABC+DD\n" +
                "\n" +
                "DD=Pair\n" +
                "\n" +
                "ABC=Sequence\n" +
                "AAA=Triplet↳\n" +
                "\n" +
                "m+n=4", "src/Resources/winninghand.png");

        // Add pages to card panel
        cardPanel.add(mahjongPage, "Mahjong Tiles");
        cardPanel.add(chowPage, "Chow");
        cardPanel.add(pengPage, "Pung");
        cardPanel.add(gangPage, "Kong");
        cardPanel.add(pairPage, "Pair");
        cardPanel.add(winningHandPage, "Winning Hand");

        // Create and add navigation buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.DARK_GRAY);
        String[] pageNames = {"Mahjong Tiles", "Chow", "Pung", "Kong", "Pair", "Winning Hand"};
        for (String pageName : pageNames) {
            JButton button = new JButton(pageName);
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setForeground(Color.WHITE);
            button.setBackground(Color.DARK_GRAY);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, pageName);
                }
            });
            buttonPanel.add(button);
        }

        this.add(cardPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }


    private JPanel createPage(String content1, String content2, String png) {
        Color customGreen = new Color(250, 250, 250);

        JPanel page = new JPanel(null);
        page.setBackground(customGreen);
        page.setLayout(new BorderLayout());

        // Create the image label for the center position
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(new Color(0, 0, 0, 0));
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(png).getImage().getScaledInstance(700, 500, Image.SCALE_DEFAULT));
        JLabel imageLabel = new JLabel(imageIcon);
        page.add(imageLabel, BorderLayout.CENTER);

        // Create the text area for the east position
        JTextPane textPane2 = new JTextPane();
        textPane2.setText("\n\n\n\n\n\n\n" + content2);
        textPane2.setEditable(false);
        textPane2.setFont(new Font("Courier New", Font.BOLD, 20));
        textPane2.setForeground(Color.GRAY);
        textPane2.setBackground(customGreen);
        textPane2.setBorder(BorderFactory.createLineBorder(customGreen));
        page.add(textPane2, BorderLayout.EAST);

        // Create the text area for the north position
        JTextPane textPane1 = new JTextPane();
        textPane1.setText("                 " + content1);
        textPane1.setEditable(false);
        textPane1.setFont(new Font("Courier New", Font.BOLD, 30));
        textPane1.setForeground(Color.DARK_GRAY);
        textPane1.setBackground(customGreen);
        textPane1.setBorder(BorderFactory.createLineBorder(customGreen));
        page.add(textPane1, BorderLayout.NORTH);

        return page;
    }
}