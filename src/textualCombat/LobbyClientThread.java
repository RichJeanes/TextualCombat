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
                switch(input.toLowerCase()) {                 
                    case "queue":
                        parent.clientForMatchMaking(this);
                        System.out.println(client + " has requested to be queued.");
                        break;

                    case "help":
                        client.write(printHelp());
                        break;
                        
                    case "exit":
                        client.close();
                        break;
                        
                    default:
                        client.write("Invalid command: " + input);
                        client.write("Type 'help' for a list of valid commands.");
                        
                }
            } catch(NullPointerException npe) {
                System.out.println("Connection terminated");
                alive = false;
            }
        }
    }
    
    public String printHelp() {
        return "\nAvailable commands:\n" +
               "queue: Places you in the match making queue to find an opponent\n" +
               "help:  Print this help statement\n" +
               "exit:  Disconnect from Textual Combat server\n";
    }
}
