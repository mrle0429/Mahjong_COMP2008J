package Controller;

import Model.Player;
import Model.PlayerType;
import Model.Tile;
import Model.TileStack;
import Util.WaysOfHu;
import View.GameUI;
import View.PreparationUI;

import java.util.ArrayList;
import java.util.List;

/**
 * The Game class represents the main controller of the game.
 * It manages the game state, player actions, and game UI.
 *
 * This class maintains a list of players, the tile stack, and the current player.
 * It also keeps track of the game state through boolean flags such as isStart, isGameOver, and hasWinner.
 *
 * The Game class provides methods for game initialization, starting the game, player actions (drawing and discarding tiles),
 * moving to the next player, checking win conditions, and updating the game state and score.
 *
 * This class interacts with the Player class for player actions and the TileStack class for tile management.
 * It also interacts with the GameUI and PreparationUI classes for updating the user interface.
 */
public class Game {
    private final List<Player> players;
    private final TileStack tileStack;
    private Player currentPlayer;

    private final GameUI gameUI;
    private final PreparationUI preparationUI;

    private boolean isStart;
    private boolean isGameOver;
    private boolean hasWinner;

    private static final int INITIAL_TILE_NUM = 13;


    public Game() {
        players = new ArrayList<>();
        tileStack = new TileStack();

        gameUI = new GameUI(this);
        preparationUI = new PreparationUI(this);

        hasWinner = false;
        isStart = false;
        isGameOver = false;
    }




    public void startGame() {
        if (!isStart) {
            addPlayer();
            preparationUI.initializeUI();
            return;
        }

        distributeTile();
        currentPlayer = findBanker();
        gameUI.initializeUI();

        // Banker draws a tile
        playerDrawTile(currentPlayer);

    }

    private void addPlayer() {
        players.add(new Player(PlayerType.East));
        players.add(new Player(PlayerType.South));
        players.add(new Player(PlayerType.West));
        players.add(new Player(PlayerType.North));
    }

    private void distributeTile() {
        for (Player player : players) {
            for (int i = 0; i != INITIAL_TILE_NUM; i++) {
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

    public Player findBanker() {
        for (Player player : players) {
            if (player.isBanker()) {
                return player;
            }
        }
        return null;
    }


    public void updateScore() {
        if (!hasWinner) {
            return;
        }

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


    public boolean checkIsWin(Player player) {
        if (player.isWinner()) {
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

    public void setBanker(int index) {
        players.get(index).setBanker(true);
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

    public boolean checkChow(Player player, Tile tile) {
        return player.checkChow(tile);
    }

    public boolean checkPung(Player player, Tile tile) {
        return player.checkPung(tile);
    }

    public boolean checkKong(Player player, Tile tile) {
        return player.checkKong(tile);
    }

    public void playerPungTile(Player player, Tile tile) {
        player.pungTile(tile);
        tileStack.getDiscardTiles().remove(tile);
    }

    public void playerKongTile(Player player, Tile tile) {
        player.kongTile(tile);
        tileStack.getDiscardTiles().remove(tile);
    }

    public void playerChowTile(Player player, Tile tile) {
        player.chowTile(tile);
        tileStack.getDiscardTiles().remove(tile);
    }

    public void playerConcealedKongTile(Player player) {
        player.concealedKongTile();
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
