package textualCombat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private Thread parent;
    private SocketBundle client;
    
    private BufferedReader in;
    private PrintWriter out;
    
    //If constructor gets a socket, new client needs to be initialized
    public ClientThread(Thread parent, Socket client) {
        this.parent = parent;
        this.client = new SocketBundle(client);
        
        in = this.client.getIn();
        out = this.client.getOut();
    }
    
    //If constructor gets a bundle, client is existing
    public ClientThread(Thread parent, SocketBundle client) {
        this.parent = parent;
        this.client = client;
        
        in = client.getIn();
        out = client.getOut();
        
        if(out != null) System.out.println("here");
    }
    
    @Override
    public void run() {
        try {
            System.out.println(in.readLine());
            out.println("Acknowledged.");
            out.flush();
            //client.close();
            //MainThread.stop();
        } catch (IOException e) {
            System.err.println("Problem passing message between client and server.");
        }
        
    }
    
    public SocketBundle getSocketBundle() {
        return client;
    }
    
    public Thread getParent() {
        return parent;
    }
}
