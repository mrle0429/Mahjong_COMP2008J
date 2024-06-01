package Network;

import Model.Message;
import Model.MessageType;
import Model.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerThread extends Thread{
    private String name;
    private final GameServer server;
    private final Socket client;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final boolean gameOver;

    public PlayerThread(GameServer server, int index, Socket client) {
        this.server = server;
        name = "Player " + index;
        this.client = client;
        gameOver = false;
    }

    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(client.getOutputStream());
            ois = new ObjectInputStream(client.getInputStream());
            oos.writeObject(MessageType.Waiting);
            oos.flush();

            while (!gameOver){
                Message msg = (Message) ois.readObject();
                if (msg.getType() == MessageType.DiscardTile){
                    server.playerDiscardTile(msg.getPlayer(), msg.getTile());
                } else if (msg.getType() == MessageType.PlayerKung) {
                    server.playerKung(msg.getPlayer());
                } else if (msg.getType() == MessageType.OptionWithPung) {
                    server.playerOptionPung(msg.getPlayer(), msg.getTile());
                } else if (msg.getType() == MessageType.OptionWithPass) {
                    server.playerOptionPass(msg.getPlayer(), msg.getTile());
                } else if (msg.getType() == MessageType.OptionWithKong) {
                    server.playerOptionKong(msg.getPlayer(), msg.getTile());
                } else if (msg.getType() == MessageType.OptionWithChow) {
                    server.playerOptionChow(msg.getPlayer(), msg.getTile());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(MessageType messageType){
        try {
            oos.writeObject(messageType);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Player player){
        try {
            oos.writeObject(player);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTurnMessage(Message msg){
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setThreadName(String name) {
        this.name = name;
    }

    public String getThreadName(){
        return this.name;
    }
}
