package Network;

import Model.*;
import View.NetWaitingUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PlayerClient extends JFrame implements MouseListener {
    public static void main(String[] args) {
        PlayerClient playerClient = new PlayerClient();
        playerClient.connectServer();
    }

    private String name = "Wzh";
    private final String serverIP = "192.168.0.102";
    private final int port = 12345;
    private NetWaitingUI netWaitingUI;
    private Player player;

    // UI information
    private static final String TITLE = "Mahjong Game";
    public static final int width = 1200;
    public static final int height = 800;
    private TileStack tileStack;
    List<Button> buttons;
    List<Button> otherButtons;
    List<Button> showButtons;
    private Image image = null;
    private Graphics gf = null;
    private Tile selectTile = null;
    private boolean failDiscard = false;
    private boolean failPung = false;
    private boolean failChow = false;
    private boolean failKong = false;
    private boolean noTiles = false;
    private MediaTracker tracker;
    private int loadTimes;
    private boolean gameOver;
    private boolean selfTurn;
    private boolean hasWinner;
    private Tile optionTile;
    private String winner;
    private Player banker;
    private Image backgroundImage;
    private Image leftPlayerTile;
    private Image rightPlayerTile;
    private Image topPlayerTile;
    private String turnInformation;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public void connectServer() {
        try {
            Socket socket = new Socket(serverIP, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            MessageType type = (MessageType) ois.readObject();
            if (type == MessageType.Waiting) {
                netWaitingUI = new NetWaitingUI(name);
            }

            type = (MessageType) ois.readObject();
            if (type == MessageType.EnoughPlayers) {
                // 人齐了关闭等待界面的UI
                netWaitingUI.dispose();
            }

            // 接受传给的玩家信息
            player = (Player) ois.readObject();
            Message msg = (Message) ois.readObject();
            banker = msg.getPlayer();
            turnInformation = banker.toString();

            msg = (Message) ois.readObject();
            tileStack = msg.getTileStack();

            initializePlayer();

            while (!gameOver) {
                msg = (Message) ois.readObject();
                if (msg.getType() == MessageType.TakeTurn) {
                    tileStack.getDiscardTiles().add(msg.getTile());
//                    Player originalPlayer = msg.getOriginalPlayer();
//                    if (player.toString().equals(originalPlayer.toString())){
//                        player.getHand().removeTile(msg.getTile());
//                    }
                    Player currentPlayer = msg.getPlayer();
                    turnInformation = currentPlayer.toString();
                    if (player.toString().equals(turnInformation)) {
                        player.getHand().addTile(msg.getNewTile());
                    }
                    repaint();
                } else if (msg.getType() == MessageType.PlayerKung) {
                    player.getHand().addTile(msg.getTile());
                    repaint();
                } else if (msg.getType() == MessageType.OptionWithTile) {
                    selfTurn = false;

                    Button button;
                    otherButtons = new ArrayList<>();

                    optionTile = msg.getTile();
                    tileStack.getDiscardTiles().add(optionTile);

                    if (player.getHand().canPeng(optionTile)) {
                        button = new Button("Pung");
                        button.setBounds(465, 555, 70, 40);
                        otherButtons.add(button);
                    }

                    if (player.getHand().canGang(optionTile)) {
                        button = new Button("Kong");
                        button.setBounds(545, 555, 70, 40);
                        otherButtons.add(button);
                    }

                    if (player.getLocation() == msg.getOriginalPlayer().getLocation().next()) {
                        if (player.getHand().canEat(optionTile)) {
                            button = new Button("Chow");
                            button.setBounds(625, 555, 70, 40);
                            otherButtons.add(button);
                        }
                    }

                    button = new Button("Pass");
                    button.setBounds(350, 555, 75, 40);
                    otherButtons.add(button);

                    repaint();
                } else if (msg.getType() == MessageType.OptionWithPung) {
//                    tileStack.getDiscardTiles().remove(msg.getTile());
                    turnInformation = msg.getPlayer().toString();
                    repaint();
                } else if (msg.getType() == MessageType.OptionWithKong) {
                    turnInformation = msg.getPlayer().toString();
                    if (player.toString().equals(turnInformation)) {
                        player.getHand().addTile(msg.getNewTile());
                    }
                    repaint();
                } else if (msg.getType() == MessageType.OptionWithChow) {
                    turnInformation = msg.getPlayer().toString();
                    repaint();
                } else if (msg.getType() == MessageType.HasWinner) {
                    gameOver = true;
                    hasWinner = true;
                    winner = msg.getPlayer().toString();
                    repaint();
                } else if (msg.getType() == MessageType.NoTiles) {
                    gameOver = true;
                    noTiles = true;
                    repaint();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializePlayer() {
        buttons = new ArrayList<>();
        otherButtons = new ArrayList<>();
        showButtons = new ArrayList<>();

        loadTimes = 0;
        tracker = new MediaTracker(this);
        gameOver = false;
        selfTurn = true;
        hasWinner = false;
        winner = null;

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

        Button kongButton = new Button("Kong");
        kongButton.setBounds(545, 555, 70, 40);
        buttons.add(kongButton);


        Button changeButton = new Button("Change tile order");
        changeButton.setBounds(500, 740, 230, 40);
        buttons.add(changeButton);

        this.setSize(width, height);

        this.setTitle(TITLE);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLocationRelativeTo(null);

        this.addMouseListener(this);

        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        if (image == null) {
            image = this.createImage(1200, 800);
        }
        if (this.gf == null) {
            this.gf = image.getGraphics();
        }

        drawBackground();
        paintBankerInfo();
        paintMessages();

        paintTile();
        paintMeldTiles();
        paintDiscardTile();

        if (selfTurn) {
            showButtons = buttons;
        } else {
            showButtons = otherButtons;
        }

        paintButton();
        g.drawImage(image, 0, 0, null);

    }

    private void paintTile() {
        List<Tile> tileList = player.getTiles();

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

    private void paintMeldTiles() {
        List<Tile> meldTiles = player.getMeldTiles();
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

        List<Tile> discardTiles = tileStack.getDiscardTiles();
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

    private void drawBackground() {
        gf.drawImage(backgroundImage, 0, 0, null);
        gf.drawImage(leftPlayerTile, 150, 200, null);
        gf.drawImage(rightPlayerTile, 1050, 200, null);
        gf.drawImage(topPlayerTile, 220, 180, null);
    }

    public void paintBankerInfo() {
        gf.setFont(new Font("宋体", Font.BOLD, 24));
        gf.setColor(Color.RED); // 修改颜色为红色

        // 修改显示位置，你可以根据需要调整这些值
        int playerInfoX = 400;
        int playerInfoY = 620;

        gf.drawString("You are : " + player, playerInfoX - 160, playerInfoY);
        gf.drawString(turnInformation + "'s turn", playerInfoX + 100, playerInfoY);

        gf.setColor(Color.BLACK);
        gf.drawString("The banker is: " + banker, 220, 100);
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
        } else if (hasWinner) {
            gf.setColor(Color.YELLOW);
            gf.drawString(winner + " wins the Game!", 350, 500);
        } else if (noTiles) {
            gf.setColor(Color.RED);
            gf.drawString("Game Over,There are not enough tile to play!", 340, 500);
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
        if (gameOver) {
            return;
        }

        if (e.getButton() == MouseEvent.BUTTON1) {
            int xPos = e.getX();
            int yPos = e.getY();

            String buttonName = "";


            // 检测点击按钮
            for (Button button : showButtons) {
                if (button.getBounds().contains(xPos, yPos)) {
                    buttonName = button.getLabel();
                }
            }

            if (selfTurn) {
                switch (buttonName) {
                    case "Discard":
                        if (selectTile == null || !turnInformation.equals(player.toString())) {
                            failDiscard = true;
                        } else {
                            player.getHand().removeTile(selectTile);

                            Message message = new Message();
                            message.setType(MessageType.DiscardTile);
                            message.setPlayer(player);
                            message.setTile(selectTile);
                            sendMessage(message);

                            selectTile = null;
                        }
                        repaint();
                        return;
                    case "Kong":
                        // 自己回合杠，仅能暗杠
                        if (player.getHand().canAnGang()) {
                            Message message = new Message();
                            message.setType(MessageType.PlayerKung);
                            message.setPlayer(player);
                            sendMessage(message);
                        }
                        failKong = true;
                        repaint();
                        return;
                    case "Change tile order":
                        if (player.getHand().getTileSortType() == TileSortType.MinToMax) {
                            player.getHand().setMaxType();
                        } else {
                            player.getHand().setMinType();
                        }
                        repaint();
                        return;
                }
            } else {
                switch (buttonName) {
                    case "Pass":
                        tileStack.getDiscardTiles().remove(optionTile);

                        Message messagePass = new Message();
                        messagePass.setType(MessageType.OptionWithPass);
                        messagePass.setPlayer(player);
                        messagePass.setTile(optionTile);
                        sendMessage(messagePass);

                        selfTurn = true;
                        repaint();
                        return;
                    case "Pung":
                        player.getHand().operation(MeldType.PENG, optionTile);
                        tileStack.getDiscardTiles().remove(optionTile);

                        Message message = new Message();
                        message.setType(MessageType.OptionWithPung);
                        message.setTile(optionTile);
                        message.setPlayer(player);
                        sendMessage(message);

                        selfTurn = true;
                        repaint();
                        return;
                    case "Kong":
                        player.getHand().operation(MeldType.GANG, optionTile);
                        tileStack.getDiscardTiles().remove(optionTile);

                        Message messageKong = new Message();
                        messageKong.setType(MessageType.OptionWithKong);
                        messageKong.setTile(optionTile);
                        messageKong.setPlayer(player);
                        sendMessage(messageKong);

                        selfTurn = true;
                        repaint();
                        return;
                    case "Chow":
                        player.getHand().operation(MeldType.EAT, optionTile);
                        tileStack.getDiscardTiles().remove(optionTile);

                        Message messageChow = new Message();
                        messageChow.setType(MessageType.OptionWithChow);
                        messageChow.setTile(optionTile);
                        messageChow.setPlayer(player);
                        sendMessage(messageChow);

                        selfTurn = true;
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

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private Tile getSelectTile(int xPos, int yPos) {
        List<Tile> tiles = player.getHand().getTiles();

        if (selectTile != null) {
            return null;
        }

        int widthArea = tiles.size() * 53;
        if (xPos < 240 || xPos > 239 + widthArea || yPos < 630 || yPos > 700) {
            return null;
        } else {
            int tilePos = (xPos - 240) / 53;
            return tiles.get(tilePos);
        }
    }

    public void resetMessage() {
        failDiscard = false;
        failPung = false;
        failChow = false;
        failKong = false;
    }

    public void sendMessage(Message msg) {
        try {
            oos.writeObject(msg);
            oos.flush();
            waitForFinish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waitForFinish() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
