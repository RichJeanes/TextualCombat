package textualCombat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

public class MainThread {

    static boolean running = true;
    
    public static void main(String[] args) {
        
        ServerSocket sock = null;
        Socket newClient = null;
        
        try {
            sock = new ServerSocket(2323);
        } catch (IOException e) {
            System.err.println("Port taken. Is there another instance of this running");
            System.exit(1);
        }
        
        //create thread pool
        
        while(running) {
            try{
                //threadFromPool(sock.accept());
                new ClientThread(Thread.currentThread(), sock.accept()).start();
                System.out.println("Got new client.");
                
            } catch (IOException e) {
                System.err.println("Problem accepting new client.");
            } finally {
                newClient = null;
            }
        }
    }
    
    public static void stop() {
        //running = false;
        System.exit(0);
    }
    
}
