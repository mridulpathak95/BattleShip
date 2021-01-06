package application.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Random;

import application.Exception.LocationHitException;
import application.Exception.NegativeScore;

/**
 * Class to implement the easy, medium and hard functionality
 * @author arsal
 *
 */
public class HitStrategy extends Observable {
	final int rows = 9;
	final int cols = 11;
	Integer[][] randomGrid = new Integer[9][11];
	Integer[][] probabilityGrid = new Integer[9][11];
	ArrayList<String> hitFound = new ArrayList<>();
	Random ran = new Random();
	String direction = "";
	int hitX, hitY;
	int counter = 0;
	int minMax = 0;
	public static int scoring = 0;
	public static int scoringComp = 0;
	private String reply = "";
	private int[] coords = {};
	public static int buttonCount = 0;
	boolean time1=false,time2=false;
	double  timea=0;
	double timeb=0;

	public HitStrategy() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 11; j++) {
				probabilityGrid[i][j] = 0;
			}
		}
		probabiltDistribution();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 11; j++) {
			}
		}
	}

	public int[] getCoords() {
		return coords;
	}

	public void setCoords(int[] coords) {
		this.coords = coords;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
		setChanged();
		notifyObservers("userHitorMiss");
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
	
	/**
	 * method to implement the easy functionality of the game
	 */
	public void randomHit() throws LocationHitException{
		// Random ran = new Random();
		int x = randomX();
		int y = randomY();

		boolean newHit = false;
		while (!newHit) {
			if (Player.userGrid[x][y] == 2) {
				x = ran.nextInt(9);
				y = ran.nextInt(11);
			} else
				newHit = true;
		}
		int hitCoord[] = { x, y };
		setCoords(hitCoord);
		String coordx = Integer.toString(x);
		String coordy = Integer.toString(y);
		if (Player.userGrid[x][y] == 1) {
			// change the grid value from 1 to 2 to signify hit
			Player.userGrid[x][y] = 2;
			
			if(!time1) {
				timea=java.lang.System.currentTimeMillis();
				time1=!time1;
			}else if(!time2) {
				timeb=java.lang.System.currentTimeMillis();
					time2=!time2;
			}else {
				double t=timeb-timea;
				if(t<3000) {
					setScore(20);
				}
				time1=false;
				time2=false;
				timea=0;
				timeb=0;
				
			}//if time between consecutive hit is less than 3 seconds ,then bonus score
			setScore(10);
			Player.coordinatesHit.add(coordx + "," + coordy);
			setReply("It's a Hit!!!!!");

		} else if (Player.userGrid[x][y] == 0) {
			Player.userGrid[x][y] = 3;
			setScore(-1);
			setReply("It's a miss!!!!!");
		} else if (Player.userGrid[x][y] == 2 || Player.userGrid[x][y] == 3) {
			setReply("The location has been hit earlier");
			 //throw new LocationHitException("The location has been hit earlier");
		}
	}
	
	/**
	 * method to implement the medium functionality of the game
	 * @param hitResult previous hit or miss
	 */
	public void mediumMode(Boolean hitResult) throws LocationHitException{
		int x = ran.nextInt(9);
		int y = ran.nextInt(11);

		if (!hitResult && direction.isEmpty()) {
			boolean newHit = false;
			while (!newHit) {
				if (Player.userGrid[x][y] == 2) {
					x = ran.nextInt(9);
					y = ran.nextInt(11);
				} else
					newHit = true;
			}
		} else if (direction.isEmpty()) {
			hitFound.add(hitX + ";" + hitY);
			counter++;
			if ((hitX + 1) < 9) {
				x = hitX + 1;
				y = hitY;
				direction = "HorizontalFront";
			} else {
				x = hitX - 1;
				y = hitY;
				direction = "HorizontalBack";
			}
		} else {
			if (direction.equals("HorizontalFront")) {
				int tempX = Integer.parseInt(hitFound.get(0).split(";")[0]);
				int tempY = Integer.parseInt(hitFound.get(0).split(";")[1]);
				if (hitResult && (hitX + 1 < 9)) {
					counter++;
					x = hitX + 1;
					y = hitY;
				} else if (tempX > 0) {
					x = tempX - 1;
					y = tempY;
					direction = "HorizontalBack";
				} else if ((Integer.parseInt(hitFound.get(0).split(";")[1]) + 1 < 11)) {
					x = Integer.parseInt(hitFound.get(0).split(";")[0]);
					y = Integer.parseInt(hitFound.get(0).split(";")[1]) + 1;
					direction = "VerticalFront";
				} else {
					x = Integer.parseInt(hitFound.get(0).split(";")[0]);
					y = Integer.parseInt(hitFound.get(0).split(";")[1]) - 1;
					direction = "VerticalBack";
				}
			} else if (direction.equals("HorizontalBack")) {
				if (hitResult && (hitX > 0)) {
					counter++;
					x = hitX - 1;
					y = hitY;
				} else if (Integer.parseInt(hitFound.get(0).split(";")[1]) + 1 < 11 && counter < 2) {
					x = Integer.parseInt(hitFound.get(0).split(";")[0]);
					y = Integer.parseInt(hitFound.get(0).split(";")[1]) + 1;
					direction = "VerticalFront";
				} else if (counter < 2) {
					x = Integer.parseInt(hitFound.get(0).split(";")[0]);
					y = Integer.parseInt(hitFound.get(0).split(";")[1]) - 1;
					direction = "VerticalBack";
				} else {
					x = ran.nextInt(9);
					y = ran.nextInt(11);
					direction = "";
					hitFound.clear();
					counter = 0;
				}
			} else if (direction.equals("VerticalFront") && counter < 2) {
				if (hitResult && (hitY + 1 < 11)) {
					x = hitX;
					y = hitY + 1;
				} else if (Integer.parseInt(hitFound.get(0).split(";")[1]) > 0) {
					x = Integer.parseInt(hitFound.get(0).split(";")[0]);
					y = Integer.parseInt(hitFound.get(0).split(";")[1]) - 1;
					direction = "VerticalBack";
				} else {
					x = ran.nextInt(9);
					y = ran.nextInt(11);
					direction = "";
					hitFound.clear();
					counter = 0;
				}
			} else {
				if (hitResult && (hitY > 0) && counter < 2) {
					x = hitX;
					y = hitY - 1;
				} else {
					x = ran.nextInt(9);
					y = ran.nextInt(11);
					direction = "";
					hitFound.clear();
					counter = 0;
				}
			}
		}

		hitX = x;
		hitY = y;
		String coordx = Integer.toString(x);
		String coordy = Integer.toString(y);
		int hitCoord[] = { x, y };
		setCoords(hitCoord);
		if (Player.userGrid[x][y] == 1) {
			// change the grid value from 1 to 2 to signify hit
			Player.userGrid[x][y] = 2;
			if(!time1) {
				timea=java.lang.System.currentTimeMillis();
				time1=!time1;
			}else if(!time2) {
				timeb=java.lang.System.currentTimeMillis();
					time2=!time2;
			}else {
				double t=timeb-timea;
				if(t<3000) {
					setScore(20);
				}
				time1=false;
				time2=false;
				timea=0;
				timeb=0;
				
			}//if time between consecutive hit is less than 3 seconds ,then bonus score
			setScore(10);
			Player.coordinatesHit.add(coordx + "," + coordy);
			setReply("It's a Hit!!!!!");
		} else if (Player.userGrid[x][y] == 0) {
			Player.userGrid[x][y] = 3;
			setScore(-1);
			setReply("It's a miss!!!!!");
		} else if (Player.userGrid[x][y] == 2 || Player.userGrid[x][y] == 3) {
			setReply("The location has been hit earlier");
			 //throw new LocationHitException("The location has been hit earlier");
		}
	}
	
	/**
	 * method to implement the functionality of the hard mode
	 * 
	 * @param hitResult tell the result the for the last hit 
	 */
	public void hardMode(Boolean hitResult) throws LocationHitException{
		int x = 0, y = 0;
		int mostProbable;

		if (minMax == 0) {
			mostProbable = 1;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 11; j++) {
					if (probabilityGrid[i][j] > mostProbable) {
						mostProbable = probabilityGrid[i][j];
						x = i;
						y = j;
					}
				}
			}
			minMax = 1;
		} else {
			mostProbable = 1000;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 11; j++) {
					if (probabilityGrid[i][j] < mostProbable && probabilityGrid[i][j] > 0) {
						mostProbable = probabilityGrid[i][j];
						x = i;
						y = j;
					}
				}
			}
			minMax = 0;
		}

		if (!hitResult && direction.isEmpty()) {
			boolean newHit = false;
			while (!newHit) {
				if (Player.userGrid[x][y] == 2 || Player.userGrid[x][y] == 3) {
					probabilityGrid[x][y] = -1;
					if (minMax == 0) {
						for (int i = 0; i < 9; i++) {
							for (int j = 0; j < 11; j++) {
								if (probabilityGrid[i][j] > mostProbable) {
									mostProbable = probabilityGrid[i][j];
									x = i;
									y = j;
								}
							}
						}
						minMax = 1;
					} else {

						for (int i = 0; i < 9; i++) {
							for (int j = 0; j < 11; j++) {
								if (probabilityGrid[i][j] < mostProbable && probabilityGrid[i][j] > 0) {
									mostProbable = probabilityGrid[i][j];
									x = i;
									y = j;

								}
							}
						}
						minMax = 0;
					}

				} else
					newHit = true;
			}
		} else if (direction.isEmpty()) {
			hitFound.add(hitX + ";" + hitY);
			counter++;
			if ((hitX + 1) < 9) {
				x = hitX + 1;
				y = hitY;
				direction = "HorizontalFront";
			} else {
				x = hitX - 1;
				y = hitY;
				direction = "HorizontalBack";
			}
		} else {
			if (direction.equals("HorizontalFront")) {
				int tempX = Integer.parseInt(hitFound.get(0).split(";")[0]);
				int tempY = Integer.parseInt(hitFound.get(0).split(";")[1]);
				if (hitResult && (hitX + 1 < 9)) {
					counter++;
					x = hitX + 1;
					y = hitY;
				} else if (tempX > 0) {
					x = tempX - 1;
					y = tempY;
					direction = "HorizontalBack";
				} else if ((Integer.parseInt(hitFound.get(0).split(";")[1]) + 1 < 11)) {
					x = Integer.parseInt(hitFound.get(0).split(";")[0]);
					y = Integer.parseInt(hitFound.get(0).split(";")[1]) + 1;
					direction = "VerticalFront";
				} else {
					x = Integer.parseInt(hitFound.get(0).split(";")[0]);
					y = Integer.parseInt(hitFound.get(0).split(";")[1]) - 1;
					direction = "VerticalBack";
				}
			} else if (direction.equals("HorizontalBack")) {
				if (hitResult && (hitX > 0)) {
					counter++;
					x = hitX - 1;
					y = hitY;
				} else if (Integer.parseInt(hitFound.get(0).split(";")[1]) + 1 < 11 && counter < 2) {
					x = Integer.parseInt(hitFound.get(0).split(";")[0]);
					y = Integer.parseInt(hitFound.get(0).split(";")[1]) + 1;
					direction = "VerticalFront";
				} else if (counter < 2) {
					x = Integer.parseInt(hitFound.get(0).split(";")[0]);
					y = Integer.parseInt(hitFound.get(0).split(";")[1]) - 1;
					direction = "VerticalBack";
				} else {
					if (minMax == 0) {
						for (int i = 0; i < 9; i++) {
							for (int j = 0; j < 11; j++) {
								if (probabilityGrid[i][j] > mostProbable) {
									mostProbable = probabilityGrid[i][j];
									x = i;
									y = j;
								}
							}
						}
						minMax = 1;
					} else {

						for (int i = 0; i < 9; i++) {
							for (int j = 0; j < 11; j++) {
								if (probabilityGrid[i][j] < mostProbable && probabilityGrid[i][j] > 0) {
									mostProbable = probabilityGrid[i][j];
									x = i;
									y = j;

								}
							}
						}
						minMax = 0;
					}
					direction = "";
					hitFound.clear();
					counter = 0;
				}
			} else if (direction.equals("VerticalFront") && counter < 2) {
				if (hitResult && (hitY + 1 < 11)) {
					x = hitX;
					y = hitY + 1;
				} else if (Integer.parseInt(hitFound.get(0).split(";")[1]) > 0) {
					x = Integer.parseInt(hitFound.get(0).split(";")[0]);
					y = Integer.parseInt(hitFound.get(0).split(";")[1]) - 1;
					direction = "VerticalBack";
				} else {
					if (minMax == 0) {
						for (int i = 0; i < 9; i++) {
							for (int j = 0; j < 11; j++) {
								if (probabilityGrid[i][j] > mostProbable) {
									mostProbable = probabilityGrid[i][j];
									x = i;
									y = j;
								}
							}
						}
						minMax = 1;
					} else {

						for (int i = 0; i < 9; i++) {
							for (int j = 0; j < 11; j++) {
								if (probabilityGrid[i][j] < mostProbable && probabilityGrid[i][j] > 0) {
									mostProbable = probabilityGrid[i][j];
									x = i;
									y = j;

								}
							}
						}
						minMax = 0;
					}
					direction = "";
					hitFound.clear();
					counter = 0;
				}
			} else {
				if (hitResult && (hitY > 0) && counter < 2) {
					x = hitX;
					y = hitY - 1;
				} else {
					if (minMax == 0) {
						for (int i = 0; i < 9; i++) {
							for (int j = 0; j < 11; j++) {
								if (probabilityGrid[i][j] > mostProbable) {
									mostProbable = probabilityGrid[i][j];
									x = i;
									y = j;
								}
							}
						}
						minMax = 1;
					} else {

						for (int i = 0; i < 9; i++) {
							for (int j = 0; j < 11; j++) {
								if (probabilityGrid[i][j] < mostProbable && probabilityGrid[i][j] > 0) {
									mostProbable = probabilityGrid[i][j];
									x = i;
									y = j;

								}
							}
						}
						minMax = 0;
					}
					direction = "";
					hitFound.clear();
					counter = 0;
				}
			}
		}

		probabilityGrid[x][y] = -1;
		hitX = x;
		hitY = y;

		int hitCoord[] = { x, y };
		setCoords(hitCoord);

		if (Player.userGrid[x][y] == 1) {
			// change the grid value from 1 to 2 to signify hit
			Player.userGrid[x][y] = 2;
			String coordx = Integer.toString(x);
			String coordy = Integer.toString(y);
			if(!time1) {
				timea=java.lang.System.currentTimeMillis();
				time1=!time1;
			}else if(!time2) {
				timeb=java.lang.System.currentTimeMillis();
					time2=!time2;
			}else {
				double t=timeb-timea;
				if(t<3000) {
					setScore(20);
				}
				time1=false;
				time2=false;
				timea=0;
				timeb=0;
				
			}//if time between consecutive hit is less than 3 seconds ,then bonus score
			setScore(10);
			Player.coordinatesHit.add(coordx + "," + coordy);
			setReply("It's a Hit!!!!!");
		} else if (Player.userGrid[x][y] == 0) {
			Player.userGrid[x][y] = 3;
			setScore(-1);
			setReply("It's a miss!!!!!");
		} else if (Player.userGrid[x][y] == 2 || Player.userGrid[x][y] == 3) {
			setReply("The location has been hit earlier");
			//throw new LocationHitException("The location has been hit earlier");

		}

	}
	
	/**
	 * method to check the energy/probability for a particular coordinate to be
	 * ship coordinate 
	 */
	public void probabiltDistribution() {
		for (int k = 0; k < 1000; k++) {
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 11; j++) {
					randomGrid[i][j] = 0;
				}
			}
			deployComputerShips();
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 11; j++) {
					probabilityGrid[i][j] = probabilityGrid[i][j] + randomGrid[i][j];
				}
			}
		}
	}
	
	
	/**
	 * method to deploy computer ships
	 */
	public void deployComputerShips() {

		try {
			Random rand = new Random();
			int carrierX = rand.nextInt(9);
			int carrierY = rand.nextInt(11);

			HashMap<Integer, Integer> Carrier = new HashMap<>();
			Boolean placed = false;

			while (!placed) {
				if (check(carrierX, carrierY, "horizontal", 5)) {
					if ((carrierY + 5) <= 11) {

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
				randomGrid[entry.getValue()][entry.getKey()] = 1;
			}

			int battleShipX = rand.nextInt(9);
			int battleShipY = rand.nextInt(11);

			HashMap<Integer, Integer> BattleShip = new HashMap<>();

			placed = false;
			while (!placed) {
				if (check(battleShipX, battleShipY, "vertical", 4)) {
					if ((battleShipX + 4) < 9) {

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

				randomGrid[entry.getKey()][entry.getValue()] = 1;
		
			}

			int cruiserX = rand.nextInt(9);
			int cruiserY = rand.nextInt(11);

			HashMap<Integer, Integer> Cruiser = new HashMap<>();
			placed = false;
			while (!placed) {
				if (check(cruiserX, cruiserY, "vertical", 3)) {
					if ((cruiserX + 3) < 9) {

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

				randomGrid[entry.getKey()][entry.getValue()] = 1;
		
			}

			int subX = rand.nextInt(9);
			int subY = rand.nextInt(11);
			HashMap<Integer, Integer> Submarine = new HashMap<>();

			placed = false;
			while (!placed) {
				if (check(subX, subY, "vertical", 3)) {
					if ((subX + 3) < 9) {

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

				randomGrid[entry.getKey()][entry.getValue()] = 1;
		
			}

			int destroyerX = rand.nextInt(9);
			int destroyerY = rand.nextInt(11);

			HashMap<Integer, Integer> Destroyer = new HashMap<>();

			placed = false;
			while (!placed) {
				if (check(destroyerX, destroyerY, "horizontal", 2)) {
					if ((destroyerY + 2) < 11) {

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

				randomGrid[entry.getValue()][entry.getKey()] = 1;

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}
	
	/**
	 * method to check the validity of points
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param direction horizontal or vertical direction
	 * @param points neighboring points 
	 * @return Boolean to tell if the ship can be placed or not
	 */
	public Boolean check(int x, int y, String direction, int points) {
		Boolean canPlace = true;
		if (direction.equals("horizontal")) {
			if ((y + points) < 11) {
				for (int j = 0; j < points; j++) {
					if (randomGrid[x][y + j] != 0 || adjacentShipCheck(x, y + j))
						canPlace = false;
				}
			} else {
				for (int j = 0; j < points; j++) {
					if (randomGrid[x][y - j] != 0 || adjacentShipCheck(x, y - j))
						canPlace = false;
				}
			}

		} else {

			if ((x + points) < 9) {
				for (int j = 0; j < points; j++) {
					if (randomGrid[x + j][y] != 0 || adjacentShipCheck(x + j, y))
						canPlace = false;
				}
			} else {
				for (int j = 0; j < points; j++) {
					if (randomGrid[x - j][y] != 0 || adjacentShipCheck(x - j, y))
						canPlace = false;
				}
			}

		}

		return canPlace;

	}
	
	/**
	 * method to check the adjacency of ships
	 * @param i x-coord
	 * @param j y-coord 
	 * @return Boolean to tell if adjacent ship is present or not
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
				if (randomGrid[m][n] == 1) {
					shipPresence = true;
					break;
				}
			}
		}
		return shipPresence;
	}
	
	/**
	 * method to check whether the points are not outside the grid
	 * @param i x-coord
	 * @param j y-coord
	 * @return Boolean to tell is the point is valid or not
	 */
	public boolean isValid(int i, int j) {
		if (i < 0 || j < 0 || i >= 9 || j >= 11)
			return false;
		return true;
	}
	

	/**
	 * generates random x-coordinate
	 * @return int the random x value
	 */
	public int randomX() {
		return ran.nextInt(9);
	}
	
	/**
	 * generates random y-coordinate
	 * @return int random y value
	 */
	public int randomY() {

		return ran.nextInt(11);

	}

}
