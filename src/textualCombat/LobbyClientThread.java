package textualCombat;

import java.net.Socket;

public class LobbyClientThread extends ClientThread{
    
    LobbyThread parent;
    
    public LobbyClientThread(LobbyThread parent, Socket client) {
        this.parent = parent;
        this.client = new SocketBundle(client);
    }
    
    public LobbyClientThread(LobbyThread parent, SocketBundle client) {
        this.parent = parent;
        this.client = client;
    }

    @Override
    public void run() {
        String input = null;
        
        while(true) {
            input = client.read();
            
            switch(input) {
                case "queue":
                    parent.clientForMatchMaking(this);
                    break;
                
                case "exit":
                    client.close();
            }
        }
    }
}
