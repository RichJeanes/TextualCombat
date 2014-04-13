package textualCombat;

import java.net.Socket;
import java.util.ArrayList;

public class LobbyThread extends Thread {
    private ArrayList<LobbyClientThread> clients;
    private LobbyClientThread matchMakingQueue;
    
    
    public LobbyThread() {
        clients = new ArrayList();
        matchMakingQueue = null;
    }
    
    @Override
    public void run() {}
    
    public void clientJoiningLobby(Socket client){
        LobbyClientThread lct = new LobbyClientThread(this, client);
        lct.start();
        clients.add(lct);
        lct.write("Welcome to the lobby, " + lct);
        
        int lobbyists = clients.size();
        if(matchMakingQueue != null) lobbyists++;
        System.out.println("Number of players in lobby: " + lobbyists);
        lct.write("Number of players in lobby: " + lobbyists);
    }
    
    public void clientJoiningLobby(SocketBundle client){
        LobbyClientThread lct = new LobbyClientThread(this, client);
        lct.start();
        clients.add(lct);
        lct.write("Welcome to the lobby, " + lct);
        
        int lobbyists = clients.size();
        if(matchMakingQueue != null) lobbyists++;
        System.out.println("Number of players in lobby: " + lobbyists);
        client.write("Number of players in lobby: " + lobbyists);
    }
    
    public void clientForMatchMaking(LobbyClientThread client) {
        if(matchMakingQueue == null) {
            matchMakingQueue = client;
            clients.remove(client);
            System.out.println(client + " queued up for match making");
        } else {
            System.out.println("here");
            InstanceThread it = new InstanceThread(matchMakingQueue.getSocketBundle(), client.getSocketBundle());
            it.start();
            
            matchMakingQueue = null;
            clients.remove(client);
            System.out.println(matchMakingQueue + " and " + client + " have entered a match.");
        }
    }
}
