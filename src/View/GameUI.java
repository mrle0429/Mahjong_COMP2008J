package View;

import Controller.Game;
import Model.Player;
import Model.Tile;
import Model.TileSortType;
import Sound.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class GameUI extends JFrame implements MouseListener {
    private static final String TITLE = "Mahjong Game";
    public static final int width = 1200;
    public static final int height = 800;

    private final Game game;
    private Player currentPlayer;
    private List<Player> optionPlayers;
    private final SoundPlayer soundPlayer = new SoundPlayer();

    List<Button> buttons = new ArrayList<>();
    List<Button> otherButtons = new ArrayList<>();
    List<Button> showButtons;

    private Image image = null;
    private Graphics gf = null;
    private Tile selectTile = null;

    private boolean failDiscard = false;
    private boolean failPung = false;
    private boolean failChow = false;
    private boolean failKong = false;
    private boolean selfTurn = true;
    private boolean gameOver;

    private MediaTracker tracker;
    private int loadTimes;

    private Image backgroundImage;
    private Image leftPlayerTile;
    private Image rightPlayerTile;
    private Image topPlayerTile;

    private int currentTileIndex;
    private Timer timer;

    public GameUI(Game game) {
        this.game = game;

    }

    public void initializeUI() {
        currentPlayer = game.getCurrentPlayer();
        optionPlayers = new ArrayList<>();
        loadTimes = 0;
        tracker = new MediaTracker(this);
        gameOver = false;

        backgroundImage = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Resources/background.png"));
        leftPlayerTile = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Resources/leftP.png"));
        rightPlayerTile = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Resources/rightP.png"));
        topPlayerTile = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Resources/topP.png"));

        tracker.addImage(backgroundImage, loadTimes++);
        tracker.addImage(leftPlayerTile, loadTimes++);
        tracker.addImage(rightPlayerTile, loadTimes++);
        tracker.addImage(topPlayerTile, loadTimes++);

        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Button discardButton = new Button("Discard");
        discardButton.setBounds(350, 555, 95, 40);
        buttons.add(discardButton);

        Button changeButton = new Button("Change tile order");
        changeButton.setBounds(500, 740, 230, 40);
        buttons.add(changeButton);


        this.setSize(width, height);

        this.setTitle(TITLE);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLocationRelativeTo(null);

        this.addMouseListener(this);

        this.setVisible(true);

        startDealing();

    }

    @Override
    public void paint(Graphics g) {
        // avoid flicker
        if (image == null) {
            image = this.createImage(1200, 800);
        }
        if (this.gf == null) {
            this.gf = image.getGraphics();
        }

        drawBackground();
        paintBankerInfo();
        paintMessages();

        if (!currentPlayer.firstDrawFinish()) {
            paintTileFirstTime();
            g.drawImage(image, 0, 0, null);
            return;
        }

        paintTile();
        paintMeldTiles();
        paintDiscardTile();

        if (game.checkConcealedKong(currentPlayer)) {
            Button kongButton = new Button("Kong");
            kongButton.setBounds(545, 555, 70, 40);
            buttons.add(kongButton);
        }


        if (selfTurn) {
            showButtons = buttons;
        } else {
            showButtons = otherButtons;
        }

        paintButton();
        g.drawImage(image, 0, 0, null);
    }

    private void drawBackground() {
        gf.drawImage(backgroundImage, 0, 0, null);
        gf.drawImage(leftPlayerTile, 150, 200, null);
        gf.drawImage(rightPlayerTile, 1050, 200, null);
        gf.drawImage(topPlayerTile, 220, 180, null);
    }


    private void paintTile() {
        List<Tile> tileList = game.getPlayerTiles(currentPlayer);
        for (int i = 0; i != tileList.size(); i++) {
            Image tile = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Resources/" + tileList.get(i) + ".png"));
            loadSingleImage(tile);
            if (tileList.get(i) != selectTile) {
                gf.drawImage(tile, 240 + (53 * i), 650, null);
            } else {
                gf.drawImage(tile, 240 + (53 * i), 630, null);
            }
        }
    }

    private void paintTileFirstTime() {
        List<Tile> tileList = game.getPlayerTiles(currentPlayer);
        for (int i = 0; i != currentTileIndex - 1; i++) {
            Image tile = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Resources/" + tileList.get(i) + ".png"));
            loadSingleImage(tile);
            gf.drawImage(tile, 240 + (53 * i), 630, null);
        }
    }

    private void paintMeldTiles() {

        List<Tile> meldTiles = game.getPlayerMeldTiles(currentPlayer);
        for (int i = 0; i != meldTiles.size(); i++) {
            Image tile = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Resources/" + meldTiles.get(i) + ".png"));
            loadSingleImage(tile);
            gf.drawImage(tile, 1003 - (53 * i), 650, null);
        }
    }

    private void paintDiscardTile() {
        if (gameOver) {
            return;
        }

        List<Tile> discardTiles = game.getDiscardTiles();
        for (int i = 0; i != discardTiles.size(); i++) {
            Image tile = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Resources/" + discardTiles.get(i) + ".png"));
            loadSingleImage(tile);
            gf.drawImage(tile, 220 + (53 * ((i + 15) % 15)), 300 + (i / 15) * 35, null);
        }
    }

    private void paintButton() {
        for (Button button : showButtons) {
            gf.setColor(Color.GRAY);
            gf.fillRect(button.getX(), button.getY(), button.getWidth(), button.getHeight());

            gf.setFont(new Font("Arial", Font.BOLD, 24));
            gf.setColor(Color.BLACK);
            gf.drawString(button.getLabel(), button.getX() + 5, button.getY() + 30);
        }
    }

    private void loadSingleImage(Image image) {
        tracker.addImage(image, loadTimes++);
        try {
            tracker.waitForID(loadTimes - 1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        // Show the first card dealing process, no other operations are allowed
        if (!currentPlayer.firstDrawFinish() || gameOver) {
            return;
        }

        // Detecting the left mouse button
        if (e.getButton() == MouseEvent.BUTTON1) {
            int xPos = e.getX();
            int yPos = e.getY();

            String buttonName = "";


            // Detecting button clicks
            for (Button button : showButtons) {
                if (button.getBounds().contains(xPos, yPos)) {
                    buttonName = button.getLabel();
                }
            }

            if (selfTurn) {
                switch (buttonName) {
                    case "Discard":
                        if (selectTile == null) {
                            failDiscard = true;
                            repaint();
                        } else {
                            soundPlayer.playSoundEffect("src/Resources/discard.wav");
                            game.playerDiscardTile(currentPlayer, selectTile);

                            // Check if other players can touch or eat the kong, if so, add them to optionPlayers
                            checkAndAddOptionPlayers();

                            updateGame();
                        }
                        return;

                    case "Pung":
                        // Cannot touch in your own turn, simplify the code
                        failPung = true;
                        repaint();
                        return;
                    case "Kong":
                        // In your turn, you can only use the hidden kang
                        game.playerConcealedKongTile(currentPlayer);
                        repaint();
                        return;
                    case "Change tile order":
                        if (currentPlayer.getHand().getTileSortType() == TileSortType.MinToMax) {
                            currentPlayer.getHand().setMaxType();
                        } else {
                            currentPlayer.getHand().setMinType();
                        }
                        repaint();
                        return;
                }
            } else {
                // Not self turn

                switch (buttonName) {
                    case "Pass":
                        updateGame();
                        return;

                    case "Pung":
                        // This card is touched by this player, so other players have no chance to touch it again. It is cleared naturally.
                        optionPlayers.clear();

                        soundPlayer.playSoundEffect("src/Resources/pung.wav");

                        game.playerPungTile(currentPlayer, selectTile);  // The touched card is added to the hand and removed from the discard pile

                        game.checkIsWin(currentPlayer);

                        selfTurn = true;
                        game.setCurrentPlayer(currentPlayer);
                        selectTile = null;

                        repaint();
                        return;

                    case "Kong":
                        optionPlayers.clear();

                        soundPlayer.playSoundEffect("src/Resources/kong.wav");

                        game.playerKongTile(currentPlayer, selectTile);  // The kong is added to the hand and removed from the discard pile

                        // Because a gang requires four cards to be played at once, the player needs to take a card
                        // and play another card at the same time, in order to maintain the characteristics
                        // of the game while keeping the hand stable.
                        game.playerDrawTile(currentPlayer);

                        selfTurn = true;
                        game.setCurrentPlayer(currentPlayer);
                        selectTile = null;

                        repaint();
                        return;
                    case "Chow":
                        optionPlayers.clear();

                        game.playerChowTile(currentPlayer, selectTile);  // The card is added to the hand and removed from the discard pile

                        game.checkIsWin(currentPlayer);

                        selfTurn = true;
                        game.setCurrentPlayer(currentPlayer);
                        selectTile = null;

                        repaint();
                        return;
                }

            }

            if (selfTurn) {
                selectTile = getSelectTile(xPos, yPos);
            }

        }

        resetMessage();
        repaint();
    }

    private Tile getSelectTile(int xPos, int yPos) {
        List<Tile> tiles = currentPlayer.getHand().getTiles();
        Tile tile = null;

        int widthArea = tiles.size() * 53;
        if (xPos < 240 || xPos > 239 + widthArea || yPos < 630 || yPos > 700) {
            tile = null;
        } else {
            if (tile == null) {
                int tilePos = (xPos - 240) / 53;
                tile = tiles.get(tilePos);
            } else {
                tile = null;
            }
        }
        return tile;

    }


    public void checkAndAddOptionPlayers() {
        Player currentTestPlayer = currentPlayer;

        for (int i = 0; i != 3; i++) {
            currentTestPlayer = game.getNextPlayer(currentTestPlayer);

            boolean canPeng = game.checkPung(currentTestPlayer, selectTile);
            boolean canGang = game.checkKong(currentTestPlayer, selectTile);
            boolean canEat = (currentTestPlayer.getLocation() == currentPlayer.getLocation().next()) && game.checkChow(currentTestPlayer, selectTile); // 只有下家能吃
            if (canPeng || canGang || canEat) {
                optionPlayers.add(currentTestPlayer);
            }
        }
    }

    public void updateGame() {
        // If the card played by the current player can be touched, eaten or ganged by other players, the logic of the operation is
        if (!optionPlayers.isEmpty()) {
            // Set the player who played this card, so it is easy to detect the eating operation
            Player originalPlayer = game.getCurrentPlayer();

            Player optionsPlayer = optionPlayers.get(0);

            optionsPlayer.getHand().setDealingFinished();
            // If this person needs to perform an operation such as a touch, but he has not seen his cards before,
            // the card dealing process will be blocked.

            currentPlayer = optionsPlayer;
            selfTurn = false;

            optionPlayers.remove(optionsPlayer);
            // 对于这名玩家的专属按钮进行重置
            otherButtons = new ArrayList<>();

            Button button;

            if (optionsPlayer.checkPung(selectTile)) {
                button = new Button("Pung");
                button.setBounds(465, 555, 70, 40);
                otherButtons.add(button);
            }

            if (optionsPlayer.checkKong(selectTile)) {
                button = new Button("Kong");
                button.setBounds(545, 555, 70, 40);
                otherButtons.add(button);
            }

            if (optionsPlayer.getLocation() == originalPlayer.getLocation().next()) {
                if (optionsPlayer.getHand().canChow(selectTile)) {
                    button = new Button("Chow");
                    button.setBounds(625, 555, 70, 40);
                    otherButtons.add(button);
                }
            }

            button = new Button("Pass");
            button.setBounds(350, 555, 75, 40);
            otherButtons.add(button);

            repaint();
            return;
        }

        // Normally, the logic for the next player
        selfTurn = true;
        if (game.checkTileStackEmpty()) {
            repaint();
            return;
        }
        game.updateGame();

        selectTile = null;
        if (game.hasWinner()) {
            gameOver = true;
        }

        currentPlayer = game.getCurrentPlayer();
        startDealing();
        repaint();
    }

    public void paintBankerInfo() {
        gf.setFont(new Font("Arial", Font.BOLD, 24));
        gf.setColor(Color.RED);

        gf.setColor(Color.WHITE);
        gf.drawString("The banker is: " + game.findBanker(), 220, 130);

        gf.drawString("Player now: ", 550, 130);
        gf.setColor(Color.RED);
        gf.drawString(currentPlayer + "'s turn", 750, 130);

        paintScores(gf);
    }


    private void paintScores(Graphics gf) {
        int yPosition = 80;
        int xPosition = 100;
        int spacing = 250;

        gf.setFont(new Font("Arial", Font.BOLD, 24));
        gf.setColor(Color.white);

        // Iterate through all players and display their scores
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i);
            String scoreText = player.toString() + ": " + player.getScore() + " points";
            gf.drawString(scoreText, xPosition + (i * spacing), yPosition);
        }
    }


    public void paintMessages() {
        if (failDiscard) {
            gf.setColor(Color.RED);
            gf.drawString("You must select a tile to discard", 350, 500);
        } else if (failPung) {
            gf.setColor(Color.RED);
            gf.drawString("You do not have enough to Pung", 350, 500);
        } else if (failChow) {
            gf.setColor(Color.RED);
            gf.drawString("You do not have enough to Chow", 350, 500);
        } else if (failKong) {
            gf.setColor(Color.RED);
            gf.drawString("You do not have enough to Kong", 350, 500);
        } else if (!selfTurn) {
            gf.setColor(Color.YELLOW);
            gf.drawString("You can do options with last tile are as follow.", 350, 500);
        } else if (game.hasWinner()) {
            gf.setColor(Color.YELLOW);
            gf.drawString(currentPlayer + " wins the Game!", 350, 500);
        } else if (game.isGameOver()) {
            gf.setColor(Color.RED);
            gf.drawString("Game Over,There are not enough tile to play!", 340, 500);
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

    public void resetMessage() {
        failDiscard = false;
        failPung = false;
        failChow = false;
        failKong = false;
    }

    public void startDealing() {
        currentTileIndex = 1;
        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentTileIndex < 15) {
                    currentTileIndex++;
                } else {
                    timer.stop();
                    currentPlayer.getHand().setDealingFinished();
                }
                repaint();
            }
        });
        timer.start();
    }

    public void updateGameUI() {
        this.repaint();
    }
}
