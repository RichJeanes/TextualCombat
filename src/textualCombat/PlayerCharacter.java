package textualCombat;

public class PlayerCharacter {
    private String name;
    
    public PlayerCharacter(String name) {
        //Get character info from db
        
        //Placeholder until actual login and db stuff is working
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public String toString() {
        return name;
    }
}
