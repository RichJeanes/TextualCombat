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
    }

    @Override
    public void run() {
        player0.start();
        player1.start();
        
        player0.write("You're in combat with " + player1);
        player1.write("You're in combat with " + player0);
        System.out.println(player0 + " vs " + player1);

        player0.write("FIGHT!");
        player1.write("FIGHT!");

        currentHealths = new int[2];
        currentHealths[0] = player0.getPlayerInfo().getHealth();
        currentHealths[1] = player1.getPlayerInfo().getHealth();

        //COMBAAAAAAATT
        boolean combatOccurring = true;

        while (combatOccurring) {

            player0.write("Make your move:");
            player1.write("Make your move:");

            try {
                while (!getBothPlayerActionQueued()) {
                    Thread.sleep(500);
                }
            } catch (InterruptedException ie) {
            }

            if (getBothPlayerActionQueued()) {
                //do combat maths
                int dieRoll = rand.nextInt(20);
                int luckRoll = rand.nextInt(100);
                int dmgToPlayer0 = 0;
                float player0DmgMod = 1;
                int dmgToPlayer1 = 0;
                float player1DmgMod = 1;

                switch (player0.getAction()) {
                    case 0:
                        if((dieRoll + getMod(player0.getPlayerInfo().getStr())) > 8) {
                            if (dieRoll + getMod(player0.getPlayerInfo().getStr()) >= 19) {
                                player0.write("Critical hit!");
                                dmgToPlayer1 += player0.getPlayerInfo().getDmg() * 2;
                            } else {
                                dmgToPlayer1 += player0.getPlayerInfo().getDmg();
                            }
                        }
                            if (luckRoll > 96) {
                                player0.write("You got a lucky hit!");
                                dmgToPlayer1 += player0.getPlayerInfo().getDmg() * 1.6;
                            }
                        break;

                    case 1:
                        if ((dieRoll + getMod(player0.getPlayerInfo().getDef())) > 8) {
                            if ((dieRoll + getMod(player0.getPlayerInfo().getDef())) >= 19) {
                                player0.write("Amazing block!");
                                player0DmgMod = (float) .2;
                            } else {
                                player0.write("Nice block!");
                                player0DmgMod = (float) .6;
                            }
                        }
                        if (luckRoll > 96) {
                            player0.write("You got in a lucky hit!");
                            dmgToPlayer1 += player0.getPlayerInfo().getDmg() * .2;
                        }
                        break;

                    case 2:
                        if ((dieRoll + getMod(player0.getPlayerInfo().getAgl())) > 8) {
                            if ((dieRoll + getMod(player0.getPlayerInfo().getAgl())) >= 17) {
                                player0.write("Amazing dodge!");
                                player0DmgMod = 0;
                            } else {
                                player0.write("Nice dodge!");
                                player0DmgMod = (float) .6;
                            }

                            if (luckRoll > 96) {
                                player0.write("You got in a lucky hit!");
                                dmgToPlayer1 += player0.getPlayerInfo().getDmg() * .8;
                            }
                        }
                        break;
                }

                //do combat maths
                dieRoll = rand.nextInt(20);
                luckRoll = rand.nextInt(100);

                switch (player1.getAction()) {
                    case 0:
                    	if((dieRoll + getMod(player1.getPlayerInfo().getStr())) > 8) {
                            if (dieRoll + getMod(player1.getPlayerInfo().getStr()) >= 19) {
                                player1.write("Critical hit!");
                                dmgToPlayer0 += player1.getPlayerInfo().getDmg() * 2;
                            } else {
                                dmgToPlayer0 += player1.getPlayerInfo().getDmg();
                            }
                        }
                        if (luckRoll > 96) {
                            player1.write("You got a lucky hit!");
                            dmgToPlayer1 += player1.getPlayerInfo().getDmg() * 1.6;
                        }
                        break;

                    case 1:
                        if ((dieRoll + getMod(player1.getPlayerInfo().getDef())) > 8) {
                            if ((dieRoll + getMod(player1.getPlayerInfo().getDef())) >= 19) {
                                player1.write("Amazing block!");
                                player1DmgMod = (float) .2;
                            } else {
                                player1.write("Nice block!");
                                player1DmgMod = (float) .6;
                            }
                        }
                        if (luckRoll > 96) {
                            player1.write("You got in a lucky hit!");
                            dmgToPlayer0 += player1.getPlayerInfo().getDmg() * .2;
                        }
           
                        break;

                    case 2:
                        if ((dieRoll + getMod(player1.getPlayerInfo().getAgl())) > 8) {
                            if ((dieRoll + getMod(player1.getPlayerInfo().getAgl())) >= 17) {
                                player1.write("Amazing dodge!");
                                player1DmgMod = 0;
                            } else {
                                player1.write("Nice dodge!");
                                player1DmgMod = (float) .6;
                            }

                            if (luckRoll > 96) {
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

                player0.write("\r\nYou hit for " + dmgDealtToPlayer1);
                player1.write("\r\nYou hit for " + dmgDealtToPlayer0);

                player0.write("You have " + currentHealths[0] + " health remaining\r\n");
                player1.write("You have " + currentHealths[1] + " health remaining\r\n");

                player0.resetActionTaken();
                player1.resetActionTaken();
                player0ActionQueued = false;
                player1ActionQueued = false;

                if ((currentHealths[0] <= 0) || (currentHealths[1] <= 0)) {
                    if ((currentHealths[0] <= 0) && (currentHealths[1] <= 0)) {
                        endMatch(5);
                    } else if (currentHealths[0] <= 0) { // player 1 win
                    	player1.getPlayerInfo().incWins();
                    	if(player1.getPlayerInfo().getWins() % 5 == 0) {
                        	player1.getPlayerInfo().levelUp();
                        }
                    	
                    	endMatch(4);
                    } else if (currentHealths[1] <= 0) { // player 0 win
                    	player0.getPlayerInfo().incWins();
                    	if(player0.getPlayerInfo().getWins() % 5 == 0) {
                        	player0.getPlayerInfo().levelUp();
                        }

                    	endMatch(3);
                    }

                    player0.stopThread();
                    player1.stopThread();
                    MainThread.lobby.clientJoiningLobby(player0.getSocketBundle());
                    MainThread.lobby.clientJoiningLobby(player1.getSocketBundle());
                    combatOccurring = false;
                    
                    player0.write("Type \'join\' to return to the lobby!");
                    player1.write("Type \'join\' to return to the lobby!");
                    
                    return;
                }

            } else {
                if (player0ActionQueued) {
                    //player 1 did not act
                    endMatch(1);
                } else if (player1ActionQueued) {
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
                player0.write("You have slain you opponent!");
                player0.write("You live to fight another day.\r\n");

                player1.write("You have been defeated!\r\n");
                break;

            // player 1 wins legitimately
            case 4:
                player1.write("You have slain you opponent!");
                player1.write("You live to fight another day.\r\n");

                player0.write("You have been defeated!\r\n");
                break;

            // draw (somehow?)
            case 5:
                player0.write("You have slain eachother simultaneously.");
                player0.write("How unfortunate...\r\n");

                player1.write("You have slain eachother simultaneously.");
                player1.write("How unfortunate...\r\n");
                break;
        }
    }

    private int getMod(int score) {
    	return (int) Math.floor((score /2) - 5);
    }
    
    public void setPlayer0ActionQueued() {
        player0ActionQueued = true;
    }

    public void setPlayer1ActionQueued() {
        player1ActionQueued = true;
    }

    public boolean getBothPlayerActionQueued() {
        if (player0ActionQueued && player1ActionQueued) {
            return true;
        } else {
            return false;
        }
    }
}
