package Network;

import Model.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the main class of the server. It manages each player's request,
 * makes judgments, and sends it back to each player.
 */

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
                // Wait for the last player to load successfully
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Tell each client that everyone is here and ready to start the game
            for (PlayerThread playerThread : playerThreads) {
                playerThread.sendMessage(MessageType.EnoughPlayers);
            }

            //Initialize game information
            players = new ArrayList<>();
            tileStack = new TileStack();
            optionPlayers = new ArrayList<>();
            hasWinner = false;

            initializeGame();

            // Send player information to the client in order
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
        // A random player is the dealer
        players.get(((int) (Math.random() * 4))).setBanker(true);

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
                player.getHand().addTile(tileStack.drawTile());
            }
        }
    }

    public Player findZhuang() {
        for (Player player : players) {
            if (player.isBanker()) {
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

        checkAndAddOptionPlayers(tile);

        updateGame(player, tile);

        waitForFinish();
    }

    public void checkAndAddOptionPlayers(Tile tile) {
        Player currentTestPlayer = currentPlayer;

        for (int i = 0; i != 3; i++) {
            currentTestPlayer = getNextPlayer(currentTestPlayer);

            boolean canPeng = currentTestPlayer.getHand().canPung(tile);
            boolean canGang = currentTestPlayer.getHand().canKong(tile);
            boolean canEat = (currentTestPlayer.getLocation() == currentPlayer.getLocation().next()) && currentTestPlayer.getHand().canChow(tile); // 只有下家能吃
            if (canPeng || canGang || canEat) {
                optionPlayers.add(currentTestPlayer);
            }
        }
    }

    public void playerKung(Player player) {
        List<Tile> optionTiles = player.getHand().operation(MeldType.CONCEALEDKONG, null);

        if (checkEnoughTiles()) {
            return;
        }

        newTile = tileStack.drawTile();
        player.getHand().addTile(newTile);

        updatePlayer(player);

        Message message = new Message();
        message.setType(MessageType.PlayerKung);
        message.setPlayer(player);
        message.setTileCount(player.getTilesCount());
        message.setOptionTiles(optionTiles);
        message.setTile(newTile);

        sendMessageToAll(message);

        waitForFinish();

        checkIsHasWinner(player);
    }

    public void playerOptionPass(Player player, Tile tile) {
        updateGame(currentPlayer, tile);
    }

    public void playerOptionPung(Player player, Tile tile) {
        tileStack.getDiscardTiles().remove(tile);
        optionPlayers.clear();

        List<Tile> optionTiles = player.getHand().operation(MeldType.PUNG, tile);

        updatePlayer(player);

        currentPlayer = player;
        Message message = new Message();
        message.setType(MessageType.OptionWithPung);
        message.setTile(tile);
        message.setOptionTiles(optionTiles);
        message.setPlayer(currentPlayer);
        message.setTileCount(currentPlayer.getTilesCount());

        sendMessageToAll(message);
        waitForFinish();

        checkIsHasWinner(player);
    }

    public void playerOptionKong(Player player, Tile tile) {
        tileStack.getDiscardTiles().remove(tile);
        optionPlayers.clear();

        List<Tile> optionTiles = player.getHand().operation(MeldType.KONG, tile);
        if (checkEnoughTiles()) {
            return;
        }
        newTile = tileStack.drawTile();
        player.getHand().addTile(newTile);

        updatePlayer(player);

        currentPlayer = player;
        Message message = new Message();
        message.setType(MessageType.OptionWithKong);
        message.setNewTile(newTile);
        message.setOptionTiles(optionTiles);
        message.setTile(tile);
        message.setPlayer(currentPlayer);
        message.setTileCount(currentPlayer.getTilesCount());

        sendMessageToAll(message);
        waitForFinish();

        checkIsHasWinner(player);
    }

    public void playerOptionChow(Player player, Tile tile) {
        tileStack.getDiscardTiles().remove(tile);
        optionPlayers.clear();

        List<Tile> optionTiles = player.getHand().operation(MeldType.CHOW, tile);

        updatePlayer(player);

        currentPlayer = player;
        Message message = new Message();
        message.setType(MessageType.OptionWithChow);
        message.setTile(tile);
        message.setOptionTiles(optionTiles);
        message.setPlayer(currentPlayer);
        message.setTileCount(currentPlayer.getTilesCount());

        sendMessageToAll(message);
        waitForFinish();

        checkIsHasWinner(player);
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
                return;
            }
        }
    }

    public void updateGame(Player player, Tile tile) {
        if (!optionPlayers.isEmpty()) {
            // Set the player who played this card, so it is easy to detect the eating operation
            Player originalPlayer = currentPlayer;
            Player optionsPlayer = optionPlayers.get(0);

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
        newTile = tileStack.drawTile();
        currentPlayer.getHand().addTile(newTile);

        Message msg = new Message();
        msg.setType(MessageType.TakeTurn);
        msg.setPlayer(currentPlayer);
        msg.setOriginalPlayer(player);
        msg.setTile(tile);
        msg.setNewTile(newTile);

        checkIsHasWinner(currentPlayer);

        sendMessageToAll(msg);
    }

    public void checkIsHasWinner(Player player) {
        if (player.isWinner() || player.getHand().checkIsWin()) {
            Message message = new Message();
            message.setType(MessageType.HasWinner);
            message.setPlayer(player);
            sendMessageToAll(message);
            waitForFinish();
        }
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
