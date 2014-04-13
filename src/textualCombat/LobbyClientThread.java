package textualCombat;

import java.net.Socket;

public class LobbyClientThread extends ClientThread {

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
