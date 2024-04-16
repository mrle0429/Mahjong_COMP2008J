package Controller;

import Model.Player;
import Model.Tile;
import Model.TileStack;
import Util.CheckTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private List<Player> players;
    private TileStack tileStack;
    private boolean hasWinner;
    private Scanner scanner;

    public Game(){
        players = new ArrayList<>();
        tileStack = new TileStack();
        scanner = new Scanner(System.in);
        hasWinner = false;
    }

    public void initializeGame(){
        //TODO CheckPlayer class to replace this.
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));
        players.add(new Player("Player 3"));
        players.add(new Player("Player 4"));

        for (Player player : players) {
            for (int i = 0; i != 13; i++) {
                ;player.getHand().addTile(tileStack.takeTile());
            }
        }
        //TODO TouZi to solve who is 庄家, 这里假设第一名是庄家
//        players.get(0).getHand().addTile(tileStack.takeTile());
    }

    public void startGame(){
        System.out.println("Game start!");
        initializeGame();
        int currentPlayerIndex = 0;

        while (!hasWinner){
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println(currentPlayer.getName() + " 's turn");
            takeTurn(currentPlayer);
            if (tileStack.isEmpty() || currentPlayer.isWinner()){
                hasWinner = true;
            }else{
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
        }
    }

    public void takeTurn(Player player){
        System.out.println("You current tiles: ");
        player.getHand().showHandTiles();
        playerCatchTile(player);
        boolean isDiscard = false;
        while (!isDiscard) {
            System.out.println();

            System.out.println("You current tiles: ");
            player.getHand().showHandTiles();

            System.out.println("Please choice options\n" +
                    "1. For replace tile (only for flowers and seasons)\n" +
                    "2. For discard\n" +
                    "3. For operations(GANG, Win, etc.)");
            String option = scanner.next();
            switch (option) {
                case "1":
                    scanner.nextLine(); // Clear scanner
                    playerReplaceTile(player);
                    break;
                case "2":
                    scanner.nextLine(); // Clear scanner
                    if (playerDiscard(player)){
                        isDiscard = true;
                    }
                    break;
                case "3":
                    scanner.nextLine(); // Clear scanner
                    playerOperations(player);
                    break;
                default:
                    System.out.println("Please enter correct option");
                    takeTurn(player);
            }
        }
    }

    public void playerCatchTile(Player player){
        System.out.println("Please choice options\n" +
                "1. For catch tile already in stack\n" +
                "2. For directly catch tile");
        String option = scanner.next();
        switch (option){
            case "1":
                if (!player.catchTileFromTable(tileStack)){
                    System.out.println("You can not catch it, because you can not form GANG or Win ...");
                    player.catchTileFromTable(tileStack);
                    System.out.println("System already help you catch a new tile in stack");
                }
                break;
            case "2":
                player.catchTileFromStack(tileStack);
                break;
            default:
                System.out.println("Please enter correct option");
                playerCatchTile(player);
        }
        scanner.nextLine(); // Clear scanner
    }

    public void playerReplaceTile(Player player){
        System.out.println("Enter flower tile or season tile name: ");
        // TODO score part in here
        String tileName = scanner.next();
        Tile tile = CheckTile.findTile(tileName);
        if (tile != null){
            if (player.getHand().removeTile(tile)){
                player.catchTileFromStack(tileStack);
            }else{
                System.out.println("You have not this tile");
            }
        }
    }

    public boolean playerDiscard(Player player){
        System.out.println("Enter the name of tile you want to discard");
        String tileName = scanner.next();
        if (player.discard(tileStack, tileName)){
            System.out.println(player.getName() + " discard: "+ tileName);
            return true;
        };
        return false;
    }

    public void playerOperations(Player player){
        System.out.println("Please choice options\n" +
                "1. Peng\n" +
                "2. Gang");
        String option = scanner.next();
        switch (option){
            case "1":
                if (player.getHand().canPeng()){
                    System.out.println(player.getName() + " Peng tiles");
                }else{
                    System.out.println("You can not peng with your tiles");
                }
                break;
            case "2":
                if (player.getHand().canGang()){
                    System.out.println(player.getName() + "Gang tiles");
                }else{
                    System.out.println("You can not Gang with your tiles");
                }
                break;
            default:
                System.out.println("Please enter correct number");
        }
    }
}
