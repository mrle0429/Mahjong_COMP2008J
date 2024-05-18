package Controller;

import Model.*;
import Util.CheckTile;
import View.GameUI;
import View.PreparationUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private List<Player> players;
    private TileStack tileStack;
    private boolean hasWinner;
    private Player currentPlayer;
    private Scanner scanner;
    private GameUI gameUI;
    private PreparationUI preparationUI;
    private boolean isStart;

    public Game() {
        players = new ArrayList<>();
        tileStack = new TileStack();
        scanner = new Scanner(System.in);
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
                player.getHand().addTile(tileStack.takeTile());
            }
//            if (player.isZhuang()) {
//                player.getHand().addTile(tileStack.takeTile());
//            }
        }
    }
    /*逻辑更新
    * 1. 庄家第一轮直接打出一张
    * 2. 之后进入正常循环*/
    public void startGame() {
//        System.out.println("Game start!");
        if (!isStart){
            initializeGame();
            preparationUI.initializeUI();
            return;
        }
        currentPlayer = findZhuang();
        // 地主额外获得一张牌
        currentPlayer.drawTile(tileStack);
        gameUI.initializeUI();
//
//        showAllTiles();    // 打印所有玩家的牌
//        System.out.println();
//
//        currentPlayer = findZhuang();
//        takeTurn(currentPlayer, true);
//
//        while (!hasWinner) {
//            takeTurn(currentPlayer, false);
//
//            if (tileStack.isEmpty() || currentPlayer.isWinner()) {
//                hasWinner = true;
//            }
//        }
    }

    public void showAllTiles() {
        for (Player player : players) {
            System.out.println(player.getLocation() + " 's tiles: ");
            player.getHand().showHandTiles();
        }
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
    public void takeTurn(Player player, boolean isFirstTurn) {
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
    }


    // 玩家弃牌
    public boolean playerDiscard(Player player) {
        System.out.println("Enter the name of tile you want to discard");
        String tileName = scanner.next();
        if (player.discardTile(tileStack, tileName)) {
            //System.out.println(player.getName() + " discard: "+ tileName);
            moveToNext(player);
            return true;
        }
        return false;
    }

    public void playerDiscardTile(Player player, Tile tile){
        if (player.discardTile(tileStack, tile)){
//            moveToNext(player);
//            return true;
        }
//        return false;
    }

    // 自动检测玩家是否有碰或杠
    // 询问玩家是否要进行操作
    public void playerOperations(Player player, Tile operationCard, boolean canEat) {
        Boolean isGang = player.getHand().canGang(operationCard);
        Boolean isPeng = player.getHand().canPeng(operationCard);
        boolean isEat = false;
        if (canEat){
            isEat = player.getHand().canEat(operationCard);
        }

        if (isPeng && isGang) {
            System.out.println(player.getLocation() + " 's tiles: ");
            player.getHand().showHandTiles();
            System.out.println(player.getLocation() + " can Peng or Gang tiles. Please choice options\n" +
                    "1. Peng\n" +
                    "2. Gang\n" +
                    "3. Not Peng or Gang");
            String option = scanner.next();
            switch (option) {
                case "1":
                    scanner.nextLine(); // Clear scanner
                    player.getHand().operation(MeldType.PENG, operationCard);
                    player.getHand().showHandTiles();
                    tileStack.getDiscardTiles().remove(tileStack.getDiscardTiles().size() - 1);  // 弃牌堆删除被碰的这张牌
                    playerDiscard(player);
                    break;
                case "2":
                    scanner.nextLine(); // Clear scanner
                    player.getHand().operation(MeldType.GANG, operationCard);
                    player.getHand().showHandTiles();
                    tileStack.getDiscardTiles().remove(tileStack.getDiscardTiles().size() - 1);  // 弃牌堆删除被杠的这张牌
                    player.getHand().addTile(tileStack.takeTile()); // 为了保证牌的总量 杠完需要额外获得牌
                    playerDiscard(player);
                    break;
                case "3":
                    scanner.nextLine(); // Clear scanner
                    break;
            }

        } else if (isPeng) {
            System.out.println(player.getLocation() + " 's tiles: ");
            player.getHand().showHandTiles();
            System.out.println(player.getLocation() + " can Peng tiles. Please choice options\n" +
                    "1. Peng\n" +
                    "2. Not Peng");
            String option = scanner.next();

            switch (option) {
                case "1":
                    scanner.nextLine(); // Clear scanner
                    player.getHand().operation(MeldType.PENG, operationCard);
                    player.getHand().showHandTiles();
                    tileStack.getDiscardTiles().remove(tileStack.getDiscardTiles().size() - 1);  // 弃牌堆删除被碰的这张牌
                    playerDiscard(player);
                    break;
                case "2":
                    scanner.nextLine(); // Clear scanner
                    break;
                default:
                    System.out.println("Please enter correct number");
            }
        } else if (isGang) {
            System.out.println(player.getLocation() + " 's tiles: ");
            player.getHand().showHandTiles();
            System.out.println(player.getLocation()+ " can Gang tiles. Please choice options\n" +
                    "1. Gang\n" +
                    "2. Not Gang");
            String option = scanner.next();
            switch (option) {
                case "1":
                    scanner.nextLine(); // Clear scanner
                    player.getHand().operation(MeldType.GANG, operationCard);
                    player.getHand().showHandTiles();
                    tileStack.getDiscardTiles().remove(tileStack.getDiscardTiles().size() - 1);  // 弃牌堆删除被杠的这张牌
                    player.getHand().addTile(tileStack.takeTile()); // 为了保证牌的总量 杠完需要额外获得牌
                    playerDiscard(player);
                    break;
                case "2":
                    scanner.nextLine(); // Clear scanner
                    break;
                default:
                    System.out.println("Please enter correct number");
            }
        }
        if (isEat) {
            System.out.println(player.getLocation() + " 's tiles: ");
            player.getHand().showHandTiles();
            System.out.println(player.getLocation()+ " can Eat tiles. Please choice options\n" +
                    "1. Eat\n" +
                    "2. Not Eat");
            String option = scanner.next();
            switch (option){
                case "1":
                    scanner.nextLine();
                    player.getHand().operation(MeldType.EAT, operationCard);
                    player.getHand().showHandTiles();
                    tileStack.getDiscardTiles().remove(tileStack.getDiscardTiles().size() - 1);  // 弃牌堆删除被杠的这张牌
                    playerDiscard(player);
                    break;
                case "2":
                    scanner.nextLine();
                    break;
                default:
                    System.out.println("Please enter correct number");
            }
        }
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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
