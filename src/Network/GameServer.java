package Network;

import Model.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    public static void main(String[] args) {
        new GameServer().startServer();
    }

    private ServerSocket serverSocket;
    private int playerNum;
    private static List<PlayerThread> playerThreads;
    private List<Player> players;
    private List<Player> optionPlayers;
    private TileStack tileStack;
    private boolean hasWinner;
    private Player currentPlayer;
    private Tile newTile;

    public void startServer() {
        try {
            serverSocket = new ServerSocket(12345);
            playerThreads = new ArrayList<>();
            playerNum = 0;
            Socket playerClient;
            while (playerNum < 4) {
                playerClient = serverSocket.accept();
                PlayerThread playerThread = new PlayerThread(this, playerNum, playerClient);
                playerThread.start();
                playerThreads.add(playerThread);
                playerNum++;
            }

            try {
                // 等待最后一个玩家加载成功
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 告诉每个客户端，人齐了，准备开始游戏
            for (PlayerThread playerThread : playerThreads) {
                playerThread.sendMessage(MessageType.EnoughPlayers);
            }

            //初始化游戏信息
            players = new ArrayList<>();
            tileStack = new TileStack();
            optionPlayers = new ArrayList<>();
            hasWinner = false;

            initializeGame();

            // 按顺序把玩家信息发送给客户端
            for (int i = 0; i != 4; i++) {
                playerThreads.get(i).sendMessage(players.get(i));
                playerThreads.get(i).setThreadName(players.get(i).toString());
                Message message = new Message();
                message.setPlayer(currentPlayer);
                message.setType(MessageType.TurnInfo);
                playerThreads.get(i).sendTurnMessage(message);

                message = new Message();
                message.setTileStack(tileStack);
                message.setType(MessageType.TileStack);
                playerThreads.get(i).sendTurnMessage(message);
            }


//            while (!hasWinner) {
//
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initializeGame() {
        addPlayer();
        distributeTile();
        // 随机一名玩家是庄家
        players.get(((int) (Math.random() * 4))).setZhuang(true);

        currentPlayer = findZhuang();
        currentPlayer.drawTile(tileStack);
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

    public Player findZhuang() {
        for (Player player : players) {
            if (player.isZhuang()) {
                return player;
            }
        }
        return null;
    }

    public void moveToNext(Player player) {
        currentPlayer = getNextPlayer(player);
    }

    public Player getNextPlayer(Player player) {
        for (Player p : players) {
            if (p.getLocation() == player.getLocation().next()) {
                return p;
            }
        }
        return null;
    }

    public void playerDiscardTile(Player player, Tile tile) {
        player.discardTile(tileStack, tile);

        updatePlayer(player);

//        for (Player player1 : players) {
//            if (player1.toString().equals(player.toString())){
//                System.out.println(player1.getHand().getTiles().size());
//                System.out.println("更新成功");
//            }
//        }


        checkAndAddOptionPlayers(tile);

        updateGame(player, tile);

//        Message msg = new Message();
//        msg.setType(MessageType.TakeTurn);
//        msg.setPlayer(currentPlayer);
//        msg.setOriginalPlayer(player);
//        msg.setTile(tile);
//        msg.setNewTile(newTile);
//
//        sendMessageToAll(msg);

        waitForFinish();
    }

    public void checkAndAddOptionPlayers(Tile tile) {
        Player currentTestPlayer = currentPlayer;

        for (int i = 0; i != 3; i++) {
            currentTestPlayer = getNextPlayer(currentTestPlayer);

            boolean canPeng = currentTestPlayer.getHand().canPeng(tile);
            boolean canGang = currentTestPlayer.getHand().canGang(tile);
            boolean canEat = (currentTestPlayer.getLocation() == currentPlayer.getLocation().next()) && currentTestPlayer.getHand().canEat(tile); // 只有下家能吃
            if (canPeng || canGang || canEat) {
                optionPlayers.add(currentTestPlayer);
            }
        }
    }

    public void playerKung(Player player) {
        if (checkEnoughTiles()) {
            return;
        }

        newTile = tileStack.takeTile();
        player.getHand().addTile(newTile);

        updatePlayer(player);

        Message message = new Message();
        message.setType(MessageType.PlayerKung);
        message.setTile(newTile);

        findPlayerThreadByPlayer(player).sendTurnMessage(message);

        waitForFinish();

        if (checkIsHasWinner(player)) {
            return;
        }
    }

    public void playerOptionPass(Player player, Tile tile) {
        updateGame(currentPlayer, tile);
    }

    public void playerOptionPung(Player player, Tile tile) {
        tileStack.getDiscardTiles().remove(tile);
        optionPlayers.clear();

        player.getHand().operation(MeldType.PENG, tile);

        updatePlayer(player);

        if (checkIsHasWinner(player)) {
            return;
        }

        currentPlayer = player;
        Message message = new Message();
        message.setType(MessageType.OptionWithPung);
        message.setTile(tile);
        message.setPlayer(currentPlayer);

        sendMessageToAll(message);
        waitForFinish();
    }

    public void playerOptionKong(Player player, Tile tile) {
        tileStack.getDiscardTiles().remove(tile);
        optionPlayers.clear();

        player.getHand().operation(MeldType.GANG, tile);
        if (checkEnoughTiles()) {
            return;
        }
        newTile = tileStack.takeTile();
        player.getHand().addTile(newTile);

        updatePlayer(player);

        currentPlayer = player;
        Message message = new Message();
        message.setType(MessageType.OptionWithKong);
        message.setNewTile(newTile);
        message.setTile(tile);
        message.setPlayer(currentPlayer);

        sendMessageToAll(message);
        waitForFinish();

        if (checkIsHasWinner(player)) {
            return;
        }
    }

    public void playerOptionChow(Player player, Tile tile) {
        tileStack.getDiscardTiles().remove(tile);
        optionPlayers.clear();

        player.getHand().operation(MeldType.EAT, tile);

        updatePlayer(player);

        if (checkIsHasWinner(player)) {
            return;
        }

        currentPlayer = player;
        Message message = new Message();
        message.setType(MessageType.OptionWithChow);
        message.setTile(tile);
        message.setPlayer(currentPlayer);

        sendMessageToAll(message);
        waitForFinish();
    }

    public void sendMessageToAll(Message message) {
        for (PlayerThread playerThread : playerThreads) {
            playerThread.sendTurnMessage(message);
        }
    }

    public PlayerThread findPlayerThreadByPlayer(Player player) {
        for (int i = 0; i != 4; i++) {
            if (player.toString().equals(playerThreads.get(i).getThreadName())) {
                return playerThreads.get(i);
            }
        }
        return null;
    }

    public void updatePlayer(Player player) {
        for (Player p : players) {
            if (p.toString().equals(player.toString())) {
                p.setHand(player.getHand());
//                players.remove(p);
//                players.add(player);
                return;
            }
        }
    }

    public void updateGame(Player player, Tile tile) {
        if (!optionPlayers.isEmpty()) {
            // 设置出这张牌的玩家，这样方便检测吃的操作
            Player originalPlayer = currentPlayer;
            Player optionsPlayer = optionPlayers.get(0);

//            currentPlayer = optionsPlayer;
            optionPlayers.remove(optionsPlayer);

            Message message = new Message();
            message.setType(MessageType.OptionWithTile);
            message.setPlayer(optionsPlayer);
            message.setOriginalPlayer(originalPlayer);
            message.setTile(tile);
            findPlayerThreadByPlayer(optionsPlayer).sendTurnMessage(message);

            waitForFinish();
            return;
        }

        moveToNext(currentPlayer);
        if (checkEnoughTiles()) {
            return;
        }
        newTile = tileStack.takeTile();
        currentPlayer.getHand().addTile(newTile);

        if (checkIsHasWinner(currentPlayer)) {
            return;
        }

        Message msg = new Message();
        msg.setType(MessageType.TakeTurn);
        msg.setPlayer(currentPlayer);
        msg.setOriginalPlayer(player);
        msg.setTile(tile);
        msg.setNewTile(newTile);

        sendMessageToAll(msg);
    }

    public boolean checkIsHasWinner(Player player) {
        if (player.isWinner() || player.getHand().checkIsWin()) {
            Message message = new Message();
            message.setType(MessageType.HasWinner);
            message.setPlayer(player);
            sendMessageToAll(message);
            waitForFinish();

            return true;
        }
        return false;
    }

    public boolean checkEnoughTiles() {
        if (tileStack.isEmpty()) {
            Message message = new Message();
            message.setType(MessageType.NoTiles);
            sendMessageToAll(message);
            waitForFinish();

            return true;
        }
        return false;
    }

    public void waitForFinish() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
