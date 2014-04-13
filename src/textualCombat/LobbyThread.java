package textualCombat;

import java.net.Socket;
import java.util.LinkedList;

public class LobbyThread extends Thread {

    private LinkedList<LobbyClientThread> clients;
    private LobbyClientThread matchMakingQueue;

    public LobbyThread() {
        clients = new LinkedList<LobbyClientThread>();
        matchMakingQueue = null;
    }

    @Override
    public void run() {
    }

    public void clientJoiningLobby(Socket client) {
        LobbyClientThread lct = new LobbyClientThread(this, client);
        lct.start();
        clients.add(lct);
    }

    public void clientJoiningLobby(SocketBundle client) {
        LobbyClientThread lct = new LobbyClientThread(this, client);
        lct.start();
        clients.add(lct);
    }

    public void clientForMatchMaking(LobbyClientThread client) {
        if (matchMakingQueue == null) {
            matchMakingQueue = client;
            clients.remove(client);
            System.out.println(client + " queued up for match making");
        } else {
            InstanceThread it = new InstanceThread(matchMakingQueue.getSocketBundle(), client.getSocketBundle());
            it.start();

            matchMakingQueue = null;
            clients.remove(client);
            
            System.out.println(matchMakingQueue + " and " + client + " have entered a match.");
        }
    }
    
    public int clientsInLobby() {
        int lobbyists = clients.size();
        if (matchMakingQueue == null) {
            return lobbyists;
        }
        return lobbyists++;
    }
    
    public void removeClient(LobbyClientThread client) {
        clients.remove(client);
    }
}
