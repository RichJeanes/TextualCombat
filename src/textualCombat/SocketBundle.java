package textualCombat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class SocketBundle {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private PlayerCharacter playerInfo;
    private login database;

    public SocketBundle(Socket client) {
        this.client = client;
        database = new login();

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error creating socket IO");
        }

        out.print("\r\n\r\nWelcome to Textual Combat!!!\r\n\r\n");
        out.print("What is your user name?\r\n");
        out.flush();

        //TODO Read user input and validate against db
        //If username does not exist, prompt to create account
        //String clientUsername = in.readLine(); or something close to this
        //Placeholder until actual login is working
        String player_name = null;
        try{
        	player_name = in.readLine();
        }catch (IOException e){
        	System.err.println("Problem reading from client.");
        }
        boolean user_exist = database.does_user_exist(player_name);
        if(user_exist){
        	boolean password_correct = false;
        	//User Exists.
        	//ask for password.
        	//check password.
        	while(password_correct==false){
        		password_correct = database.check_user_and_password(player_name, user_password);
            	if(password_correct){
            		//password was correct
            		break;
            	}else{
            		//password was not correct
            	}
        	}
        	
        }else{
        	//User Does Not Exists.
        	//ask for a new password.
        	//add user to DB.
        }
        boolean char_exist = database.does_char_exist(player_name);
        if(char_exist){
        	//char does not exist
        }else{
        	//char does exist
        	String players_char = database.find_char_name(player_name);
        	playerInfo = new PlayerCharacter(players_char);
        }
    }

    public PlayerCharacter getPlayerInfo() {
        return playerInfo;
    }

    public void write(String input) {
        out.print(input + "\r\n");
        out.flush();
    }

    public String read() {
        try {
            return in.readLine();
        } catch (IOException ex) {
            System.err.println("Problem reading from client.");
        }

        return null;
    }

    public void close() {
        try {
            client.close();
            in.close();
            out.close();
            System.out.println("Socket Bundle close");
        } catch (IOException e) {
            System.err.println("Error closing socket IO stuff.");
        }
    }

    public String toString() {
        return playerInfo.toString();
    }
}
