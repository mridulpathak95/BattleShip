package application.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Random;

import application.Exception.LocationHitException;
import application.Exception.NegativeScore;
import application.Views.AlertBox;

/**
 * Class for saving the states of Computer grid
 * @author Sagar Bhatia
 *
 */
public class Computer extends Observable {

	final static int rows = 9;
	final static  int cols = 11;
	boolean time1 = false, time2 = false;
	double timea = 0;
	double timeb = 0;
	Random rand = new Random();
	// to check if all ships have been placed or not
	int counter = 0;
	static public Integer[][] computerGrid = new Integer[rows][cols];

	private String UserWon = "";

	public static Map<String, ArrayList<String>> shipsMap = new HashMap<>();
	static ArrayList<String> tempList = new ArrayList<String>();
	public static ArrayList<String> sunkenShips = new ArrayList<String>();
	public static ArrayList<String> coordinatesHit = new ArrayList<String>();
	public static int scoringComp = 0;
	private String reply = "";

	
	public Integer[][] getComputerGrid() {
		return computerGrid;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
		setChanged();
		notifyObservers("HITORMISS");
	}

	
	
	public void setScoreComp(int s) {
		try {
			scoringComp = scoringComp + s;
			if (scoringComp < 0) {
				throw new NegativeScore("Score can't be negative");
			}
		} catch (NegativeScore e) {
			scoringComp = 0;
			System.out.println(e);
		}
	}

	public int getScoreComp() {
		return scoringComp;
	}

	public void setSunkenShips(String shipType) {
		sunkenShips.add(shipType);
	}

	public void setCounter(int counter) {

		this.counter = counter;

	}

	public int getCounter() {
		return this.counter;

	}

	public ArrayList<String> getSunkenShips() {

		return sunkenShips;

	}

	public String getUserWon() {

		return UserWon;
	}

	public void setUserWon(String userWon) {
		this.UserWon = userWon;	
	}

	public Computer() {
		initialize();
	}

	/**
	 * Takes the input based on the event listener Provides the hit or miss while
	 * hitting on the user grid
	 * 
	 * @param x coordinate
	 * @param y coordinates
	 * 
	 */
	public void userTurn(int x, int y)throws LocationHitException {

		// get the X and y coordinate from the input
		String coordx = Integer.toString(x);
		String coordy = Integer.toString(y);
		if (computerGrid[x][y] == 1) {
			// change the grid value from 1 to 2 to signify hit
			computerGrid[x][y] = 2;
			if (!time1) {
				timea = java.lang.System.currentTimeMillis();
				time1 = !time1;
			} else if (!time2) {
				timeb = java.lang.System.currentTimeMillis();
				time2 = !time2;
			} else {
				double t = timeb - timea;
				if (t < 3000) {
					setScoreComp(20);
				}
				time1 = false;
				time2 = false;
				timea = 0;
				timeb = 0;

			}

			setScoreComp(10);
			coordinatesHit.add(coordx + "," + coordy);
			setReply("It's a Hit!!!!!");

		
		} else if (computerGrid[x][y] == 0) {
			computerGrid[x][y] = 3;
			setScoreComp(-1);
			setReply("It's a miss!!!!!");
		
		} else if (computerGrid[x][y] == 2 || computerGrid[x][y] == 3) {

			setReply("The location has been hit earlier");
			 //throw new LocationHitException("The location has been hit earlier");
		}

		else {

			setReply("Some other error");
		}
		
	
	}

	/**
	 * This method places computer ships randomly
	 * 
	 * @author arsalaan
	 */
	public void deployComputerShips() {

		try {

			int carrierX = randomX();
			int carrierY = randomY();

			HashMap<Integer, Integer> Carrier = new HashMap<>();
			Boolean placed = false;

			while (!placed) {

				if (check(carrierX, carrierY, "horizontal", 5)) {

					if ((carrierY + 5) <= 11) {

						for (int i = 0; i < 5; i++) {
							Carrier.put((carrierY + i), carrierX);
							putIntoShipsMap("Carrier", carrierY + i, carrierX, "horizontal");
						}
					} else {
						for (int i = 0; i < 5; i++) {
							Carrier.put((carrierY - i), carrierX);
							putIntoShipsMap("Carrier", carrierY - i, carrierX, "horizontal");
						}
					}
					placed = true;
					counter++;
				} else {
					carrierX = rand.nextInt(9);
					carrierY = rand.nextInt(11);
				}
			}

			for (Map.Entry<Integer, Integer> entry : Carrier.entrySet()) {
				computerGrid[entry.getValue()][entry.getKey()] = 1;
			}

			int battleShipX = rand.nextInt(9);
			int battleShipY = rand.nextInt(11);
			HashMap<Integer, Integer> BattleShip = new HashMap<>();
			tempList = new ArrayList<String>();
			placed = false;
			while (!placed) {
				if (check(battleShipX, battleShipY, "vertical", 4)) {
					if ((battleShipX + 4) < 9) {

						for (int i = 0; i < 4; i++) {
							BattleShip.put((battleShipX + i), battleShipY);
							putIntoShipsMap("Battleship", battleShipX + i, battleShipY, "vertical");
						}
					} else {
						for (int i = 0; i < 4; i++) {
							BattleShip.put((battleShipX - i), battleShipY);
							putIntoShipsMap("Battleship", battleShipX - i, battleShipY, "vertical");
						}
					}
					placed = true;
					counter++;
				} else {
					battleShipX = rand.nextInt(9);
					battleShipY = rand.nextInt(11);
				}
			}

			for (Map.Entry<Integer, Integer> entry : BattleShip.entrySet()) {

				computerGrid[entry.getKey()][entry.getValue()] = 1;

			}

			int cruiserX = rand.nextInt(9);
			int cruiserY = rand.nextInt(11);

			HashMap<Integer, Integer> Cruiser = new HashMap<>();
			tempList = new ArrayList<String>();
			placed = false;
			while (!placed) {
				if (check(cruiserX, cruiserY, "vertical", 3)) {
					if ((cruiserX + 3) < 9) {

						for (int i = 0; i < 3; i++) {
							Cruiser.put((cruiserX + i), cruiserY);
							putIntoShipsMap("Cruiser", cruiserX + i, cruiserY, "vertical");
						}
					} else {
						for (int i = 0; i < 3; i++) {
							Cruiser.put((cruiserX - i), cruiserY);
							putIntoShipsMap("Cruiser", cruiserX - i, cruiserY, "vertical");
						}
					}
					placed = true;
					counter++;
				} else {
					cruiserX = rand.nextInt(9);
					cruiserY = rand.nextInt(11);
				}
			}

			for (Map.Entry<Integer, Integer> entry : Cruiser.entrySet()) {
				computerGrid[entry.getKey()][entry.getValue()] = 1;
	
			}

			int subX = rand.nextInt(9);
			int subY = rand.nextInt(11);
			HashMap<Integer, Integer> Submarine = new HashMap<>();
			tempList = new ArrayList<String>();

			placed = false;
			while (!placed) {
				if (check(subX, subY, "vertical", 3)) {
					if ((subX + 3) < 9) {

						for (int i = 0; i < 3; i++) {
							Submarine.put((subX + i), subY);
							putIntoShipsMap("Submarine", subX + i, subY, "vertical");
						}
					} else {
						for (int i = 0; i < 3; i++) {
							Submarine.put((subX - i), subY);
							putIntoShipsMap("Submarine", subX - i, subY, "vertical");
						}
					}
					placed = true;
					counter++;
				} else {
					subX = rand.nextInt(9);
					subY = rand.nextInt(11);
				}
			}
			for (Map.Entry<Integer, Integer> entry : Submarine.entrySet()) {

				computerGrid[entry.getKey()][entry.getValue()] = 1;
		
			}

			int destroyerX = rand.nextInt(9);
			int destroyerY = rand.nextInt(11);

			HashMap<Integer, Integer> Destroyer = new HashMap<>();
			tempList = new ArrayList<String>();
			placed = false;
			while (!placed) {
				if (check(destroyerX, destroyerY, "horizontal", 2)) {
					if ((destroyerY + 2) < 11) {

						for (int i = 0; i < 2; i++) {
							Destroyer.put((destroyerY + i), destroyerX);
							putIntoShipsMap("Destroyer", destroyerY + i, destroyerX, "horizontal");
						}
					} else {
						for (int i = 0; i < 2; i++) {
							Destroyer.put((destroyerY - i), destroyerX);
							putIntoShipsMap("Destroyer", destroyerY - i, destroyerX, "horizontal");
						}
					}
					placed = true;
					counter++;

					setCounter(counter);
				} else {
					destroyerX = rand.nextInt(9);
					destroyerY = rand.nextInt(11);
				}
			}

			for (Map.Entry<Integer, Integer> entry : Destroyer.entrySet()) {
				computerGrid[entry.getValue()][entry.getKey()] = 1;
	
			}
			
			printGrid();
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}

	}

	/**
	 * This method checks if a ship can be placed over the computer grid.
	 * 
	 * @author arsalaan
	 * 
	 * @param x         coordinate
	 * @param y         coordinate
	 * @param direction describes horizontal or vertical
	 * @param points    limit of movement
	 * @return canPlace tells if ship can be placed or not
	 */
	public Boolean check(int x, int y, String direction, int points) {
		Boolean canPlace = true;
		if (direction.equals("horizontal")) {
			if ((y + points) < 11) {
				for (int j = 0; j < points; j++) {
					if (computerGrid[x][y + j] != 0 || adjacentShipCheck(x, y + j))
						canPlace = false;
				}
			} else {
				for (int j = 0; j < points; j++) {
					if (computerGrid[x][y - j] != 0 || adjacentShipCheck(x, y - j))
						canPlace = false;
				}
			}

		} else {

			if ((x + points) < 9) {
				for (int j = 0; j < points; j++) {
					if (computerGrid[x + j][y] != 0 || adjacentShipCheck(x + j, y))
						canPlace = false;
				}
			} else {
				for (int j = 0; j < points; j++) {
					if (computerGrid[x - j][y] != 0 || adjacentShipCheck(x - j, y))
						canPlace = false;
				}
			}

		}

		return canPlace;

	}
	
	
	/**
	 * method to check the adjacency of ships
	 * @param i x-coordinate
	 * @param j y-coordinate
	 * @return Boolean shipPresence tells is the ship is present or not
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
				if (computerGrid[m][n] == 1) {
					shipPresence = true;
					break;
				}
			}
		}
		return shipPresence;
	}
	
	/**
	 * method to check whether the coordinates are within the grid or not
	 * @param i x-axis coord
	 * @param j y-axis coord
	 * @return Boolean type to tell if the the point is valid or not
	 */
	public boolean isValid(int i, int j) {
		if (i < 0 || j < 0 || i >= 9 || j >= 11)
			return false;
		return true;
	}

	/**
	 * Initialize all the grids to zero
	 * 
	 */
	public void initialize() {

		for (int i = 0; i < rows; i++) {

			for (int j = 0; j < cols; j++) {

				computerGrid[i][j] = 0;

			}
		}

	}
	
	/**
	 * method to check if the user won
	 */
	public void checkIfUserWon() {
		
		boolean flaguser = false;

		// check the computer grid if all the 1 are converted to 2
		for (int i = 0; i < rows; i++) {

			for (int j = 0; j < cols; j++) {

				if (computerGrid[i][j] == 1) {
					flaguser = true;
					break;
				}
				

			}

		}

		if (!flaguser) {
			setUserWon("Won");
		} else {
			setUserWon("Lost");
		
		}

	}

	/**
	 * Method to display the computer grid
	 */
	public void printGrid() {


		for (int i = 0; i < rows; i++) {

			for (int j = 0; j < cols; j++) {
				
				System.out.print(computerGrid[i][j]);

			}
			System.out.println("");
		}

	}

	/**
	 * The method puts the values in ships and coordinates hashmap
	 * 
	 * @param shipType type of ship
	 * @param cY       coordinate X
	 * @param cX       coordinate Y
	 * @param direction tells the direction of the ship vertical or horizontal
	 */
	public void putIntoShipsMap(String shipType, int cY, int cX, String direction) {
		String coordy = new String();
		String coordx = new String();
		if (direction.equals("horizontal")) {
			coordy = Integer.toString(cX);
			coordx = Integer.toString(cY);
		} else {
			coordy = Integer.toString(cY);
			coordx = Integer.toString(cX);
		}
		tempList.add(coordy + "," + coordx);
		shipsMap.put(shipType, tempList);
	}

	/**
	 * This function checks whether any ships have sunk or not
	 * 
	 */
	public void checkSunkenShips() {
		Map<String, ArrayList<String>> tempMap;
		for (String coords : coordinatesHit) {
			tempMap = new HashMap<>();
			tempMap.putAll(shipsMap);
			for (Map.Entry<String, ArrayList<String>> entry : shipsMap.entrySet()) {
				if (!shipsMap.get(entry.getKey()).isEmpty()) {
					if (shipsMap.get(entry.getKey()).contains(coords)) {
						if (!shipsMap.get(entry.getKey()).isEmpty()) {
							if (shipsMap.get(entry.getKey()).contains(coords)) {
								tempMap.get(entry.getKey()).remove(coords);

								if (shipsMap.get(entry.getKey()).isEmpty()) {
									setSunkenShips(entry.getKey());
									tempMap.remove(entry.getKey());

								}
							}

						}

					}
				}
				shipsMap = new HashMap<>();
				shipsMap.putAll(tempMap);
			}
		}

	}
	
	/**
	 * methods to generate random x-coordinate
	 * @return int x-coordinate
	 */
	public int randomX() {

		return rand.nextInt(9);
	}
	
	/**method to generate random y-coordinate
	 * @return int y-coordinate
	 */
	public int randomY() {

		return rand.nextInt(11);

	}

}
