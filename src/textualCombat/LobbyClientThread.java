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
        boolean alive = true;
        
        while(alive) {
            input = client.read();
            System.out.println(client + " sent command " + input);
            
            try {
                switch(input) {                 
                    case "queue":
                        parent.clientForMatchMaking(this);
                        System.out.println(client + " has requested to be queued.");
                        break;

                    case "exit":
                        client.close();
                }
            } catch(NullPointerException npe) {
                System.out.println("Connection terminated");
                alive = false;
            }
        }
    }
}
