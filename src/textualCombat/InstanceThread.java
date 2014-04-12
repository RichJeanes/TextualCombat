package textualCombat;

public class InstanceThread extends Thread {
    
    private InstanceClientThread player0, player1;
    private boolean player0ActionQueued, player1ActionQueued;
    
    public InstanceThread(LobbyClientThread p0, LobbyClientThread p1) {
        
        System.out.println("instance0");
        player0 = new InstanceClientThread(this, p0.getSocketBundle());
        System.out.println("instance1");
        player1 = new InstanceClientThread(this, p1.getSocketBundle());
        System.out.println("instance2");
        
        player0.start();
        player1.start();
    }
    
    @Override
    public void run() {
        player0.write("You're in combat with " + player1);
        player1.write("You're in combat with " + player0);
        System.out.println(player0 + " vs " + player1);
        
        player0.write("FIGHT!");
        player1.write("FIGHT!");
        System.out.println("FIGHT!");
        
        //COMBAAAAAAATT
        
        //Combat ends, clients sent back to lobby
        
        //for now:
        player0.close();
        System.out.println("Instance Thread.5 close");
        player1.close();
        System.out.println("Instance Thread close");
    }
}
