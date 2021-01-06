package application.Models;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import application.Exception.NegativeScore;
import application.Exception.PortException;
import application.Views.AlertBox;
import application.Views.RadarGrid;
import application.Views.ShipGrid;
import application.Views.UserDetailsWindow;
import javafx.application.Platform;
import main.Main;

/**
 * Class to set the states for the functionality of user grid
 * 
 * @author Sagar Bhatia
 *
 */
public class Player extends Observable {

	public static int userScore;
	public List<String> deployedShips = new ArrayList<>();
	public HashMap<String, Integer> convert = new HashMap<>();
	public static Map<String, ArrayList<String>> shipsMap = new HashMap<>();
	public static ArrayList<String> sunkenShips = new ArrayList<String>();
	public static ArrayList<String> coordinatesHit = new ArrayList<String>();
	public int hitX, hitY;
	public static int numOfShipsDep = 0;

	Boolean server1Flag = true, server2Flag = true, handshake1 = false, handshake2 = false;
	String ipAddress1 = "132.205.94.99", ipAddress2 = "132.205.94.100";
	boolean time1 = false, time2 = false;
	double timea = 0;
	double timeb = 0;

	public String shipType = "";
	// msg for himself that he has won or not
	public String PlayerWon = "";

	// msg from other player that he won or not

	public String OtherWon = "";

	public String getOtherWon() {
		return OtherWon;
	}

	public void setOtherWon(String otherWon) {
		OtherWon = otherWon;
		setChanged();
		notifyObservers("VsmodeOtherWon");
	}

	public void setOtherWonT(String otherWon) {
		OtherWon = otherWon;
	}

	public String getPlayerWon() {
		return PlayerWon;
	}

	public void setPlayerWon(String playerWon) {
		PlayerWon = playerWon;
		setChanged();
		notifyObservers("VsmodeWon");

	}

	// gives that which player is playing
	public static int PlayerNum = 0;

	public int getPlayerNum() {
		return PlayerNum;
	}

	public void setPlayerNum(int playerNum) {
		PlayerNum = playerNum;
	}

	int[] coords = {};

	String axis = "";

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		Player.name = name;
	}

	public String reply = "";
	public int scoring;
	public String compWon = "";

	public boolean Hit = false;
	public boolean Miss = false;

	final static int rows = 9;
	final static int cols = 11;
	static String name = "";
	SaveClass saveObj;
	Properties prop = new Properties();

	String propFileName = "C:\\Users\\ar_jave\\git\\ProjectBattleshipGame\\BattleShipGame\\src\\config.properties";
	InputStream inputStream = null;// new FileInputStream(propFileName);

	// original Grid that remains unchanged throughout the game
	public static Integer[][] userGrid = new Integer[rows][cols];

	public Player(SaveClass saveObj) {
		this.saveObj = saveObj;

		initialize();

	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
		setChanged();
		notifyObservers("RadarSet");
	}

	public String getShipGridReply() {
		return reply;
	}

	public void setShipGridReply(String reply) {
		this.reply = reply;
		setChanged();
		notifyObservers("ShipGridSet");
	}

	public void setScore(int s) {
		try {
			scoring = scoring + s;
			if (scoring < 0) {
				throw new NegativeScore("Score can't be negative");
			}
		} catch (NegativeScore e) {
			scoring = 0;
			System.out.println(e);
		}
	}

	public int getScore() {
		return scoring;
	}

	public boolean isHit() {
		return Hit;
	}

	public void setHit(boolean hit) {
		Hit = hit;
	}

	public boolean isMiss() {
		return Miss;
	}

	public void setMiss(boolean miss) {
		Miss = miss;
	}

	public int getNumOfShipsDep() {
		return numOfShipsDep;
	}

	public Integer[][] getUserGrid() {
		return userGrid;
	}

	public List<String> getDeployedShips() {
		return deployedShips;
	}

	public int[] getCoords() {
		return coords;
	}

	public void setCoords(int[] coords) {
		this.coords = coords;
	}

	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	public static void setSunkenShips(String shipType) {
		sunkenShips.add(shipType);
	}

	public ArrayList<String> getSunkenShips() {

		return sunkenShips;

	}

	public String getCompWon() {
		return compWon;
	}

	public void setCompWon(String compWon) {
		this.compWon = compWon;
		setChanged();
		notifyObservers("compwon");
	}

	/**
	 * 
	 * @param coordinates defines the coordinated to be deployed
	 * @param shipType    type of the ship
	 */
	public void deployUserGrid(String coordinates, String shipType) {
		try {

			String str[] = coordinates.split("\\s");
			List<String> templist = new ArrayList<>();

			int y1 = Integer.parseInt(str[0]);
			int x1 = Integer.parseInt(str[1]);
			int y2 = Integer.parseInt(str[2]);
			int x2 = Integer.parseInt(str[3]);

			// ships cannot be placed outside the grid
			if (x1 >= cols || y1 >= rows || x2 >= cols || y2 >= rows) {
				deployedShips.remove(shipType);
				setReply("You can't place ships outside the " + rows + " by " + cols + " grid");
			}

			// coordinates cannot be negative
			if (x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0) {
				deployedShips.remove(shipType);
				setReply("You can't place ships outside the " + rows + " by " + cols + " grid");
			}

			if (x1 == x2) {

				// if two X are the same then the line is vertical
				int count = 0;
				if (y1 > y2) {
					int temp = y2;
					y2 = y1;
					y1 = temp;
				}
				for (int i = y1; i <= y2; i++) {
					if (userGrid[i][x1] == 1) {
						deployedShips.remove(shipType);
						setReply("ships cannot be placed on the same location");
					}

				}
				if (deployedShips.contains(shipType)) {
					for (int i = y1; i <= y2; i++) {
						if (adjacentShipCheck(i, x1)) {
							deployedShips.remove(shipType);
							setReply("Ship cannot be placed Adjacent to each other");
						}
					}
				}
				String res = areHolesValid(y2 - y1 + 1, shipType);
				if (res.equals("YES") && deployedShips.contains(shipType)) {
					for (int i = y1; i <= y2; i++) {
						if ((x1 >= 0 && x1 < cols) && (y1 >= 0 && y1 < rows) && (y2 >= 0 && y2 < rows)
								&& (userGrid[i][x1] == 0)) {
							userGrid[i][x1] = 1;
							templist.add(Integer.toString(i) + "," + Integer.toString(x1));
							count++;
						}
					}

					if (((y2 + 1) - (y1 + 1)) + 1 == count) {
						shipsMap.put(shipType, (ArrayList) templist);
						int[] coords = { x1, y1, x2, y2 };
						setCoords(coords);
						setShipType(shipType);
						setAxis("Y");
						numOfShipsDep++;
						setReply("Done");
					}
				}
			}

			else if (y1 == y2) {
				// if two Y are the same then the line is Horizontal
				int count = 0;
				if (x1 > x2) {
					int temp = x2;
					x2 = x1;
					x1 = temp;
				}
				for (int i = x1; i <= x2; i++) {
					if (userGrid[y1][i] == 1) {
						// displayUserShips();
						deployedShips.remove(shipType);
						setReply("ships cannot be placed on the same location");
					}
				}
				if (deployedShips.contains(shipType)) {
					for (int i = x1; i <= x2; i++) {
						if (adjacentShipCheck(y1, i)) {
							deployedShips.remove(shipType);
							setReply("Ship cannot be placed Adjacent to each other");
						}
					}
				}
				String res = areHolesValid(x2 - x1 + 1, shipType);
				if (res.equals("YES") && deployedShips.contains(shipType)) {
					for (int i = x1; i <= x2; i++) {
						if ((y1 >= 0 && y1 < rows) && (x1 >= 0 && x1 < cols) && (x2 >= 0 && x2 < cols)
								&& (userGrid[y1][i] == 0)) {
							userGrid[y1][i] = 1;
							templist.add(Integer.toString(y1) + "," + Integer.toString(i));
							count++;
						}
					}

					if (((x2 + 1) - (x1 + 1)) + 1 == count) {
						shipsMap.put(shipType, (ArrayList) templist);
						int[] coords = { x1, y1, x2, y2 };
						setCoords(coords);
						setShipType(shipType);
						setAxis("X");
						numOfShipsDep++;
						setReply("Done");
					}

				}
			} else {
				deployedShips.remove(shipType);
				setReply("Can not place ship Diagonal");
			}
		} catch (Exception e) {
			deployedShips.remove(shipType);
			setReply("Invalid input, please try again.");
		} finally {
			setChanged();
			notifyObservers("ShipDeploy");
		}
	}

	/**
	 * Method to check whether the ships are placed adjacent to each other or not
	 * 
	 * @param i x-axis
	 * @param j y-axis
	 * @return Boolean tell if a ship is adjacent or not
	 */
	public Boolean adjacentShipCheck(int i, int j) {
		Boolean shipPresence = false;
		int m, n;
		int[] ith = { 0, 1, 1, -1, 0, -1, -1, 1 };
		int[] jth = { 1, 0, 1, 0, -1, -1, 1, -1 };
		for (int k = 0; k < 8; k++) {
			m = i + ith[k];
			n = j + jth[k];
			if (isValid(i + ith[k], j + jth[k])) {
				if (userGrid[m][n] == 1) {
					shipPresence = true;
					break;
				}
			}
		}
		return shipPresence;
	}

	/**
	 * Method to check whether the points are not out of the grid
	 * 
	 * @param i x - axis
	 * @param j y - axis
	 * @return Boolean if the point is valid or not
	 */
	public boolean isValid(int i, int j) {
		if (i < 0 || j < 0 || i >= 9 || j >= 11)
			return false;
		return true;
	}

	/**
	 * Checking that the ships are of the exact size
	 * 
	 * @param diff     size of the ship
	 * @param shipType type of the ship
	 * @return returns String defining if the ships have the correct size
	 */
	public String areHolesValid(int diff, String shipType) {
		if (shipType.equals("Carrier")) {
			if (diff == 5)
				return "YES";
			else
				return "Carriers can only have 5 holes";
		}
		if (shipType.equals("Battleship")) {
			if (diff == 4)
				return "YES";
			else
				return "Battleships can only have 4 holes";
		}
		if (shipType.equals("Cruiser")) {
			if (diff == 3)
				return "YES";
			else
				return "Cruisers can only have 3 holes";
		}
		if (shipType.equals("Submarine")) {
			if (diff == 3)
				return "YES";
			else
				return "Submarines can only have 3 holes";
		}
		if (shipType.equals("Destroyer")) {
			if (diff == 2)
				return "YES";
			else
				return "Destroyers can only have 2 holes";
		}
		return null;
	}

	/**
	 * function to see whether a particular ship is deployed or not
	 * 
	 * @param shipType gives the type of the ship
	 * @return boolean to check the ship deployed
	 */
	public boolean isShipDeployed(String shipType) {

		if (deployedShips.contains(shipType))
			return true;
		else if (deployedShips.isEmpty()) {
			deployedShips.add(shipType);
			return false;
		} else
			deployedShips.add(shipType);

		return false;
	}

	/**
	 * check if all the ships have been deployed or not
	 * 
	 * @return boolean true if all the ships deployed
	 */
	public boolean areAllShipsDeployed() {
		if (deployedShips.size() == 5)
			return true;
		else
			return false;
	}

	/**
	 * Method to set the functionality of feeling lazy
	 */
	public void deployUserRandomShips() {
		try {
			Random rand = new Random();
			int carrierX = rand.nextInt(9);
			int carrierY = rand.nextInt(11);
			List<String> templist = new ArrayList<>();

			HashMap<Integer, Integer> Carrier = new HashMap<>();
			Boolean placed = false;

			Boolean shipPlacementFlag = false;
			while (!placed) {
				if (checkUserShip(carrierX, carrierY, "horizontal", 5)) {
					if ((carrierY + 5) <= 11) {
						shipPlacementFlag = true;
						for (int i = 0; i < 5; i++) {
							Carrier.put((carrierY + i), carrierX);
						}
					} else {
						for (int i = 0; i < 5; i++) {
							Carrier.put((carrierY - i), carrierX);
						}
					}
					placed = true;
				} else {
					carrierX = rand.nextInt(9);
					carrierY = rand.nextInt(11);
				}
			}

			for (Map.Entry<Integer, Integer> entry : Carrier.entrySet()) {
				userGrid[entry.getValue()][entry.getKey()] = 1;
				templist.add(Integer.toString(entry.getValue()) + "," + Integer.toString(entry.getKey()));
				shipsMap.put("Carrier", (ArrayList) templist);

			}

			if (shipPlacementFlag) {
				int[] coords = { carrierY, carrierX, carrierY + 4, (carrierX) };
				setCoords(coords);
			} else {
				int[] coords = { carrierY - 4, carrierX, carrierY, (carrierX) };
				setCoords(coords);
			}

			setShipType("Carrier");
			setAxis("X");

			setChanged();
			notifyObservers("ShipDeploy");

			int battleShipX = rand.nextInt(9);
			int battleShipY = rand.nextInt(11);

			HashMap<Integer, Integer> BattleShip = new HashMap<>();
			templist = new ArrayList<>();
			shipPlacementFlag = false;
			placed = false;
			while (!placed) {
				if (checkUserShip(battleShipX, battleShipY, "vertical", 4)) {
					if ((battleShipX + 4) < 9) {
						shipPlacementFlag = true;
						for (int i = 0; i < 4; i++) {
							BattleShip.put((battleShipX + i), battleShipY);
						}
					} else {
						for (int i = 0; i < 4; i++) {
							BattleShip.put((battleShipX - i), battleShipY);
						}
					}
					placed = true;
				} else {
					battleShipX = rand.nextInt(9);
					battleShipY = rand.nextInt(11);
				}
			}

			for (Map.Entry<Integer, Integer> entry : BattleShip.entrySet()) {

				userGrid[entry.getKey()][entry.getValue()] = 1;
				templist.add(Integer.toString(entry.getKey()) + "," + Integer.toString(entry.getValue()));
				shipsMap.put("Battleship", (ArrayList) templist);

			}
			if (shipPlacementFlag) {
				int[] coords = { battleShipY, battleShipX, (battleShipY), battleShipX + 3 };
				setCoords(coords);
			} else {

				int[] coords = { battleShipY, battleShipX - 3, (battleShipY), battleShipX };
				setCoords(coords);
			}

			setShipType("Battleship");
			setAxis("Y");

			setChanged();
			notifyObservers("ShipDeploy");

			int cruiserX = rand.nextInt(9);
			int cruiserY = rand.nextInt(11);

			HashMap<Integer, Integer> Cruiser = new HashMap<>();
			templist = new ArrayList<>();
			shipPlacementFlag = false;
			placed = false;
			while (!placed) {
				if (checkUserShip(cruiserX, cruiserY, "vertical", 3)) {
					if ((cruiserX + 3) < 9) {
						shipPlacementFlag = true;
						for (int i = 0; i < 3; i++) {
							Cruiser.put((cruiserX + i), cruiserY);
						}
					} else {
						for (int i = 0; i < 3; i++) {
							Cruiser.put((cruiserX - i), cruiserY);
						}
					}
					placed = true;
				} else {
					cruiserX = rand.nextInt(9);
					cruiserY = rand.nextInt(11);
				}
			}

			for (Map.Entry<Integer, Integer> entry : Cruiser.entrySet()) {

				userGrid[entry.getKey()][entry.getValue()] = 1;
				templist.add(Integer.toString(entry.getKey()) + "," + Integer.toString(entry.getValue()));
				shipsMap.put("Cruiser", (ArrayList) templist);

			}

			if (shipPlacementFlag) {
				int[] coords = { cruiserY, cruiserX, (cruiserY), cruiserX + 2 };
				setCoords(coords);
			} else {
				int[] coords = { cruiserY, cruiserX - 2, (cruiserY), cruiserX };
				setCoords(coords);
			}

			setShipType("Cruiser");
			setAxis("Y");

			setChanged();
			notifyObservers("ShipDeploy");

			int subX = rand.nextInt(9);
			int subY = rand.nextInt(11);
			HashMap<Integer, Integer> Submarine = new HashMap<>();
			templist = new ArrayList<>();
			shipPlacementFlag = false;
			placed = false;
			while (!placed) {
				if (checkUserShip(subX, subY, "vertical", 3)) {
					if ((subX + 3) < 9) {
						shipPlacementFlag = true;
						for (int i = 0; i < 3; i++) {
							Submarine.put((subX + i), subY);
						}
					} else {
						for (int i = 0; i < 3; i++) {
							Submarine.put((subX - i), subY);
						}
					}
					placed = true;
				} else {
					subX = rand.nextInt(9);
					subY = rand.nextInt(11);
				}
			}
			for (Map.Entry<Integer, Integer> entry : Submarine.entrySet()) {

				userGrid[entry.getKey()][entry.getValue()] = 1;
				templist.add(Integer.toString(entry.getKey()) + "," + Integer.toString(entry.getValue()));
				shipsMap.put("Submarine", (ArrayList) templist);

			}
			if (shipPlacementFlag) {
				int[] coords = { subY, subX, (subY), subX + 2 };
				setCoords(coords);

			} else {

				int[] coords = { subY, subX - 2, (subY), subX };
				setCoords(coords);

			}

			setShipType("Submarine");
			setAxis("Y");

			setChanged();
			notifyObservers("ShipDeploy");

			int destroyerX = rand.nextInt(9);
			int destroyerY = rand.nextInt(11);

			HashMap<Integer, Integer> Destroyer = new HashMap<>();
			templist = new ArrayList<>();
			shipPlacementFlag = false;
			placed = false;
			while (!placed) {
				if (checkUserShip(destroyerX, destroyerY, "horizontal", 2)) {
					if ((destroyerY + 2) < 11) {
						shipPlacementFlag = true;
						for (int i = 0; i < 2; i++) {
							Destroyer.put((destroyerY + i), destroyerX);
						}
					} else {
						for (int i = 0; i < 2; i++) {
							Destroyer.put((destroyerY - i), destroyerX);
						}
					}
					placed = true;
				} else {
					destroyerX = rand.nextInt(9);
					destroyerY = rand.nextInt(11);
				}
			}

			for (Map.Entry<Integer, Integer> entry : Destroyer.entrySet()) {

				userGrid[entry.getValue()][entry.getKey()] = 1;
				templist.add(Integer.toString(entry.getValue()) + "," + Integer.toString(entry.getKey()));
				shipsMap.put("Destroyer", (ArrayList) templist);

			}
			if (shipPlacementFlag) {
				int[] coords = { destroyerY, destroyerX, destroyerY + 1, (destroyerX) };
				setCoords(coords);
			} else {

				int[] coords = { destroyerY - 1, destroyerX, destroyerY, (destroyerX) };
				setCoords(coords);
			}

			setShipType("Destroyer");
			setAxis("X");

			setChanged();
			notifyObservers("ShipDeploy");

			numOfShipsDep = 5;

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * method to check the adjacency in user ships
	 * 
	 * @param x         x-coordinate
	 * @param y         y-coordinate
	 * @param direction tell the direction horizontal or vertical
	 * @param points    size of ship points
	 * @return Boolean returns if it can place or not
	 */
	public Boolean checkUserShip(int x, int y, String direction, int points) {
		Boolean canPlace = true;
		if (direction.equals("horizontal")) {
			if ((y + points) < 11) {
				for (int j = 0; j < points; j++) {
					if (userGrid[x][y + j] != 0 || adjacentShipCheck(x, y + j))
						canPlace = false;
				}
			} else {
				for (int j = 0; j < points; j++) {
					if (userGrid[x][y - j] != 0 || adjacentShipCheck(x, y - j))
						canPlace = false;
				}
			}
		} else {

			if ((x + points) < 9) {
				for (int j = 0; j < points; j++) {
					if (userGrid[x + j][y] != 0 || adjacentShipCheck(x + j, y))
						canPlace = false;
				}
			} else {
				for (int j = 0; j < points; j++) {
					if (userGrid[x - j][y] != 0 || adjacentShipCheck(x - j, y))
						canPlace = false;
				}
			}

		}

		return canPlace;

	}

	/**
	 * 
	 * checks the grid of the User to verify if user has won or not displays the Win
	 * case if User won
	 * 
	 */
	public void checkIfCompWon() {
		boolean flagcomp = false;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (userGrid[i][j] == 1) {
					flagcomp = true;
				}
			}
		}

		if (!flagcomp) {
			// set that user has won
			int pscore = Computer.scoringComp;
			int cscore;
			if (Main.gameType.equals("Salvo"))
				cscore = HitStrategySalvo.scoring;
			else
				cscore = HitStrategy.scoring;
			boolean scoreReverse = false;

			if ((cscore > pscore) || sunkenShips.size() == Main.TOTAL_SHIPS) {
				scoreReverse = true;
				System.out.println("Comp Won");
				setCompWon("Won");
			} else {
				setCompWon("Lost");
			}
		}
	}

	/**
	 * Method to display the computer grid
	 * 
	 * @param Grid contains the reference to the grid
	 */
	public void printGrid(Integer[][] Grid) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
			}
		}
	}

	/**
	 * Initialize all the grids to zero
	 * 
	 */
	public void initialize() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				userGrid[i][j] = 0;
			}
		}
	}

	/**
	 * This function checks whether any ships have sunk or not
	 */
	public static void checkSunkenShips() {

		Map<String, ArrayList<String>> tempMap;
		for (String coords : coordinatesHit) {
			tempMap = new HashMap<>();
			tempMap.putAll(shipsMap);
			for (Map.Entry<String, ArrayList<String>> entry : shipsMap.entrySet()) {
				if (!shipsMap.get(entry.getKey()).isEmpty()) {
					// if any ship has been placed on the assigned coordinate
					if (shipsMap.get(entry.getKey()).contains(coords)) {
						tempMap.get(entry.getKey()).remove(coords);

						// if no coordinates are remaining to be hit then add the ship to sunken ships
						// and remove the ships from the shipsMap
						if (shipsMap.get(entry.getKey()).isEmpty()) {
							setSunkenShips(entry.getKey());
							System.out.println("Player sunken ships " + sunkenShips);
							tempMap.remove(entry.getKey());
						}
					}
				}
			}
			shipsMap = new HashMap<>();
			shipsMap.putAll(tempMap);
		}
		if (Main.gameType.equals("Salvo"))
			ShipGrid.salvaAlertCall(sunkenShips);

	}

	/**
	 * Method to launch the server for player-1
	 * 
	 * @param port port number
	 */
	public void launchServer1(int port) {
		System.out.println("launch 1");
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(port);
			while (true) {
				byte[] buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				String msg = new String(request.getData(), 0, request.getLength());
				System.out.println("msg1 " + msg);
				String[] coordinates = msg.split("\\s");
				// case when the coordinates are received
				if (coordinates.length == 1) {
					if (coordinates[0].contains("Name")) {
						Platform.runLater(() -> Main.resultLabel2.setText(coordinates[0].split(":")[1]));
						Platform.runLater(() -> AlertBox.displayError("Connection",
								"Connection Established With " + coordinates[0].split(":")[1]));
						RadarGrid.enableButtons();
						handshake1 = true;
					} else if (coordinates[0].contains("Ready")) {
						Platform.runLater(() -> Main.resultLabel2.setText(coordinates[0].split(":")[1]));
						RadarGrid.enableButtons();
						Platform.runLater(() -> AlertBox.displayError("Connection",
								"Connection Established With " + coordinates[0].split(":")[1]));
						sendReply(6792, "Name:" + saveObj.getuName(), ipAddress2);
						handshake1 = true;
					} else {
						setOtherWon("Won");
					}
				} else if (coordinates.length == 2) {
					RadarGrid.enableButtons();
					int x = Integer.parseInt(coordinates[0]);
					int y = Integer.parseInt(coordinates[1]);
					int xy[] = { x, y };
					String coordx = coordinates[0];
					String coordy = coordinates[1];
					if (userGrid[x][y] == 1) {
						// change the grid value from 1 to 2 to signify hit
						userGrid[x][y] = 2;
						coordinatesHit.add(coordx + "," + coordy);
						if (!time1) {
							timea = java.lang.System.currentTimeMillis();
							time1 = !time1;
						} else if (!time2) {
							timeb = java.lang.System.currentTimeMillis();
							time2 = !time2;
						} else {
							double t = timeb - timea;
							if (t < 3000) {
								// setScore(20);
							}
							time1 = false;
							time2 = false;
							timea = 0;
							timeb = 0;

						}
						setScore(10);
						setCoords(xy);
						checkSunkenShips();
						String playerHealth = Integer.toString(Player.sunkenShips.size());
						System.out.println(Player.PlayerNum + " health drop is is " + playerHealth);
						setShipGridReply("It's a Hit!!!!!");
						// sendReply(6792, "It's a Hit!!!!!", "132.205.94.100");
						sendReply(6792, "It's a Hit!!!!! " + playerHealth, ipAddress2);
						checkPlayerWon();
					} else if (userGrid[x][y] == 0) {
						userGrid[x][y] = 3;
						checkSunkenShips();
						String playerHealth = Integer.toString(Player.sunkenShips.size());
						System.out.println(Player.PlayerNum + " health drop is is " + playerHealth);
						setCoords(xy);
						setScore(-1);
						setShipGridReply("It's a miss!!!!! ");
						// sendReply(6792, "It's a miss!!!!!", "132.205.94.100");
						sendReply(6792, "It's a miss!!!!! " + playerHealth, ipAddress2);
					} else if (userGrid[x][y] == 2 || userGrid[x][y] == 3) {
						// sendReply(6792, "The location has been hit earlier", "132.205.94.100");
						sendReply(6792, "The location has been hit earlier", ipAddress2);
					} else {
						// sendReply(6792, "Some other error", "132.205.94.100");
						sendReply(6792, "Some other error", ipAddress2);
					}
				} else if (coordinates.length > 2) {
					// case when some message is received
					// set the message in the right getter and setter
					setReply(msg);
				}
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());

		}
	}

	/**
	 * Method to launch the server for player-2
	 * 
	 * @param port wrong port number for exception
	 */
	public void launchServer2(int port) {
		DatagramSocket aSocket = null;

		try {

			aSocket = new DatagramSocket(port);
			while (true) {
				byte[] buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				String msg = new String(request.getData(), 0, request.getLength());
				String[] coordinates = msg.split("\\s");
				// case when the coordinates are received
				if (coordinates.length == 1) {
					if (coordinates[0].contains("Name")) {
						Platform.runLater(() -> Main.resultLabel1.setText(coordinates[0].split(":")[1]));
						Platform.runLater(() -> AlertBox.displayError("Connection",
								"Connection Established With " + coordinates[0].split(":")[1]));
						RadarGrid.enableButtons();
						handshake2 = true;
					} else if (coordinates[0].contains("Ready")) {
						Platform.runLater(() -> Main.resultLabel1.setText(coordinates[0].split(":")[1]));
						RadarGrid.enableButtons();
						Platform.runLater(() -> AlertBox.displayError("Connection",
								"Connection Established With " + coordinates[0].split(":")[1]));
						// sendReply(6795, "Connected", "127.0.0.1");
						sendReply(6795, "Name:" + saveObj.getuName(), ipAddress1);
						handshake2 = true;
					} else {
						setOtherWon("Won");
					}
				} else if (coordinates.length == 2) {
					System.out.println(coordinates);
					RadarGrid.enableButtons();
					int x = Integer.parseInt(coordinates[0]);
					int y = Integer.parseInt(coordinates[1]);
					int xy[] = { x, y };
					String coordx = coordinates[0];
					String coordy = coordinates[1];
					if (userGrid[x][y] == 1) {
						// change the grid value from 1 to 2 to signify hit
						userGrid[x][y] = 2;
						coordinatesHit.add(coordx + "," + coordy);
						if (!time1) {
							timea = java.lang.System.currentTimeMillis();
							time1 = !time1;
						} else if (!time2) {
							timeb = java.lang.System.currentTimeMillis();
							time2 = !time2;
						} else {
							double t = timeb - timea;
							if (t < 3000) {
								// setScore(20);
							}
							time1 = false;
							time2 = false;
							timea = 0;
							timeb = 0;

						}
						setScore(10);
						setCoords(xy);
						checkSunkenShips();
						String playerHealth = Integer.toString(Player.sunkenShips.size());
						System.out.println(Player.PlayerNum + " health drop is is " + playerHealth);
						setShipGridReply("It's a Hit!!!!!");
						// sendReply(6795, "It's a Hit!!!!!", "132.205.94.99");
						sendReply(6795, "It's a Hit!!!!! " + playerHealth, ipAddress1);
						checkPlayerWon();
					} else if (userGrid[x][y] == 0) {
						userGrid[x][y] = 3;
						setScore(-1);
						setCoords(xy);
						checkSunkenShips();
						String playerHealth = Integer.toString(Player.sunkenShips.size());
						System.out.println(Player.PlayerNum + " health drop is is " + playerHealth);
						setShipGridReply("It's a miss!!!!!");
						// sendReply(6795, "It's a miss!!!!!", "132.205.94.99");
						sendReply(6795, "It's a miss!!!!! " + playerHealth, ipAddress1);
					} else if (userGrid[x][y] == 2) {
						// sendReply(6795, "The location has been hit earlier", "132.205.94.99");
						sendReply(6795, "The location has been hit earlier", ipAddress1);
					} else {
						// sendReply(6795, "Some other error", "132.205.94.99");
						sendReply(6795, "Some other error", ipAddress1);
					}
				} else if (coordinates.length > 2) {
					// case when some message is received
					// set the message in the right getter and setter
					setReply(msg);
				}
			}

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());

		}

	}

	/**
	 * This method check the build 3 requirement for managed exceptions
	 * 
	 * @param port This parameter receives the port number to be called
	 */
	public void managedExceptionHandling(int port) {
		try {
			if (PlayerNum == 1) {
				if (port != 6795) {
					port = 6795;
					Runnable task = () -> {
						launchServer1(6795);
					};
					new Thread(task).start();
					Runnable task2 = () -> {
						handshake();
					};
					new Thread(task2).start();
					throw new PortException("Port error! Correct Port: " + port);
				}
			} else {
				if (port != 6792) {
					port = 6792;
					Runnable task = () -> {
						launchServer2(6792);
					};
					new Thread(task).start();
					Runnable task2 = () -> {
						handshake();
					};
					new Thread(task2).start();
					throw new PortException("Port error! Correct Port: " + port);
				}
			}
		} catch (

		PortException e) {
			System.out.println(e);
		}
	}

	/**
	 * Method that is called when the Vs player network mode is chosen
	 * 
	 * @param playerNum 1 or 2
	 */
	public void PlayerMode(int playerNum) {
		setPlayerNum(playerNum);
		managedExceptionHandling(1111);
		/*
		 * try { setPlayerNum(playerNum); managedExceptionHandling(1111); //int port;
		 */
		// setPlayerNum(playerNum);
		// throw new PortException("Sorry wrong port number!" + port);
		// port = 2.2;
		/*
		 * } catch (PortException e) { managedExceptionHandling(1111);
		 * System.out.println(e); } catch (Exception e) { // TODO: handle exception
		 * System.out.println(e.getMessage()); }
		 */
	}

	/**
	 * This method is made to sending notifications to the other server once it get
	 * started.
	 */
	public void handshake() {
		try {
			if (getPlayerNum() == 1) {
				while (!handshake1) {
					// sendReply(6792, "The location has been hit earlier", "132.205.94.100");
					sendReply(6792, "Ready:" + saveObj.getuName(), ipAddress2);
					TimeUnit.SECONDS.sleep(1);
					System.out.println("Ready");
				}
			} else {
				while (!handshake2) {
					// sendReply(6795, "The location has been hit earlier", "132.205.94.99");
					sendReply(6795, "Ready:" + saveObj.getuName(), ipAddress1);
					TimeUnit.SECONDS.sleep(1);
					System.out.println("Ready2");
				}
			}
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Front end will call this method whenever there is PLayer 1 hits player 2
	 * 
	 * @param x row-coordinate of the grid
	 * @param y column-coordinate of the grid
	 */
	public void Player1Hit2Send(int x, int y) {

		hitX = x;
		hitY = y;

		String rply = x + " " + y;
		byte[] bytesSend = null;
		DatagramSocket aSocket = null;
		try {
			bytesSend = rply.getBytes();
			aSocket = new DatagramSocket();

			// InetAddress aHost_soen = InetAddress.getByName("132.205.94.100");
			InetAddress aHost_soen = InetAddress.getByName("127.0.0.1");
			int player2Port = 6792;

			DatagramPacket request_PLayer1 = new DatagramPacket(bytesSend, rply.length(), aHost_soen, player2Port);
			System.out.println("Message sent");
			aSocket.send(request_PLayer1);
			RadarGrid.disableButtons();
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally

		{
			{
				if (aSocket != null)
					aSocket.close();

			}
		}

	}

	/**
	 * Front end will call this method whenever Player 2 hits player 1
	 * 
	 * @param x row-coordinate of the grid
	 * @param y column-coordinate of the grid
	 */
	public void Player2Hit1Send(int x, int y) {

		hitX = x;
		hitY = y;
		String rply = x + " " + y;
		byte[] bytesSend = null;
		DatagramSocket aSocket = null;
		try {
			bytesSend = rply.getBytes();
			aSocket = new DatagramSocket();

			// InetAddress aHost_soen = InetAddress.getByName("132.205.94.99");
			InetAddress aHost_soen = InetAddress.getByName("127.0.0.1");
			int player1Port = 6795;

			DatagramPacket request_PLayer2 = new DatagramPacket(bytesSend, rply.length(), aHost_soen, player1Port);
			aSocket.send(request_PLayer2);
			RadarGrid.disableButtons();
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}

	/**
	 * Method to send the reply to the other player
	 * 
	 * @param port    port number that is required
	 * @param msg     message to be sent
	 * @param address address of the player
	 */
	public void sendReply(int port, String msg, String address) {
		String rply = msg;
		byte[] bytesSend = null;
		DatagramSocket aSocket = null;
		try {
			bytesSend = rply.getBytes();
			aSocket = new DatagramSocket();
			InetAddress aHost_soen = InetAddress.getByName(address);
			int player1Port = port;
			DatagramPacket repy_PLayer2or1 = new DatagramPacket(bytesSend, rply.length(), aHost_soen, player1Port);
			aSocket.send(repy_PLayer2or1);
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}

	/**
	 * Method to check if the player won
	 */
	public void checkPlayerWon() {

		boolean flag = false;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (userGrid[i][j] == 1) {
					flag = true;
				}
			}
		}
		if (!flag) {
			setPlayerWon("Lost");
		} else { // do nothing
			setPlayerWon("Won");
		}
		if (getPlayerWon().equals("Lost")) {
			if (PlayerNum == 1) {// send msg to player 2 that player 1 has won

				// sendReply(6792, "Won", "132.205.94.100");
				sendReply(6792, "Won", "127.0.0.1");
			} else {
				// player 2 case
				// send msg to player 1 that player 2 has won
				// sendReply(6795, "Won", "132.205.94.99");
				sendReply(6795, "Won", "127.0.0.1");

			}
		}

	}

}
