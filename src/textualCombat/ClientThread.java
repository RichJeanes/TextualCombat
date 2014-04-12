package textualCombat;

public class ClientThread extends Thread {
    protected SocketBundle client;
    
    @Override
    public void run() {}
    
    public SocketBundle getSocketBundle() {
        return client;
    }
    
    public void write(String output) {
        client.write(output);
    }
    
    public String read() {
        return client.read();
    }
    
    public void close() {
        client.close();
        this.stop();
    }
    
    public String toString() {
        return client.toString();
    }
}
