package textualCombat;

import java.net.Socket;

public class LobbyClientThread extends ClientThread{
    
    public LobbyClientThread(Thread parent, Socket client) {
        super(parent, client);
    }
    
    public LobbyClientThread(Thread parent, SocketBundle client) {
        super(parent, client);
    }
}
