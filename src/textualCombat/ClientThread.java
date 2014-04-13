package textualCombat;

public class ClientThread extends Thread {
    protected SocketBundle client;
    
    @Override
    public void run() {}
    
    public SocketBundle getSocketBundle() {
        return client;
    }
    
    public PlayerCharacter getPlayerInfo() {
        return client.getPlayerInfo();
    }
    
    public void write(String output) {
        client.write(output);
    }
    
    public String read() {
        return client.read();
    }
    
    public void close() {
        System.out.println("Client Thread close");
        client.close();
    }
    
    public String toString() {
        return client.toString();
    }
}
