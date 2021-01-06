package application.Controllers;

import application.Exception.LocationHitException;
import application.Exception.WrongMethodException;
import application.Models.Computer;
import application.Models.HitStrategy;
import application.Models.HitStrategySalvo;
import application.Models.LoadClass;
import application.Models.Player;
import application.Models.SaveClass;
import main.Main;

/**
 * 
 * Class provides the AI functionality of the Computer and the User
 * 
 * @author Prateek
 * 
 *
 */
public class GridUser {

	Computer computer;
	Player player;
	HitStrategy strategy;
	HitStrategySalvo strategySalvo;
	SaveClass saveClass;
	LoadClass loadClass;
	String callType = "";

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	/**
	 * Constructor to initialize the required object
	 * 
	 * @param player        Player model object
	 * @param computer      Computer model object
	 * @param strategy      HitStrategy model object
	 * @param strategySalvo HitStrategySalvo object
	 * @param saveClass		SaveClass Object
	 * @param loadClass		LoadClass Object
	 */
	public GridUser(Player player, Computer computer, HitStrategy strategy, HitStrategySalvo strategySalvo,
			SaveClass saveClass, LoadClass loadClass) {
		this.computer = computer;
		this.player = player;
		this.strategy = strategy;
		this.strategySalvo = strategySalvo;
		this.saveClass = saveClass;
		this.loadClass = loadClass;

	}

	/**
	 * Provides if its a hit or miss while hitting on the computer grid
	 * 
	 * @param hitResult String defining the computer turn results
	 * @param gameMode  Tells the game mode
	 */
	public void computerTurn(Boolean hitResult, String gameMode) {
		try {
			if (Main.gameType.equals("Salvo")) {
				setCallType("Salvo");
				strategySalvo.mediumMode(hitResult);
			} else {
				// set call type for junit
				setCallType("Normal");
				if (gameMode.equals("Easy"))
					strategy.randomHit();
				else if (gameMode.equals("Medium"))
					strategy.mediumMode(hitResult);
				else
					strategy.hardMode(hitResult);
			}
		} catch (LocationHitException e) {
			System.out.println("1"+ e);
		}

	}

	/**
	 * 
	 * checks the grid of the computer to verify if they won or not sets the static
	 * flag true if someone wins
	 */
	public void callCheckIfUserWon() {
		computer.checkIfUserWon();
	}
	
	/**
	 * Method to call the method for deploying the computer's ships
	 */
	public void deployCompShips() {
		computer.deployComputerShips();
	}
	
	/**
	 * Method to call the method to deploy the user ships
	 */
	public void deployUserShips() {
		player.deployUserRandomShips();
	}

	public void checkingManagedException(String methodName) {
		try {
			throw new WrongMethodException("No method found" + methodName);
		} catch (WrongMethodException e) {
			System.out.println(e);
		}
	}

	/**
	 * 
	 * checks the grid of the User to verify if user has won or not displays the Win
	 * case if User won
	 * 
	 */
	public void callCheckIfCompWon() {
		player.checkIfCompWon();

	}

	/**
	 * This method will user turn in the model to check whether the coordinates are
	 * hit or not
	 * 
	 * @param coordX cordinate x axis
	 * @param coordY cordinate y axis
	 */
	public void callUserTurn(int coordX, int coordY) {
		try {
			computer.userTurn(coordX, coordY);
		} catch (LocationHitException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}

	}

	/**
	 * This method will tell the computer model to deploy its ships in the backend
	 * 
	 * @param computer Computer model object
	 */
	public void callDeployComputerShips(Computer computer) {
		computer.deployComputerShips();
	}
	
	/**
	 * Method to call the deploy user ships method in Computer class
	 * @param coordinates	x and y coordinate
	 * @param shipType	type of the ship
	 */
	public void callDeployUserGrid(String coordinates, String shipType) {
		player.deployUserGrid(coordinates, shipType);
	}
	
	/**
	 * Method to call the checkSunkenShips method in the computer  
	 * @param computer Computer Object
	 */
	public void callSunkenShips(Computer computer) {
		computer.checkSunkenShips();
	}
	
	/**
	 * method to call the checkSunkenShips method in the player
	 */
	public void callPlayerSunkenShips() {
		Player.checkSunkenShips();
	}
	
	/**
	 * Method to call send to hit method in network mode
	 * @param x	x-coordinate
	 * @param y y-coordinate
	 */
	public void CallPlayer1Hit2Send(int x, int y) {
		player.Player1Hit2Send(x, y);
	}
	
	/**
	 * Method to call send to hit method in network mode
	 * @param x	x-coordinate
	 * @param y y-coordinate
	 */
	public void CallPlayer2Hit1Send(int x, int y) {
		player.Player2Hit1Send(x, y);
	}

	/**
	 * method to transfer the call to checkUserName in models
	 * 
	 * @param userName  username that the user entered
	 * @param playerType type of the player
	 * @throws Exception io exception
	 */
	public void checkUserName(String userName, String playerType) throws Exception {
		System.out.println("passing call from controller");
		saveClass.checkUserName(userName, playerType);
	}
	
	/**
	 * Method to call the method to save the game state
	 * @param gameMode Easy/Difficult/Hard
	 * @param gameType Classic/Salvo
	 */
	public void saveGame(String gameMode, String gameType) {
		if (gameType.equals("Salvo"))
			saveClass.saveGame(player, computer, strategySalvo, gameMode, gameType);
		else
			saveClass.saveGame(player, computer, strategy, gameMode, gameType);
	}
	
	/**
	 * Method to call the method to load the game state
	 *@param createdOn	date when the save was created
	 */
	public void loadGame(String createdOn) {
		loadClass.loadGame(computer, player, saveClass, createdOn, strategy, strategySalvo);
	}
	
	/**
	 * Method to get the desired loaded game
	 */
	public void getSelectedLoadGame() {
		loadClass.getSelectedLoadGame(saveClass);
		;
	}
}
