package application.Models;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import main.Main;

/**
 * Class to handle the loading user data
 * @author Sagar Bhatia
 *
 */
public class LoadClass extends Observable{
	
	int rowNum, colNum;
	int coordState;
	int score;
	String rowCoord, colCoord, shipType;
	String createdOn = "";
	String[] listOfSaves;
	
	public void setRadarGridCoords(int j, int i, int coordstate) {
		this.rowNum = j;
		this.colNum = i;
		this.coordState = coordstate;
		setChanged();
		notifyObservers("setcoordscomp");
	}
	
	public void setRadarGridCoordsT(int j, int i, int coordstate) {
		this.rowNum = j;
		this.colNum = i;
		this.coordState = coordstate;
	}
	
	public int[] getRadarGridCoords() {
		int coords[] = {this.rowNum, this.colNum, this.coordState};
		return coords;
	}
	
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		setChanged();
		notifyObservers("setscorecomp");
	}
	
	public void setScoreT(int score) {
		this.score = score;
	}
	
	public void setShipGridCoords(int j, int i, int coordstate) {
		this.rowNum = j;
		this.colNum = i;
		this.coordState = coordstate;
		setChanged();
		notifyObservers("setcoordsuser");
	}
	
	public int[] getShipGridCoords() {
		int coords[] = {this.rowNum, this.colNum, this.coordState};
		return coords;
	}
	
	public void setListOfSaves(String[] listOfSaves) {
		this.listOfSaves = listOfSaves;
		setChanged();
		notifyObservers("listofsaves");
	}
	
	public void setListOfSavesT(String[] listOfSaves) {
		this.listOfSaves = listOfSaves;
	
	}
	
	public String[] getListOfSaves() {
		return this.listOfSaves;
	}
	
	public int getUserScore() {
		return score;
	}

	public void setUserScore(int score) {
		this.score = score;
		setChanged();
		notifyObservers("setscoreuser");
	}
	
	public void setUserScoreT(int score) {
		this.score = score;
		
	}
	
	public void setColoredShipsCoord(String rowCoord, String colCoord, String shipType) {
		
		this.rowCoord = rowCoord;
		this.colCoord = colCoord;
		this.shipType = shipType;
		setChanged();
		notifyObservers("setcolorcoords");
		
	}
	
	public void setColoredShipsCoordT(String rowCoord, String colCoord, String shipType) {
		
		this.rowCoord = rowCoord;
		this.colCoord = colCoord;
		this.shipType = shipType;
		
	}

	public String[] getColoredShipsCoord() {
		String[] coords = {this.rowCoord, this.colCoord, this.shipType}; 
		return coords;
	}
	
	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	
	/**
	 * method to load the state of the required saved game
	 * @param computer	Object of Computer
	 * @param player	Object of Player
	 * @param saveClass	Object of SaveClass
	 * @param createdOn	createdOn criteria for selected saved game
	 * @param strategy	Object of HitStrategy
	 * @param strategySalvo	Object of HitStrategySalvo
	 */
	public void loadGame(Computer computer, Player player, SaveClass saveClass,
			String createdOn, HitStrategy strategy, HitStrategySalvo strategySalvo) {
		
		String folderPath = "User-Data\\"; 
		String username = saveClass.getuName();
		String line = "";
		this.createdOn = createdOn;
		try {
			Scanner in  = new Scanner(new File(folderPath+username+".txt"));
			while(in.hasNextLine()) {
				line = in.nextLine();
				if(line.contains(this.createdOn)) {
					break;
				}
			}
			Main.gameMode = in.nextLine().split(" ")[2];
			Main.gameType = in.nextLine().split(" ")[2];
			in.nextLine();
			if(Main.gameType.equals("Salvo"))
				loadComputerGrid(in, computer, strategySalvo);
			else
				loadComputerGrid(in, computer, strategy);
			//store sunken ships
			line = in.nextLine();
			loadComputerSunkenShips(line);
			line = in.nextLine();
			//store shipsmap
			loadComputerShipsMap(in);
			
			while(in.hasNextLine()) {
				line = in.nextLine();
				if(line.contains("Player ships status")) {
					break;
				}
			}
			//load the ships with colors
			loadColoredUserShips(in);
			//load the ship grid with hits and misses
			in  = new Scanner(new File(folderPath+username+".txt"));
			while(in.hasNextLine()) {
				line = in.nextLine();
				//"Sat Aug 03 00:16:01"
				if(line.contains(this.createdOn)) {
					break;
				}
			}
			while(in.hasNextLine()) {
				line = in.nextLine();
				if(line.contains("ShipGrid")) {
					break;
				}
			}
			loadUserGrid(in, player, computer);
			line = in.nextLine();
			loadUserSunkenShips(line);
			line = in.nextLine();
			Player.numOfShipsDep = 5;
			in.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * method to get the list of saved games
	 * @param saveClass object of save class
	 */
	@SuppressWarnings("unchecked")
	public void getSelectedLoadGame(SaveClass saveClass) {
		ArrayList<String> listofsaves = new ArrayList<>();
		String username = saveClass.getuName();
		String folderPath = "User-Data\\";
		String line;
		try {
			File file = new File(folderPath+username+".txt");
			if(file.exists() && !file.isDirectory()) {
				Scanner in  = new Scanner(new File(folderPath+username+".txt"));
				while(in.hasNextLine()) {
					line = in.nextLine();
					if(line.contains("Created on")) {
						listofsaves.add(line);
					}
				}
			}
			else {
				line = "No available save games";
				listofsaves.add(line);
			}
			String[] listofsavesarr = listofsaves.toArray(new String[listofsaves.size()]);
			setListOfSaves(listofsavesarr);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * method to load the computer grid
	 * @param in Scanner object
	 * @param computer	Computer object
	 * @param o	HitStrategy or HitStrategySalvo Object
	 */
	public void loadComputerGrid(Scanner in, Computer computer, Object o) {
		
		String line;
		int j = 0;
		while(in.hasNextLine()) {
			line = in.nextLine();
			if(line.contains("Computer Score")) {
				String[] line2 = line.split(" ");
				setScore(Integer.parseInt(line2[2]));
				if(o instanceof HitStrategySalvo)
					((HitStrategySalvo) o).scoring = Integer.parseInt(line2[2]);
				else
					((HitStrategy) o).scoring = Integer.parseInt(line2[2]);
				break;
			}
			for(int i = 0; i < line.length(); i++) {
				computer.computerGrid[j][i] = Integer.parseInt(Character.toString(line.charAt(i)));
				setRadarGridCoords(j, i, computer.computerGrid[j][i]);
			}
			j++;
		}
	}
	
	/**
	 * method to load the state of sunken Ships
	 * @param line	sunken ships line in the file
	 */
	public void loadComputerSunkenShips(String line) {
		
		Computer.sunkenShips = new ArrayList<>();
		String[] sublines = line.split(" ");
		for(int i = 3; i < sublines.length; i++) {
			Computer.sunkenShips.add(sublines[i]);
		}
			
	}
	
	/**
	 * method to load the computer shipsMap
	 * @param in Scanner object
	 */
	public void loadComputerShipsMap(Scanner in) {
		String line;
		ArrayList<String> tempList = new ArrayList<>();
		Map<String, ArrayList<String>> shipsMap = new HashMap<>();
		while(in.hasNextLine()) {
			line = in.nextLine();
			if(line.contains("Coordinates hit")) {
				loadComputerCoordinatesHit(line);
				break;
			}
			else {
				tempList = new ArrayList<>();
				String[] sublines = line.split(" ");
				for(int i = 1; i < sublines.length; i++) {
					tempList.add(sublines[i]);
				}
				shipsMap.put(sublines[0], tempList);
			}
		}
		Computer.shipsMap.putAll(shipsMap);
	}
	
	/**
	 * Method to load the computer coordinates that were hit
	 * @param line line in the text file that stored comp hit coordinates
	 */
	public void loadComputerCoordinatesHit(String line) {
		
		String[] subline = line.split(" ");
		Computer.coordinatesHit = new ArrayList<>();
		for(int i = 2; i < subline.length; i++) {
			Computer.coordinatesHit.add(subline[i]);
		}
	}
	
	/**
	 * Method to load the user coordinates that were hit
	 * @param line line in the text file that stored user hit coordinates
	 */
	public void loadUserCoordinatesHit(String line) {
		
		String[] subline = line.split(" ");
		Player.coordinatesHit = new ArrayList<>();
		for(int i = 2; i < subline.length; i++) {
			Player.coordinatesHit.add(subline[i]);
		}
		
	}
	
	/**
	 * method to load the user grid
	 * @param in Scanner Object
	 * @param player Player Object
	 * @param computer Computer Object
	 */
	public void loadUserGrid(Scanner in, Player player, Computer computer) {
		
		String line;
		int j = 0;
		while(in.hasNextLine()) {
			line = in.nextLine();
			if(line.contains("Player Score")) {
				String[] line2 = line.split(" ");
				setUserScore(Integer.parseInt(line2[2]));
				computer.scoringComp = Integer.parseInt(line2[2]);
				break;
			}
			for(int i = 0; i < line.length(); i++) {
				player.userGrid[j][i] = Integer.parseInt(Character.toString(line.charAt(i)));
				setShipGridCoords(j, i, player.userGrid[j][i]);
			}
			j++;
		}
	}
	
	/**
	 * method to load the ships in colored button
	 * @param in Scanner object
	 */
	public void loadColoredUserShips(Scanner in) {
		String line;
		ArrayList<String> tempList = new ArrayList<>();
		Map<String, ArrayList<String>> shipsMap = new HashMap<>();
		while(in.hasNextLine()) {
			line = in.nextLine();
			if(line.contains("Coordinates hit")) {
				loadUserCoordinatesHit(line);
				break;
			}
			else {
				tempList = new ArrayList<>();
				String[] sublines = line.split(" ");
				for(int i = 1; i < sublines.length; i++) {
					tempList.add(sublines[i]);
					System.out.println(sublines[i]);
					String[] sublines2 = sublines[i].split(",");
					setColoredShipsCoord(sublines2[0], sublines2[1], sublines[0]);
				}
				shipsMap.put(sublines[0], tempList);
			}
		}
		Player.shipsMap = new HashMap<>();
		Player.shipsMap.putAll(shipsMap);
		System.out.println(Player.shipsMap);
	}
	
	/**
	 * method to load the sunken ships arraylist of the user 
	 * @param line sunken ships line in the text file
	 */
	public void loadUserSunkenShips(String line) {
		
		Player.sunkenShips = new ArrayList<>();
		String[] sublines = line.split(" ");
		for(int i = 3; i < sublines.length; i++) {
			Player.sunkenShips.add(sublines[i]);
		}
		
	}
	
}
