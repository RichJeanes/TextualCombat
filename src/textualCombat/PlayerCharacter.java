package textualCombat;

import java.util.Map;
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
        health = Integer.parseInt(user_char.get("health").getS());
        damage = Integer.parseInt(user_char.get("attack").getS());
        defense = Integer.parseInt(user_char.get("defence").getS());
        strength = Integer.parseInt(user_char.get("strength").getS());
        agility = Integer.parseInt(user_char.get("agility").getS());
        wins = Integer.parseInt(user_char.get("wins").getS());
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
    
    public String toString() {
        return name;
    }
}
