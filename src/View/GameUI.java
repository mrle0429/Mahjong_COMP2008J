package View;

import Controller.Game;
import Model.Player;
import Model.Tile;
import Model.TileStack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class GameUI extends JFrame implements MouseListener {
    private Game game;
    private Player currentPlayer;
    private Player banker;
    public static int height = 800;
    public static int width = 1200;
    List<Button> buttons = new ArrayList<>();
    private Image image = null;
    private Graphics gf = null;
    private Tile laiZi;
    private Tile selectTile = null;
    private boolean failDiscard = false;
    private boolean hasWinner = false;
    private boolean noTiles = false;
    Image backgroundImage = Toolkit.getDefaultToolkit().getImage("src\\Resources\\background.png");
    Image leftPlayerTile = Toolkit.getDefaultToolkit().getImage("src\\Resources\\leftP.png");
    Image rightPlayerTile = Toolkit.getDefaultToolkit().getImage("src\\Resources\\rightP.png");
    Image topPlayerTile = Toolkit.getDefaultToolkit().getImage("src\\Resources\\topP.png");

    public GameUI(Game game) {
        this.game = game;
    }

    public void initializeUI() {
        currentPlayer = game.getCurrentPlayer();
        banker = game.findZhuang();
        laiZi = game.getTileStack().getLaiZi();

        Button button = new Button("Discard");
        button.setBounds(350, 555, 90, 40);
        button.setBackground(Color.WHITE);
        buttons.add(button);

        this.setSize(width, height);

        this.setTitle("Mahjong");

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

        gf.drawImage(backgroundImage, 0, 0, null);
        gf.drawImage(leftPlayerTile, 150, 200, null);
        gf.drawImage(rightPlayerTile, 1050, 200, null);
        gf.drawImage(topPlayerTile, 220, 180, null);
        gf.setFont(new Font("宋体", Font.BOLD, 24));

        paintBankerInfo();

        paintMessages();

        gf.setColor(Color.BLACK);
        gf.drawString(currentPlayer + "'s turn", 240, 780);

        List<Tile> tileList = currentPlayer.getHand().getTiles();
        for (int i = 0; i != tileList.size(); i++) {
            Image tile = Toolkit.getDefaultToolkit().getImage("src\\resources\\" + tileList.get(i) + ".png");
            if (tileList.get(i) != selectTile) {
                gf.drawImage(tile, 240 + (53 * i), 650, null);
            } else {
                gf.drawImage(tile, 240 + (53 * i), 630, null);
            }
        }

        List<Tile> discardTiles = game.getTileStack().getDiscardTiles();
        for (int i = 0; i != discardTiles.size(); i++) {
            Image tile = Toolkit.getDefaultToolkit().getImage("src\\resources\\" + discardTiles.get(i) + ".png");
            gf.drawImage(tile, 220 + (53 * ((i + 15) % 15)), 300 + (i / 15) * 35, null);
        }

        for (Button button : buttons) {
            gf.setColor(Color.GRAY);
            gf.fillRect(button.getX(), button.getY(), button.getWidth(), button.getHeight());

            gf.setFont(new Font("宋体", Font.BOLD, 24));
            gf.setColor(Color.BLACK);
            gf.drawString(button.getLabel(), button.getX(), button.getY() + 36);
        }


        g.drawImage(image, 0, 0, null);
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

            if ("Discard".equals(name)){
                if (selectTile == null){
                    failDiscard = true;
                    repaint();
                }else{
                    game.playerDiscardTile(currentPlayer, selectTile);
                    updateGame();
                }
                return;
            }

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

        resetMessage();
        repaint();
    }

    public void updateGame(){
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
        repaint();
    }

    public void paintBankerInfo(){
        gf.setFont(new Font("宋体", Font.BOLD, 24));
        gf.setColor(Color.BLACK);
        gf.drawString("The banker is: " + banker, 220, 100);

        gf.drawString("LaiZi is: ", 550, 100);
        Image tile = Toolkit.getDefaultToolkit().getImage("src\\resources\\" + laiZi + ".png");
        gf.drawImage(tile, 700, 65, null);
    }

    public void paintMessages(){
        if (failDiscard){
            gf.setColor(Color.RED);
            gf.drawString("You must select a tile to discard", 350, 500);
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
    }
}
