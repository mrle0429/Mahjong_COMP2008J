package Controller;

import Model.MeldType;
import Model.Player;
import Model.Tile;
import Model.TileStack;
import Util.CheckPlayer;
import Util.CheckTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private List<Player> players;
    private TileStack tileStack;
    private boolean hasWinner;
    private Scanner scanner;

    public Game() {
        players = new ArrayList<>();
        tileStack = new TileStack();
        scanner = new Scanner(System.in);
        hasWinner = false;
    }

    public void initializeGame() {    // 重构初始化函数
        addPlayer();                 // 添加四名玩家
        CheckPlayer.isZhuang(players.get(0));  // 确定庄家
        distributeTile();            // 发牌，同时为庄家多发一张
    }

    public void addPlayer() {
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));
        players.add(new Player("Player 3"));
        players.add(new Player("Player 4"));
    }

    public void distributeTile() {
        for (Player player : players) {
            for (int i = 0; i != 13; i++) {
                player.getHand().addTile(tileStack.takeTile());
            }
            if (player.isZhuang()) {
                player.getHand().addTile(tileStack.takeTile());
            }
        }
    }
    /*逻辑更新
    * 1. 庄家第一轮直接打出一张
    * 2. 之后进入正常循环*/
    public void startGame() {
        System.out.println("Game start!");
        initializeGame();

        showAllTiles();    // 打印所有玩家的牌

        int currentPlayerIndex = 0;
        firstTurn(players.get(currentPlayerIndex));
        currentPlayerIndex++;

        while (!hasWinner) {
            Player currentPlayer = players.get(currentPlayerIndex);
            takeTurn(currentPlayer);

            if (tileStack.isEmpty() || currentPlayer.isWinner()) {
                hasWinner = true;
            } else {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }

        }
    }

    public void showAllTiles() {
        for (Player player : players) {
            System.out.println(player.getName() + " 's tiles: ");
            player.getHand().showHandTiles();
        }
    }

    /*
     * 第一轮特殊，庄家直接丢出一张牌不需要摸牌
     * 丢出牌后检测其他玩家是否有操作
     * */
    public void firstTurn(Player player) {
        System.out.println(player.getName() + " 's turn");

        System.out.println("You current tiles: ");
        player.getHand().showHandTiles();

        System.out.println("First turn, you are Zhuang. Please choice options\n" +   // 提示玩家应该弃牌
                "Please discard one tile");

        boolean isDiscard = false;
        while (!isDiscard) {
            if (playerDiscard(player)) {     // 玩家选择要丢弃的牌

                List<Tile> discardPile = tileStack.getDiscardTiles();  // 打印弃牌堆
                for (Tile tile : discardPile) {
                    System.out.println("Discard Piles: \n");
                    System.out.print(tile + "\t");
                }
                System.out.println();

                for (Player p : players) {      // 其他玩家操作检测。 检测是否有碰或杠
                    if (p != player) {
                        playerOperations(p, tileStack.getDiscardTiles().get(tileStack.getDiscardTiles().size() - 1));
                    }
                }
                isDiscard = true;

            }
        }

    }

    /**
     * 正常回合
     * 1.玩家丢出一张牌
     * 2.检测其他玩家是否可操作
     *
     * @param player
     */
    public void takeTurn(Player player) {
        System.out.println(player.getName() + " 's turn");
        System.out.println("You current tiles: ");  // 打印玩家手牌
        player.getHand().showHandTiles();
        System.out.println();


        player.catchTileFromStack(tileStack);  // 玩家摸牌

        boolean isDiscard = false;
        while (!isDiscard) {

            System.out.println("You current tiles: ");  // 打印摸牌后的手牌
            player.getHand().showHandTiles();

            System.out.println("Please discard one tile.");  // 提示玩家应该弃牌
            // TODO 癞子牌操作

            if (playerDiscard(player)) {  // 玩家选择要丢弃的牌

                List<Tile> discardPile = tileStack.getDiscardTiles();   // 打印弃牌堆
                System.out.println("Discard Piles : \n");

                for (Tile tile : discardPile) {
                    System.out.print(tile + "\t");
                }

                for (Player p : players) {  // 检测玩家操作
                    if (p != player) {
                        playerOperations(p, tileStack.getDiscardTiles().get(tileStack.getDiscardTiles().size() - 1));
                    }
                }
                isDiscard = true;
            }
        }
    }




    public void playerReplaceTile(Player player) {
        System.out.println("Enter flower tile or season tile name: ");
        // TODO score part in here
        String tileName = scanner.next();
        Tile tile = CheckTile.findTile(tileName);
        if (tile != null) {
            if (player.getHand().removeTile(tile)) {
                player.catchTileFromStack(tileStack);
            } else {
                System.out.println("You have not this tile");
            }
        }
    }

    // 玩家弃牌
    public boolean playerDiscard(Player player) {
        System.out.println("Enter the name of tile you want to discard");
        String tileName = scanner.next();
        if (player.discard(tileStack, tileName)) {
            //System.out.println(player.getName() + " discard: "+ tileName);
            return true;
        }
        return false;
    }

    // 自动检测玩家是否有碰或杠
    // 询问玩家是否要进行操作
    public void playerOperations(Player player, Tile operationCard) {
        Boolean isPeng = player.getHand().canPeng();
        Boolean isGang = player.getHand().canGang();

        if (isPeng && isGang) {
            System.out.println(player.getName() + " 's tiles: ");
            player.getHand().showHandTiles();
            System.out.println(player.getName() + " can Peng or Gang tiles. Please choice options\n" +
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
                    break;
                case "2":
                    scanner.nextLine(); // Clear scanner
                    player.getHand().operation(MeldType.GANG, operationCard);
                    player.getHand().showHandTiles();
                    tileStack.getDiscardTiles().remove(tileStack.getDiscardTiles().size() - 1);  // 弃牌堆删除被杠的这张牌
                    break;
                case "3":
                    scanner.nextLine(); // Clear scanner
                    break;
            }

        } else if (isPeng) {
            System.out.println("You can Peng tiles. Please choice options\n" +
                    "1. Peng\n" +
                    "2. Not Peng");
            String option = scanner.next();

            switch (option) {
                case "1":
                    scanner.nextLine(); // Clear scanner
                    player.getHand().meldTiles(MeldType.PENG);
                    player.getHand().showHandTiles();
                    tileStack.getDiscardTiles().remove(tileStack.getDiscardTiles().size() - 1);  // 弃牌堆删除被碰的这张牌
                    break;
                case "2":
                    scanner.nextLine(); // Clear scanner
                    return;
                default:
                    System.out.println("Please enter correct number");
            }
        } else if (isGang) {
            System.out.println("You can Gang tiles. Please choice options\n" +
                    "1. Gang\n" +
                    "2. Not Gang");
            String option = scanner.next();
            switch (option) {
                case "1":
                    scanner.nextLine(); // Clear scanner
                    player.getHand().meldTiles(MeldType.GANG);
                    player.getHand().showHandTiles();
                    tileStack.getDiscardTiles().remove(tileStack.getDiscardTiles().size() - 1);  // 弃牌堆删除被杠的这张牌
                    return;
                case "2":
                    scanner.nextLine(); // Clear scanner
                    return;
                default:
                    System.out.println("Please enter correct number");
            }
        } else {

        }
    }
}
