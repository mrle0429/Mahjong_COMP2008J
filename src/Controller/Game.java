package Controller;


import Model.*;
import Util.CheckTile;
import Util.WaysOfHu;


import View.GameUI;
import View.PreparationUI;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players;
    private TileStack tileStack;
    private Player currentPlayer;

    private GameUI gameUI;
    private PreparationUI preparationUI;

    private boolean isStart;
    private boolean isGameOver;
    private boolean hasWinner;



    public Game() {
        players = new ArrayList<>();
        tileStack = new TileStack();
        hasWinner = false;
        isStart = false;
        isGameOver = false;
        gameUI = new GameUI(this);
        preparationUI = new PreparationUI(this);
    }

    // 初始化游戏
    public void initializeGame() {
        addPlayer();                 // 添加四名玩家
        preparationUI.initializeUI();
    }

    public void startGame() {
        if (!isStart) {
            initializeGame();
            return;
        }
        distributeTile(); // 发牌

        currentPlayer = findZhuang();
        gameUI.initializeUI();

        // 地主额外获得一张牌
        playerDrawTile(currentPlayer);
        //gameUI.updateGameUI();
    }

    private void addPlayer() {
        players.add(new Player(PlayerType.East));
        players.add(new Player(PlayerType.South));
        players.add(new Player(PlayerType.West));
        players.add(new Player(PlayerType.North));
    }

    private void distributeTile() {
        for (Player player : players) {
            for (int i = 0; i != 13; i++) {
                player.drawTile(tileStack);
            }
        }
    }


    public Player getNextPlayer(Player player) {
        for (Player p : players) {
            if (p.getLocation() == player.getLocation().next()) {
                return p;
            }
        }
        return null;
    }

    public Player getLastPlayer(Player player) {
        for (Player p : players) {
            if (player.getLocation() == p.getLocation().next()) {
                return p;
            }
        }
        return null;
    }

    public void moveToNext(Player player) {
        currentPlayer = getNextPlayer(player);
    }

    public Player findZhuang() {
        for (Player player : players) {
            if (player.isZhuang()) {
                return player;
            }
        }
        return null;
    }


    public void updateScore() {
        //System.out.println("updateScore");
        if (!hasWinner) {return;}

        Player winner = null;
        int baseScore = 1;

        for (Player player : players) {
            if (player.isWinner()) {
                winner = player;
                break;
            }
        }

        if (winner == null) return;

        int fan = WaysOfHu.calculateScore(winner.getHand().getTiles());
        int totalScore = baseScore + fan;

        for (Player otherPlayer : players) {
            if (otherPlayer != winner) {
                otherPlayer.setScore(otherPlayer.getScore() - totalScore);
                winner.setScore(winner.getScore() + totalScore);
            }
        }
    }



    public void playerDiscardTile(Player player, Tile tile) {
        player.discardTile(tileStack, tile);
    }

    public void playerDrawTile(Player player) {
        if (tileStack.isEmpty()) {
            isGameOver = true;
            return;
        }
        player.drawTile(tileStack);
        checkIsWin(player);
        gameUI.updateGameUI();
    }

    public void updateGame() {
        moveToNext(currentPlayer);
        if (checkTileStackEmpty()) {
            isGameOver = true;
        }

        currentPlayer.drawTile(tileStack);
        if (checkIsWin(currentPlayer)) {
            hasWinner = true;
            updateScore();
        }
    }

    public TileStack getTileStack() {
        return tileStack;
    }

    public boolean checkIsWin(Player player) {
        if (player.isWinner()){
            hasWinner = true;
        }
        return player.isWinner();
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setStart() {
        isStart = true;
    }

    public void setZhuang(int index) {
        players.get(index).setZhuang(true);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Tile> getPlayerTiles(Player player) {
        return player.getTiles();
    }

    public List<Tile> getPlayerMeldTiles(Player player) {
        return player.getMeldTiles();
    }

    public List<Tile> getDiscardTiles() {
        return tileStack.getDiscardTiles();
    }

    public boolean checkEat(Player player, Tile tile) {
        return player.checkEat(tile);
    }

    public boolean checkPung(Player player, Tile tile) {
        return player.checkPung(tile);
    }

    public boolean checkGang(Player player, Tile tile) {
        return player.checkGang(tile);
    }

    public void playerPungTile(Player player, Tile tile) {
        player.pengTile(tile);
        tileStack.getDiscardTiles().remove(tile);
    }

    public void playerGangTile(Player player, Tile tile) {
        player.gangTile(tile);
        tileStack.getDiscardTiles().remove(tile);
    }

    public void playerEatTile(Player player, Tile tile) {
        player.eatTile(tile);
        tileStack.getDiscardTiles().remove(tile);
    }

    public void playerAnGangTile(Player player) {
        player.anGangTile();
    }

    public boolean checkTileStackEmpty() {
        if (tileStack.isEmpty()) {
            isGameOver = true;
        }
        return tileStack.isEmpty();
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean hasWinner() {
        return hasWinner;
    }
}
