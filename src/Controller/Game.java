package Controller;

import Model.*;
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
        players.get(0).setZhuang(true);  // 确定庄家
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
        System.out.println();

        int currentPlayerIndex = 0;
        takeTurn(players.get(currentPlayerIndex), true);
        currentPlayerIndex++;

        while (!hasWinner) {
            Player currentPlayer = players.get(currentPlayerIndex);
            takeTurn(currentPlayer, false);

            if (tileStack.isEmpty() || currentPlayer.isWinner()) {
                hasWinner = true;
            } else {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }

        }
    }

    public void showAllTiles() {
        for (Player player : players) {
            System.out.println(player.getLocation() + " 's tiles: ");
            player.getHand().showHandTiles();
        }
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
                        playerOperations(p, tileStack.getDiscardTiles().get(tileStack.getDiscardTiles().size() - 1));
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
            return true;
        }
        return false;
    }

    // 自动检测玩家是否有碰或杠
    // 询问玩家是否要进行操作
    public void playerOperations(Player player, Tile operationCard) {
        Boolean isPeng = player.getHand().canPeng(operationCard);
        Boolean isGang = player.getHand().canGang(operationCard);

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
                    return;
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
                    playerDiscard(player);
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
