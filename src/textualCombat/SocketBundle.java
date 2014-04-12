package textualCombat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketBundle {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    
    public SocketBundle(Socket client) {
        this.client = client;
        
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch(IOException e) {
            System.err.println("Error creating socket IO");
        }
    }
    
    public void setIn(BufferedReader in) {
        this.in = in;
    }
    
    public void setOut(PrintWriter out) {
        this.out = out;
    }
    
    public BufferedReader getIn() {
        return in;
    }
    
    public PrintWriter getOut() {
        return out;
    }
    
    public void close() {
        try {
            in.close();
            out.close();
        } catch(IOException e) {
            System.err.println("Error closing socket IO stuff.");
        }
    }
}
