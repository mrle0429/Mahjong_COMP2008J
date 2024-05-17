package View;

import Controller.Game;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class GameUI extends JFrame implements MouseListener {
    private Game game;
    private Player currentPlayer;
    private Player banker;
    private List<Player> optionPlayers;
    public static int height = 800;
    public static int width = 1200;
    List<Button> buttons = new ArrayList<>();
    List<Button> otherButtons = new ArrayList<>();
    List<Button> showButtons;
    private Image image = null;
    private Graphics gf = null;
    private Tile laiZi;
    private Tile selectTile = null;
    private boolean failDiscard = false;
    private boolean failPung = false;
    private boolean failChow = false;
    private boolean failKong = false;
    private boolean hasWinner = false;
    private boolean noTiles = false;
    private boolean selfTurn = true;
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
        banker = game.findZhuang();
        laiZi = game.getTileStack().getLaiZi();
        optionPlayers = new ArrayList<>();
        loadTimes = 0;
        tracker = new MediaTracker(this);

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

        Button button = new Button("Discard");
        button.setBounds(350, 555, 90, 40);
        buttons.add(button);

        button = new Button("Pung");
        button.setBounds(465, 555, 55, 40);
        buttons.add(button);

        button = new Button("Kong");
        button.setBounds(545, 555, 55, 40);
        buttons.add(button);

        button = new Button("Change tile order");
        button.setBounds(500, 740, 230, 40);
        buttons.add(button);

//        button = new Button("Chow");
//        button.setBounds(625, 555, 55, 40);
//        buttons.add(button);

        this.setSize(width, height);

        this.setTitle("Mahjong Game");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLocationRelativeTo(null);

        this.addMouseListener(this);

        this.setVisible(true);

        startDealing();
    }

    @Override
    public void paint(Graphics g) {
        // 添加临时图片 解决屏幕闪烁问题
        if (image == null) {
            image = this.createImage(1200, 800);
        }
        if (this.gf == null) {
            this.gf = image.getGraphics();
        }

        gf.drawImage(backgroundImage, 0, 0, null);
        gf.drawImage(leftPlayerTile, 150, 200, null);
        gf.drawImage(rightPlayerTile, 1050, 200, null);
        gf.drawImage(topPlayerTile, 220, 180, null);
        gf.setFont(new Font("宋体", Font.BOLD, 24));


        paintBankerInfo();

        if (!currentPlayer.getHand().isDealingFinished()){
            List<Tile> tileList = currentPlayer.getHand().getTiles();
            for (int i = 0; i != currentTileIndex - 1; i++) {
                Image tile = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Resources/" + tileList.get(i) + ".png"));
                loadSingleImage(tile);
                gf.drawImage(tile, 240 + (53 * i), 630, null);
            }

            g.drawImage(image, 0, 0, null);
            return;
        }

        paintMessages();

        List<Tile> tileList = currentPlayer.getHand().getTiles();
        for (int i = 0; i != tileList.size(); i++) {
            Image tile = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Resources/" + tileList.get(i) + ".png"));
            loadSingleImage(tile);
            if (tileList.get(i) != selectTile) {
                gf.drawImage(tile, 240 + (53 * i), 650, null);
            } else {
                gf.drawImage(tile, 240 + (53 * i), 630, null);
            }
        }

        List<Tile> meldTiles = currentPlayer.getHand().getMeldTiles();
        for (int i = 0; i != meldTiles.size(); i++) {
            Image tile = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Resources/" + meldTiles.get(i) + ".png"));
            loadSingleImage(tile);
            gf.drawImage(tile, 1003 - (53 * i), 650, null);
        }

        List<Tile> discardTiles = game.getTileStack().getDiscardTiles();
        for (int i = 0; i != discardTiles.size(); i++) {
            Image tile = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Resources/" + discardTiles.get(i) + ".png"));
            loadSingleImage(tile);
            gf.drawImage(tile, 220 + (53 * ((i + 15) % 15)), 300 + (i / 15) * 35, null);
        }

        // 对于不同的界面绘制不同的按钮
        if (selfTurn){
            showButtons = buttons;
        }else{
            showButtons = otherButtons;
        }

        for (Button button : showButtons) {
            gf.setColor(Color.GRAY);
            gf.fillRect(button.getX(), button.getY(), button.getWidth(), button.getHeight());

            gf.setFont(new Font("宋体", Font.BOLD, 24));
            gf.setColor(Color.BLACK);
            gf.drawString(button.getLabel(), button.getX(), button.getY() + 36);
        }

        g.drawImage(image, 0, 0, null);
    }

    private void loadSingleImage(Image image){
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
        if (!currentPlayer.getHand().isDealingFinished()){
            return;
        }

        if (e.getButton() == MouseEvent.BUTTON1) {
            int xPos = e.getX();
            int yPos = e.getY();

            String name = "";

            for (Button button : showButtons) {
                if (button.getBounds().contains(xPos, yPos)) {
                    name = button.getLabel();
                }
            }

            if (selfTurn && "Discard".equals(name)){
                if (selectTile == null){
                    failDiscard = true;
                    repaint();
                }else{
                    game.playerDiscardTile(currentPlayer, selectTile);
                    Player currentTestPlayer = currentPlayer;
                    for (int i = 0; i != 3; i++) {
                        currentTestPlayer = game.getNextPlayer(currentTestPlayer);
                        boolean canEat = false;
                        if (currentTestPlayer.getLocation() == currentPlayer.getLocation().next()){
                            canEat = true;
                        }
                        playerOperations(currentTestPlayer, selectTile, canEat);
                    }
                    updateGame();
                }
                return;
            } else if (selfTurn && "Pung".equals(name)) {
                if (!currentPlayer.getHand().canPeng()){
                    failPung = true;
                }
                repaint();
                return;
            } else if (selfTurn && "Kong".equals(name)) {
                if (!currentPlayer.getHand().canGang()){
                    failKong = true;
                }else{
                    currentPlayer.drawTile(game.getTileStack());
                }
                repaint();
                return;
            } else if (selfTurn && "Change tile order".equals(name)) {
                if (currentPlayer.getHand().getTileSortType() == TileSortType.MinToMax){
                    currentPlayer.getHand().setMaxType();
                }else{
                    currentPlayer.getHand().setMinType();
                }
                repaint();
                return;
            } else if (!selfTurn && "Pass".equals(name)) {
                updateGame();
                return;
            } else if (!selfTurn && "Pung".equals(name)) {
                // 这张牌被这名玩家碰了 所以其他玩家没有机会再碰 自然清楚
                optionPlayers.clear();

                currentPlayer.getHand().operation(MeldType.PENG, selectTile);
                game.getTileStack().getDiscardTiles().remove(selectTile);

                selfTurn = true;
                game.setCurrentPlayer(currentPlayer);
                selectTile = null;

                repaint();
//                game.setCurrentPlayer(game.getLastPlayer(currentPlayer));
//                updateGame();
                return;
            } else if (!selfTurn && "Kong".equals(name)) {
                optionPlayers.clear();

                currentPlayer.getHand().operation(MeldType.GANG, selectTile);
                game.getTileStack().getDiscardTiles().remove(selectTile);

                // 因为杠牌是需要一下出4张，这个时候需要让这名玩家取一张牌的同时，还要出一张牌，为了保持游戏特性的同时维持手牌稳定
                currentPlayer.getHand().addTile(game.getTileStack().takeTile());

                selfTurn = true;
                game.setCurrentPlayer(currentPlayer);
                selectTile = null;

                repaint();
                return;
            } else if (!selfTurn && "Chow".equals(name)) {
                optionPlayers.clear();

                currentPlayer.getHand().operation(MeldType.EAT, selectTile);
                game.getTileStack().getDiscardTiles().remove(selectTile);

                selfTurn = true;
                game.setCurrentPlayer(currentPlayer);
                selectTile = null;

                repaint();
                return;
            }

            if (selfTurn) {
                List<Tile> tiles = currentPlayer.getHand().getTiles();

                int widthArea = tiles.size() * 53;
                if (xPos < 240 || xPos > 239 + widthArea || yPos < 630 || yPos > 700) {
                    selectTile = null;
                } else {
                    if (selectTile == null) {
                        int tilePos = (xPos - 240) / 53;
                        selectTile = tiles.get(tilePos);
                    } else {
                        selectTile = null;
                    }
                }
            }
        }

        resetMessage();
        repaint();
    }

    public void playerOperations(Player player, Tile tile, boolean canEat) {
        boolean isPeng = player.getHand().canPeng(tile);
        boolean isGang = player.getHand().canGang(tile);
        boolean isEat = false;
        if (canEat){
            isEat = player.getHand().canEat(tile);
        }

        if (isPeng || isGang || isEat){
//            Button button = new Button("Pung");
//            button.setBounds(465, 555, 55, 40);
//            otherButtons.add(button);
            optionPlayers.add(player);
        }
    }

    public void updateGame(){
        // 如果当前玩家出的牌可以被其他玩家碰 吃 杠操作的时候 的逻辑
        if (!optionPlayers.isEmpty()){
            // 设置出这张牌的玩家，这样方便检测吃的操作
            Player originalPlayer = game.getCurrentPlayer();

            Player optionsPlayer = optionPlayers.get(0);

            optionsPlayer.getHand().setDealingFinished(); // 如果这个人需要执行碰等操作的时候 但是他之前还没有看见自己的牌 则屏蔽发牌过程

            currentPlayer = optionsPlayer;
            selfTurn = false;

            optionPlayers.remove(optionsPlayer);
            // 对于这名玩家的专属按钮进行重置
            otherButtons = new ArrayList<>();

            Button button;

            if (optionsPlayer.getHand().canPeng(selectTile)){
                button = new Button("Pung");
                button.setBounds(465, 555, 55, 40);
                otherButtons.add(button);
            }

            if (optionsPlayer.getHand().canGang(selectTile)){
                button = new Button("Kong");
                button.setBounds(545, 555, 55, 40);
                otherButtons.add(button);
            }

            if (optionsPlayer.getLocation() == originalPlayer.getLocation().next()){
                if (optionsPlayer.getHand().canEat(selectTile)){
                    button = new Button("Chow");
                    button.setBounds(625, 555, 55, 40);
                    otherButtons.add(button);
                }
            }

            button = new Button("Pass");
            button.setBounds(350, 555, 75, 40);
            otherButtons.add(button);

            repaint();
            return;
        }

        //正常情况下，下一名玩家的逻辑
        selfTurn = true;
        if (game.getTileStack().isEmpty()){
            noTiles = true;
            repaint();
            return;
        }
        game.updateGame();
        if (game.isHasWinner()){
            hasWinner = true;
        }
        selectTile = null;
        currentPlayer = game.getCurrentPlayer();
        startDealing();
        repaint();
    }

    public void paintBankerInfo(){
        gf.setFont(new Font("宋体", Font.BOLD, 24));
        gf.setColor(Color.BLACK);
        gf.drawString("The banker is: " + banker, 220, 100);

        gf.drawString("LaiZi is: ", 550, 100);
        Image tile = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("Resources/" + laiZi + ".png"));
        loadSingleImage(tile);
        gf.drawImage(tile, 700, 65, null);

        gf.setColor(Color.BLACK);
        gf.drawString(currentPlayer + "'s turn", 240, 780);
    }

    public void paintMessages(){
        if (failDiscard){
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
            gf.drawString(currentPlayer + " wins the Game!", 350, 500);
        } else if (noTiles) {
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

    public void resetMessage(){
        failDiscard = false;
        failPung = false;
        failChow = false;
        failKong = false;
    }

    public void startDealing(){
        currentTileIndex = 1;
        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentTileIndex < 15){
                    currentTileIndex++;
                }else{
                    timer.stop();
                    currentPlayer.getHand().setDealingFinished();
                }
                repaint();
            }
        });
        timer.start();
    }
}
