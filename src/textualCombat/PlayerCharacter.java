package textualCombat;

import java.util.Map;
import java.util.Random;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class PlayerCharacter {

    private String name;
    private int damage;
    private int strength;
    private int health;
    private int agility;
    private int defense;
    private int wins;
    private login database;

    public PlayerCharacter(String name) {
        //Get character info from db
        this.name = name;
        database = new login();
        Map<String, AttributeValue> user_char = database.find_char(name);
        health = Integer.parseInt(user_char.get("health").getN());
        damage = Integer.parseInt(user_char.get("attack").getN());
        defense = Integer.parseInt(user_char.get("defence").getN());
        strength = Integer.parseInt(user_char.get("strength").getN());
        agility = Integer.parseInt(user_char.get("agility").getN());
        wins = Integer.parseInt(user_char.get("wins").getN());
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

    public void incWins() {
        wins++;
        database.update_char(name, name, health, damage, defense, strength, agility, wins);
    }

    public int getWins() {
        return wins;
    }
    
    public void levelUp() {
    	Random rand = new Random();
    	
    	switch(rand.nextInt(4)) {
    	case 0:
    		damage++;
    		break;
    	case 1:
    		defense++;
    		break;
    	case 2:
    		strength++;
    		break;
    	case 3:
    		agility++;
    		break;
    	}
    	
    	health += 5;
    	
    	database.update_char(name, name, health, damage, defense, strength, agility, wins);
    }
    
    public String toString() {
        return name;
    }
}
