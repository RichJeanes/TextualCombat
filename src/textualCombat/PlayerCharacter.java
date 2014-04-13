package textualCombat;

public class PlayerCharacter {
    private String name;
    private int damage;
    private int health;
    
    public PlayerCharacter(String name) {
        //Get character info from db
        
        //Placeholder until actual login and db stuff is working
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setDmg(int d) {
        damage = d;
    }
    
    public int getDmg() {
        return damage;
    }
        
    public void setHealth(int h) {
        health = h;
    }
    
    public int getHealth() {
        return health;
    }           
    
    public String toString() {
        return name;
    }
}
