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
    private PlayerCharacter playerInfo;
    
    public SocketBundle(Socket client) {
        this.client = client;
        
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch(IOException e) {
            System.err.println("Error creating socket IO");
        }
        
        out.println("\n\nWelcome to Textual Combat!!!\n");
        out.println("What is your user name?");
        
        //TODO Read user input and validate against db
        //If username does not exist, prompt to create account
        //String clientUsername = in.readLine(); or something close to this
        
        //Placeholder until actual login is working
        try {
            playerInfo = new PlayerCharacter(in.readLine());
            playerInfo.setDmg(20);
            playerInfo.setHealth(100);
        } catch (IOException e) {
            System.err.println("Problem reading from client.");
        }
    }
    
    public PlayerCharacter getPlayerInfo() {
        return playerInfo;
    }
    
    public void write(String input) {
        out.println(input);
        out.flush();
    }
    
    public String read() {
        try {
            return in.readLine();
        } catch (IOException ex) {
            System.err.println("Problem reading from client.");
        }
        
        return null;
    }
    
    public void close() {
        try {
            client.close();
            in.close();
            out.close();
            System.out.println("Socket Bundle close");
        } catch(IOException e) {
            System.err.println("Error closing socket IO stuff.");
        }
    }
    
    public String toString() {
        return playerInfo.toString();
    }
}
