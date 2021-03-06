package textualCombat;

import java.net.Socket;

public class LobbyClientThread extends ClientThread {

    LobbyThread parent;
    Socket tempSocket;

    public LobbyClientThread(LobbyThread parent, Socket client) {
        this.parent = parent;
        this.tempSocket = client;
    }

    public LobbyClientThread(LobbyThread parent, SocketBundle client) {
        this.parent = parent;
        this.client = client;
    }

    @Override
    public void run() {
        String input = null;
        boolean alive = true;

        if(client == null) {
            client = new SocketBundle(tempSocket);
            client.write("Welcome to the lobby, " + client);
        } else {
            client.write("\r\nWelcome back to the lobby, " + client);
        }
        
        int clientCount = MainThread.lobby.clientsInLobby() ;
        if(clientCount == 1) {
            client.write("You are all alone.");
        } else {
            client.write("There are " + (clientCount - 1) + " players with you."); //-1 for you
        }
        
        client.write(client + "'s stats:\r\n" 
        		   + client.getPlayerInfo().getWins() + " wins\r\n"
        		   + client.getPlayerInfo().getHealth() + " health\r\n"
        		   + client.getPlayerInfo().getStr() + " strength\r\n"
        		   + client.getPlayerInfo().getDef() + " defense\r\n"
        		   + client.getPlayerInfo().getAgl() + " agility\r\n"
        		   + client.getPlayerInfo().getDmg() + " damage\r\n");
        
        while (alive) {
            input = client.read();
            System.out.println(client + " sent command " + input);

            try {
                switch (input.toLowerCase().trim()) {
                    case "queue":
                        parent.clientForMatchMaking(this);
                        return;
                        
                    case "help":
                        client.write(printHelp());
                        break;

                    case "exit":
                        client.close();
                        parent.removeClient(this);
                        return;

                    case "whatdo":
                        client.write(printWhatDo());
                        break;

                    case "stopservernowplz":
                        MainThread.stopServer();
                        break;

                    default:
                        client.write("Invalid command: " + input);
                        client.write("Type 'help' for a list of valid commands.");

                }
            } catch (NullPointerException npe) {
                System.out.println("Connection terminated");
                alive = false;
            }

        }
    }
    
    public String printHelp() {
        return "\r\nAvailable lobby commands:\r\n"
                + "queue: Places you in the match making queue to find an opponent\r\n"
                + "help:  Print this help statement\r\n"
                + "exit:  Disconnect from Textual Combat server\r\n";
    }

    public String printWhatDo() {
        return "";
    }
}
