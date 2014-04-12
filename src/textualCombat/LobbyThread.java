package textualCombat;

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
        
    }
    
    public void clientJoiningLobby(){
        
    }
    
    public void clientForMatchMaking() {
        
    }
}
