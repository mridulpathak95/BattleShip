package application.Models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Observable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Main;

/**
 * Class to save the user data
 * @author Sagar Bhatia
 *
 */
public class SaveClass extends Observable {
	
	String name;
	boolean nameSame;
	String filePath;
	String folderPath;
	String uName = "";
	ArrayList<String> computerCoordinatesHit;
	ArrayList<String> userCoordinatesHit;
	
	public SaveClass(){
		filePath = "User-Data\\Users-List.txt";
		folderPath = "User-Data\\";
		this.computerCoordinatesHit = new ArrayList<>();
		this.userCoordinatesHit = new ArrayList<>();
		
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public String getFolderPath() {
		return folderPath;
	}
	
	public boolean isnameSame() {
		return nameSame;
	}

	public void setNameSame(boolean r, String userName, String playerType) throws Exception {
		nameSame = r;
		
		if(!isnameSame() && playerType.equals("New Player")) {
			saveUserName(userName);
		}
		setChanged();
		notifyObservers(r);
	}
	
	public String getuName() {
		return this.uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}
	
	public ArrayList<String> getComputerCoordinatesHit() {
		return computerCoordinatesHit;
	}

	public void setComputerCoordinatesHit(ArrayList<String> computerCoordinatesHit) {
		this.computerCoordinatesHit = computerCoordinatesHit;
	}
	

	public ArrayList<String> getUserCoordinatesHit() {
		return userCoordinatesHit;
	}

	public void setUserCoordinatesHit(ArrayList<String> userCoordinatesHit) {
		this.userCoordinatesHit = userCoordinatesHit;
	}

	/**
	 * This function will save the username for the new user
	 * @param userName name chosen by the user
	 * @throws Exception file not found
	 */
	public void saveUserName(String userName) throws Exception {
		System.out.println("writing name in file "+userName);
		setuName(userName);
		FileWriter fw = new FileWriter(filePath, true);
	    BufferedWriter bw = new BufferedWriter(fw);
	    PrintWriter out = new PrintWriter(bw);
	    out.println(userName);
	    out.close();
			
	}
	
	/**
	 * This function checks whether there is any other user of the same name
	 * @param userName user name passed from the view
	 * @param playerType type of player
	 * @throws Exception file not Found
	 */
	public void checkUserName(String userName, String playerType) throws Exception{
		
		boolean check = false;
		System.out.println("Checking Username");
		File file = new File(filePath);
		if(file.exists()) {
			Scanner in = new Scanner(file);
			//if a user with the same name is found, then choose different name
			while(in.hasNextLine()) {
				if(userName.equals(in.nextLine())){
					
					check = true;
					System.out.println("Username found");
					setuName(userName);
					if(Main.playerNum == 2)
						Main.resultLabel2.setText(userName);
					else
						Main.resultLabel1.setText(userName);
					break;
					
				}
			}
			in.close();
		}
		setNameSame(check, userName, playerType) ;
	}
	
	/**
	 * method to save the current state of the game in a new file
	 * @param player Player Object
	 * @param computer Computer Object
	 * @param strategy HitStrategy Object
	 * @param gameMode Easy/ Medium/ Difficult
	 * @param gameType Classic/Salvo
	 */
	public void saveGame(Player player, Computer computer, 
			Object strategy, String gameMode, String gameType) {
	
		System.out.println("writing data in file "+this.uName);
		try {

			//gets the total saved files in the folder;
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date date = new Date();
		    formatter.format(date);
		    FileWriter fw = new FileWriter(folderPath+this.uName+".txt", true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw);
		    out.println("Created on: "+date);
		    out.println("Game Mode: "+gameMode);
		    out.println("Game Type: "+gameType);
		    saveData(out, player, computer, strategy);
		    out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
		
	/**
	 * method to save user data
	 * @param out printwriter object
	 * @param player Player object
	 * @param computer Computer object
	 */
	void saveData(PrintWriter out, Player player, Computer computer, Object strategy) {
		
		out.println("CompGrid:");
		int[][] saveCompGrid = new int[Computer.computerGrid.length][Computer.computerGrid[0].length];
		for (int i = 0; i < Computer.computerGrid.length; ++i) {
	         saveCompGrid[i] = new int[Computer.computerGrid[i].length];
	         for (int j = 0; j < saveCompGrid[i].length; ++j) {
	            saveCompGrid[i][j] = Computer.computerGrid[i][j];
	            out.print(saveCompGrid[i][j]);
	         }
	         out.println("");
		}
		if(strategy instanceof HitStrategy)
			out.println("Computer Score: "+ ((HitStrategy)strategy).getScore());
		else
			out.println("Computer Score: "+ ((HitStrategySalvo)strategy).getScore());
		out.print("Computer Sunken ships:");
		saveComputerSunkenShips(out);
		out.println("Computer ships status: ");
		saveComputerShipsMap(out);
		out.print("Coordinates hit:");
		setComputerCoordinatesHit(Computer.coordinatesHit);
		saveComputerCoordinatesHit(out);
		out.println("ShipGrid:");
		int[][] saveUserGrid = new int[Player.userGrid.length][Player.userGrid[0].length];
		for (int i = 0; i < Player.userGrid.length; ++i) {
	         saveUserGrid[i] = new int[Player.userGrid[i].length];
	         for (int j = 0; j < saveUserGrid[i].length; ++j) {
	            saveUserGrid[i][j] = Player.userGrid[i][j];
	            out.print(saveUserGrid[i][j]);
	         }
	         out.println("");
		}
		out.println("Player Score: "+computer.getScoreComp());
		out.print("Player Sunken ships:");
		saveUserSunkenShips(out);
		out.println("Player ships status: ");
		saveUserShipsMap(out);
		out.print("Coordinates hit:");
		setComputerCoordinatesHit(Computer.coordinatesHit);
		saveUserCoordinatesHit(out);		
	}
	
	/**
	 * Method to store the coordinatesHit ArrayList in Computer class
	 * @param out PrintWriter Object
	 */
	public void saveComputerCoordinatesHit(PrintWriter out){
		
		for (int i = 0; i < Computer.coordinatesHit.size(); i++) {
			out.print(" "+Computer.coordinatesHit.get(i));
		}
		out.println("");		
	}
	
	/**
	 * Method to store the coordinatesHit ArrayList in Player class
	 * @param out PrintWriter Object
	 */
	public void saveUserCoordinatesHit(PrintWriter out){
		
		for (int i = 0; i < Player.coordinatesHit.size(); i++) {
			out.print(" "+Player.coordinatesHit.get(i));
		}
		out.println("");
	}
	
	/**
	 * method to save the computer ships hashmap
	 * @param out Printwriter Object
	 */
	public void saveComputerShipsMap(PrintWriter out) {
		for (Map.Entry<String, ArrayList<String>> entry : Computer.shipsMap.entrySet()) {
			out.print(entry.getKey());
			for(String it : entry.getValue()) {
				out.print(" "+it);
			}
			out.println("");
		}
	}
	
	/**
	 * method to save the user ships hasmap
	 * @param out PrintWriter Object
	 */
	public void saveUserShipsMap(PrintWriter out) {
		System.out.println("PLayer ships map "+Player.shipsMap);
		for (Map.Entry<String, ArrayList<String>> entry : Player.shipsMap.entrySet()) {
			out.print(entry.getKey());
			for(String it : entry.getValue()) {
				out.print(" "+it);
			}
			out.println("");
		}
	}
	
	/**
	 * Method to store the sunken ships of the Computer
	 * @param out PrintWriter
	 */
	public void saveComputerSunkenShips(PrintWriter out) {
		for(int i = 0; i < Computer.sunkenShips.size(); i++) {
			out.print(" "+Computer.sunkenShips.get(i));
		}
		
		out.println("");
	}
	
	/**
	 * Method to store the sunken ships of the User
	 * @param out PrintWriter
	 */
	public void saveUserSunkenShips(PrintWriter out) {
		for(int i = 0; i < Player.sunkenShips.size(); i++) {
			out.print(" "+Player.sunkenShips.get(i));
		}
		
		out.println("");
	}
	
}
