package textualCombat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainThread {

    static boolean running = true;
    static LobbyThread lobby;
    static ServerSocket sock;
    
    public static void main(String[] args) {
        
        if(args.length != 1) {
            System.err.println("Give me a port!");
            System.exit(1);
        }
        
        sock = null;
        Socket newClient = null;
        int port = Integer.parseInt(args[0]);
        
        try {
            sock = new ServerSocket(port);
            System.out.println("Textual Combat v0.1.turtle started on port " + port);
        } catch (IOException e) {
            System.err.println("Port taken. Is there another instance of this running");
            System.exit(1);
        }
        
        lobby = new LobbyThread();
        lobby.start();
        
        while(running) {
            try{
                lobby.clientJoiningLobby(sock.accept());
                
            } catch (IOException e) {
                System.err.println("Problem accepting new client.");
            } finally {
                newClient = null;
            }
        }
    }
    
    public static void stop() {
        //running = false;
        System.exit(0);
    }
    
    public static void stopServer() {
        try {
            sock.close();
        } catch (IOException ex) {
            Logger.getLogger(MainThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
