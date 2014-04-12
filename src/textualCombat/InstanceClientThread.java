package textualCombat;

public class InstanceClientThread extends ClientThread{
    
    InstanceThread parent;
    
    public InstanceClientThread(InstanceThread parent, SocketBundle client) {
        this.parent = parent;
        this.client = client;
    }
}
