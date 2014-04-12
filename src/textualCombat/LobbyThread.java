package textualCombat;

import java.net.Socket;
import java.util.ArrayList;

public class LobbyThread extends Thread {
    private ArrayList<LobbyClientThread> clients;
    private ArrayList<LobbyClientThread> matchMakingQueue;
    
    public LobbyThread() {
        clients = new ArrayList();
        matchMakingQueue = new ArrayList(2);
    }
    
    @Override
    public void run() {
        super.run();
    }
    
    public void clientJoiningLobby(Socket client){
        clients.add(new LobbyClientThread(Thread.currentThread(), client));
    }
    
    public void clientForMatchMaking(LobbyClientThread client) {
        //add given thread to match making
        
        //check if there are 2 people in match making
            //if yes, spawn instance and remove clients from clients<> and queue<>
            //if no, do nothing
    }
}
