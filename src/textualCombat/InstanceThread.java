package textualCombat;

import java.util.Random;

public class InstanceThread extends Thread {
    
    private Random rand = new Random();
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
                   //do combat maths
                   int dieRoll = rand.nextInt(20);
                   int luckRoll = rand.nextInt(100);
                   int dmgToPlayer0 = 0;
                   float player0DmgMod = 1;
                   int dmgToPlayer1 = 0;
                   float player1DmgMod = 1;
                   
                   switch(player0.getAction()) {
                       case 0:
                           if((dieRoll + player0.getPlayerInfo().getStr()) >= 19) {
                               player0.write("Critical hit!");
                               player0.write("You hit for " + (player0.getPlayerInfo().getDmg() * 2));
                               dmgToPlayer1 += player0.getPlayerInfo().getDmg() * 2;
                           } else if(luckRoll > 96) {
                               player0.write("You got a lucky hit!");
                               player0.write("You hit for " + (player0.getPlayerInfo().getDmg() * 1.6));
                               dmgToPlayer1 += player0.getPlayerInfo().getDmg() * 1.6;
                           } else {
                               player0.write("You hit for " + player0.getPlayerInfo().getDmg());
                               dmgToPlayer1 += player0.getPlayerInfo().getDmg();
                           }
                           
                           break;
                           
                       case 1:
                           if((dieRoll + player0.getPlayerInfo().getDef()) > 8) {
                               if((dieRoll + player0.getPlayerInfo().getDef()) >= 19) {
                                   player0.write("Amazing block!");
                                   player0DmgMod = (float) .2;
                               } else {
                                   player0.write("Nice block!");
                                   player0DmgMod = (float) .6;
                               }
                           }
                           if(luckRoll > 96) {
                               player0.write("You got in a lucky hit!");
                               dmgToPlayer1 += player0.getPlayerInfo().getDmg() * .2;
                           }
                           break;
                       
                       case 2:
                           if((dieRoll + player0.getPlayerInfo().getAgl()) > 8) {
                               if((dieRoll + player0.getPlayerInfo().getAgl()) >= 17) {
                                   player0.write("Amazing dodge!");
                                   player0DmgMod = 0;
                               } else {
                                   player0.write("Nice dodge!");
                                   player0DmgMod = (float) .6;
                               }
                               
                               if(luckRoll > 96) {
                               player0.write("You got in a lucky hit!");
                               dmgToPlayer1 += player0.getPlayerInfo().getDmg() * .8;
                               }
                           }
                           break;
                   }

                   //do combat maths
                   dieRoll = rand.nextInt(20);
                   luckRoll = rand.nextInt(100);
                   
                   switch(player1.getAction()) {
                       case 0:
                           if((dieRoll + player1.getPlayerInfo().getStr()) >= 19) {
                               player1.write("Critical hit!");
                               
                               dmgToPlayer0 += player1.getPlayerInfo().getDmg() * 2;
                           } else if(luckRoll > 96) {
                               player1.write("You got a lucky hit!");
                               player1.write("You hit for " + (player0.getPlayerInfo().getDmg() * 1.6));
                               dmgToPlayer0 += player1.getPlayerInfo().getDmg() * 1.6;
                           } else {
                               player1.write("You hit for " + player1.getPlayerInfo().getDmg());
                               dmgToPlayer0 += player1.getPlayerInfo().getDmg();
                           }
                           
                           break;
                           
                       case 1:
                           if((dieRoll + player1.getPlayerInfo().getDef()) > 8) {
                               if((dieRoll + player1.getPlayerInfo().getDef()) >= 19) {
                                   player1.write("Amazing block!");
                                   player1DmgMod = (float) .2;
                               } else {
                                   player1.write("Nice block!");
                                   player1DmgMod = (float) .6;
                               }
                           }
                           if(luckRoll > 96) {
                               player1.write("You got in a lucky hit!");
                               dmgToPlayer0 += player1.getPlayerInfo().getDmg() * .2;
                           }
                           break;
                       
                       case 2:
                           if((dieRoll + player1.getPlayerInfo().getAgl()) > 8) {
                               if((dieRoll + player1.getPlayerInfo().getAgl()) >= 17) {
                                   player1.write("Amazing dodge!");
                                   player1DmgMod = 0;
                               } else {
                                   player1.write("Nice dodge!");
                                   player1DmgMod = (float) .6;
                               }
                               
                               if(luckRoll > 96) {
                               player1.write("You got in a lucky hit!");
                               dmgToPlayer0 += player1.getPlayerInfo().getDmg() * .8;
                               }
                           }
                           break;
                   }
                   
                   int dmgDealtToPlayer0 = (int) (dmgToPlayer0 * player0DmgMod);
                   int dmgDealtToPlayer1 = (int) (dmgToPlayer1 * player1DmgMod);
                   
                   currentHealths[0] -= dmgDealtToPlayer0;
                   currentHealths[1] -= dmgDealtToPlayer1;
                   
                   player0.write("You hit for " + dmgDealtToPlayer1);
                   player1.write("You hit for " + dmgDealtToPlayer0);
                   
                   player0.write("You have " + currentHealths[0] + " health remaining");
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
