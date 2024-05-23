package Controller;

import Model.*;
import View.GameUI;
import View.PreparationUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private List<Player> players;
    private TileStack tileStack;
    private boolean hasWinner;
    private  Player currentPlayer;

    private GameUI gameUI;
    private PreparationUI preparationUI;

    private boolean isStart;

    public Game() {
        players = new ArrayList<>();
        tileStack = new TileStack();
        hasWinner = false;
        isStart = false;
        gameUI = new GameUI(this);
        preparationUI = new PreparationUI(this);
    }

    public void initializeGame() {    // 重构初始化函数
        addPlayer();                 // 添加四名玩家
        distributeTile();            // 发牌，同时为庄家多发一张
    }

    public void addPlayer() {
        players.add(new Player(PlayerType.East));
        players.add(new Player(PlayerType.South));
        players.add(new Player(PlayerType.West));
        players.add(new Player(PlayerType.North));
    }

    public void distributeTile() {
        for (Player player : players) {
            for (int i = 0; i != 13; i++) {
                player.getHand().addTile(tileStack.playerDrawTile());
            }
        }
    }
    /*逻辑更新
    * 1. 庄家第一轮直接打出一张
    * 2. 之后进入正常循环*/
    public void startGame() {
        if (!isStart){
            initializeGame();
            preparationUI.initializeUI();
            return;
        }
        currentPlayer = findZhuang();
        // 地主额外获得一张牌
        currentPlayer.drawTile(tileStack);
        gameUI.initializeUI();
    }


    public Player getNextPlayer(Player player){
        for (Player p : players) {
            if (p.getLocation() == player.getLocation().next()){
                return p;
            }
        }
        return null;
    }

    public Player getLastPlayer(Player player){
        for (Player p : players) {
            if (player.getLocation() == p.getLocation().next()){
                return p;
            }
        }
        return null;
    }

    public void moveToNext(Player player){
        currentPlayer = getNextPlayer(player);
    }

    public Player findZhuang(){
        for (Player player : players) {
            if (player.isZhuang()){
                return player;
            }
        }
        return null;
    }

    /**
     * 正常回合
     * 1.玩家摸牌
     * 2.玩家丢出一张牌
     * 3.检测其他玩家是否可操作
     *
     * @param player
     */
    /*public void takeTurn(Player player, boolean isFirstTurn) {
        System.out.println(player.getLocation() + " 's turn");
        System.out.println("You current tiles: ");  // 打印玩家手牌
        player.getHand().showHandTiles();
        System.out.println();

        if(!isFirstTurn) {
            player.drawTile(tileStack);  // 玩家摸牌
        }

        if (player.isWinner()){
            return;
        }

        boolean isDiscard = false;
        while (!isDiscard) {
            if(!isFirstTurn) {
                System.out.println("You current tiles after catch: ");  // 打印摸牌后的手牌
                player.getHand().showHandTiles();
            }

            System.out.println("Please discard one tile.");  // 提示玩家应该弃牌
            // TODO 癞子牌操作

            if (playerDiscard(player)) {  // 玩家选择要丢弃的牌

                List<Tile> discardPile = tileStack.getDiscardTiles();   // 打印弃牌堆
                System.out.println("Discard Piles : \n");

                for (Tile tile : discardPile) {
                    System.out.print(tile + "\t");
                }
                System.out.println();

                for (Player p : players) {  // 检测玩家操作
                    if (p != player) {
                        boolean canEat = false;
                        if (p.getLocation() == player.getLocation().next()){
                            canEat = true;
                        }
                        playerOperations(p, tileStack.getDiscardTiles().get(tileStack.getDiscardTiles().size() - 1), canEat);
                    }
                }
                isDiscard = true;
            }
        }
    }*/



    public void playerDiscardTile(Player player, Tile tile){
        if (player.discardTile(tileStack, tile)){

        }
    }

    public void playerDrawTile(Player player){
        player.drawTile(tileStack);
        checkIsWin(player);
    }


    public void updateGame(){
        moveToNext(currentPlayer);
        currentPlayer.drawTile(tileStack);
        if (currentPlayer.isWinner()){
            hasWinner = true;
        }
    }

    public TileStack getTileStack() {
        return tileStack;
    }

    public boolean checkIsWin(Player player){
        return player.isWinner();
    }

    public boolean isHasWinner() {
        return hasWinner;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setStart(){
        isStart = true;
    }

    public void setZhuang(int index){
        players.get(index).setZhuang(true);
    }

    public  Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Tile> getPlayerTiles(Player player){
        return player.getTiles();
    }

    public List<Tile> getPlayerMeldTiles(Player player){
        return player.getMeldTiles();
    }

    public List<Tile> getDiscardTiles(){
        return tileStack.getDiscardTiles();
    }

    public boolean concealedKongTile(Player player){
        return player.concealedKongTile();
    }

    public boolean checkPung(Player player, Tile tile){
        return player.checkPung(tile);
    }

    public boolean checkGang(Player player, Tile tile){
        return player.checkGang(tile);
    }

    public void playerPungTile(Player player, Tile tile){
        player.pengTile(tile);
        tileStack.getDiscardTiles().remove(tile);
    }
}
