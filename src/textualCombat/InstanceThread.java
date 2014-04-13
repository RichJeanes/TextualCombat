package textualCombat;

public class InstanceThread extends Thread {
    
    private InstanceClientThread player0, player1;
    private boolean player0ActionQueued, player1ActionQueued;
    private int[] currentHealths;
    
    public InstanceThread(SocketBundle p0, SocketBundle p1) {
        
        player0 = new InstanceClientThread(this, p0, 0);
        player1 = new InstanceClientThread(this, p1, 1);
        
        
        
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
        
        currentHealths = new int[2];
        currentHealths[0] = player0.getPlayerInfo().getHealth();
        currentHealths[1] = player1.getPlayerInfo().getHealth();
        
        //COMBAAAAAAATT
        boolean combatOccurring = true;
        
        while(combatOccurring) {
            try{
                for(int i = 0; i < 20; i++) {
                    if(getBothPlayerActionQueued()) break;
                    Thread.sleep(500);
                }
            } catch(InterruptedException ie) {}
            
            if(getBothPlayerActionQueued()) {
                    // combat engage
                   System.out.println("New combat turn!");
                   
                   
                   //do combat maths
                   switch(player0.getAction()) {
                       case 0:
                           currentHealths[1] -= player0.client.getPlayerInfo().getDmg();
                           break;
                   }

                   //do combat maths
                   switch(player1.getAction()) {
                       case 0:
                           currentHealths[0] -= player1.client.getPlayerInfo().getDmg();
                           break;
                   }
                   
                   player0.write("You hit for " + player0.client.getPlayerInfo().getDmg());
                   player0.write("You have " + currentHealths[0] + " health remaining");
                   
                   player1.write("You hit for " + player1.client.getPlayerInfo().getDmg());
                   player1.write("You have " + currentHealths[1] + " health remaining");
                   
                   //determine if match is over
                   //eg check hp
                   
                   
                   player0.resetActionTaken();
                   player1.resetActionTaken();
                   player0ActionQueued = false;
                   player1ActionQueued = false;
                   
            } else {
                if(player0ActionQueued) {
                    //player 1 did not act
                    endMatch(1);
                } else if(player1ActionQueued){
                    //player 0 did not act
                    endMatch(0);
                } else {
                    //neither acted
                    endMatch(2);
                }
            }
        }
        //Combat ends, clients sent back to lobby
    }
        //for now:
     public void endMatch(int reason) {
         switch (reason) {
             // player 0 drops
             case 0:
                 break;
                 
             // player 1 drops
             case 1:
                 break;
                 
             // all the drops
             case 2:
                 break;
                 
             // player 0 wins legitmately
             case 3:
                 break;
                 
             // player 1 wins legitimately
             case 4: 
                 break;
                 
             // draw (somehow?)
             case 5:
                 break;
         }
    }
     
     public void setPlayer0ActionQueued() {
         player0ActionQueued = true;
     }
     
     public void setPlayer1ActionQueued() {
         player1ActionQueued = true;
     }
     
     public boolean getBothPlayerActionQueued() {
         if(player0ActionQueued && player1ActionQueued) {
             return true;
         } else {
             return false;
         }
     }
}
