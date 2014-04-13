package textualCombat;

public class PlayerCharacter {
    private String name;
    private int damage;
    private int strength;
    private int health;
    private int agility;
    private int defense;
    
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
    
    public void setStr(int s) {
        strength = s;
    }
    
    public int getStr() {
        return strength;
    }
    
    public void setAgl(int a) {
        agility = a;
    }
    
    public int getAgl() {
        return agility;
    }

    public void setDef(int d) {
        defense = d;
    }
    
    public int getDef() {
        return defense;
    }
    
    public String toString() {
        return name;
    }
}
