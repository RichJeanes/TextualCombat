package textualCombat;

import java.net.Socket;
import java.util.ArrayList;

public class LobbyThread extends Thread {
    private ArrayList<LobbyClientThread> clients;
    private LobbyClientThread matchMakingQueue;
    
    private boolean alive = true;
    
    public LobbyThread() {
        clients = new ArrayList();
        matchMakingQueue = null;
    }
    
    @Override
    public void run() {
        while(alive);
        return;
    }
    
    public void clientJoiningLobby(Socket client){
        LobbyClientThread lct = new LobbyClientThread(this, client);
        lct.start();
        clients.add(lct);
        lct.write("Welcome to the lobby, " + lct);
        
        System.out.println("Number of players in lobby: " + clients.size());
    }
    
    public void clientForMatchMaking(LobbyClientThread client) {
        if(matchMakingQueue == null) {
            matchMakingQueue = client;
        } else {
            new InstanceThread(matchMakingQueue, client).start();
            matchMakingQueue = null;
        }
    }
    
    public void kill() {
        alive = false;
    }
}
