package textualCombat;

public class InstanceClientThread extends ClientThread{
    
    private InstanceThread parent;
    private int id;
    private int action;
    private boolean actionTaken;
    
    public InstanceClientThread(InstanceThread parent, SocketBundle client, int id) {
        this.parent = parent;
        this.client = client;
        this.id = id;
    }
    
    public void run() {
        String input = null;
        boolean alive = true;
        
        actionTaken = false;
        
        while(alive) {
            input = client.read();
            System.out.println(client + " sent command " + input.toLowerCase().trim());
            
            try {
                switch(input.toLowerCase().trim()) {
                    case "attack": 
                        if(!actionTaken) {
                            action = 0;
                            actionTaken = true;
                        } else {
                            client.write("You already queued an action!");
                        }
                        break;

                    case "defend": 
                        if(!actionTaken) {
                            action = 1;
                            actionTaken = true;
                        } else {
                            client.write("You already queued an action!");
                        }
                        break;

                    case "dodge":
                        if(!actionTaken) {
                            action = 2;
                            actionTaken = true;
                        } else {
                            client.write("You already queued an action!");
                        }
                        break;

                    case "help":
                        client.write(printHelp());
                        break;

                    default:
                        client.write("Invalid command: " + input);
                        client.write("Type 'help' for a list of valid commands.");
                }
                
                if(id == 0 && actionTaken) {
                    parent.setPlayer0ActionQueued();
                }
                else if(actionTaken) {
                    parent.setPlayer1ActionQueued();
                }
                
            } catch (NullPointerException npe) {
                System.err.println("Connection terminated");
                alive = false;
            }
        }
    }
    
    public int getAction() {
        return action;
    }
    
    public void resetActionTaken() {
        actionTaken = false;
    }
    public String printHelp() {
        return "\nAvailable combat commands:\n" +
               "attack: Attempt to hit opponent with a melee attack\n" +
               "defend: Attempt to deflect opponent's attack and cause a stagger\n" +
               "help:   Print this help statement\n";              
    }
}
