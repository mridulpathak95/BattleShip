package application.Views;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import application.Controllers.GridUser;
import application.Models.Computer;
import application.Models.HitStrategy;
import application.Models.HitStrategySalvo;
import application.Models.LoadClass;
import application.Models.Player;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Main;

/**
 * This is view for User grid and contains its ship placed rendered by the
 * Player.java class.
 */
public class ShipGrid implements Observer {
	static int rowButtonCount;
	static int columnButtonCount;
	public static Button[][] userButton;
	static int noButtonsClicked = 0;
	static int buttonCount = 0;
	int xInitialCo;
	int yInitialCo;
	String compWon = "";
	static String initialCoordinates, finalCoordinates;
	static String gameMode = "Medium";
	public static Boolean lastCompResult = false;
	private GridUser ob;
	Label resulttext1, resulttext4, resulttext3, resulttext2;
	private Player player;
	double otherPlayerHealth;

	/**
	 * It is the constructor for this class
	 * 
	 * @param player      Its a reference for player object
	 * @param ob          Its a reference for Grid user object
	 * @param resulttext1 Its a reference for label for setting user result
	 * @param resulttext4 Its a reference for label for setting user score
	 * @param resulttext3 Its a reference for label for setting computer result
	 * @param resulttext2 Its a reference for label for setting computer score
	 */
	public ShipGrid(Player player, GridUser ob, Label resulttext1, Label resulttext4, Label resulttext3, Label resulttext2) {
		this.player = player;
		this.ob = ob;
		this.resulttext1 = resulttext1;
		this.resulttext4 = resulttext4;
		this.resulttext3 = resulttext3;
		this.resulttext2 = resulttext2;
	}

	
	/**
	 * Deploys user grid on the screen
	 * 
	 * @param g_pane2 This is reference to grid pane containing user ships
	 */
	public void setUserShipGrid(GridPane g_pane2) {
		double r = 12;
		userButton = new Button[9][11];
		Text t = new Text("User Grid");
		t.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 12));
		rowButtonCount = 0;
		columnButtonCount = 0;
		g_pane2.add(t, columnButtonCount, rowButtonCount);
		for (rowButtonCount = 10; rowButtonCount >= 1; rowButtonCount--) {
			int ch = 10;
			Text text1 = new Text(Integer.toString(ch - rowButtonCount));
			g_pane2.add(text1, columnButtonCount, rowButtonCount);
		}
		// initializing the radar grid buttons of 9*11 size
		// so that they can be accessed via ID
		for (rowButtonCount = 0; rowButtonCount < 9; rowButtonCount++) {
			for (columnButtonCount = 0; columnButtonCount < 11; columnButtonCount++) {
				userButton[rowButtonCount][columnButtonCount] = new Button();
			}
		}
		int buttonRowIndex = 0;
		// placing the buttons or holes on the grid
		for (rowButtonCount = 9; rowButtonCount >= 1; rowButtonCount -= 1) {
			columnButtonCount = 1;
			for (Button b : userButton[buttonRowIndex]) {
				b.setStyle("-fx-background-color: #000000; ");
				b.setId((buttonRowIndex) + " " + (columnButtonCount - 1));
				b.setShape(new Circle(r));
				b.setMinSize(2 * r, 2 * r);
				b.setMaxSize(2 * r, 2 * r);
				b.setOnMouseClicked(event -> {
					noButtonsClicked++;
					if (noButtonsClicked == 1) {
						initialCoordinates = b.getId();
						b.setStyle("-fx-background-color: red;");
						b.setOnMouseExited(null);
					} else if (noButtonsClicked == 2) {
						finalCoordinates = b.getId();
						noButtonsClicked = 0;
						String xy[] = initialCoordinates.split(" ");
						xInitialCo = Integer.parseInt(xy[0]);
						yInitialCo = Integer.parseInt(xy[1]);
						if (Main.shipType.isEmpty()) {
							userButton[xInitialCo][yInitialCo].setStyle("-fx-background-color: black;");
							AlertBox.displayError("Error", "Please first select a ship type from menu options.");
						} else {
							if (!player.areAllShipsDeployed()) {
								if (!player.isShipDeployed(Main.shipType)) {
									String res = initialCoordinates + " " + finalCoordinates;
									ob.callDeployUserGrid(res, Main.shipType);
								} else {
									userButton[xInitialCo][yInitialCo].setStyle("-fx-background-color: black;");
									AlertBox.displayError(Main.shipType, "Already Deployed!");
								}
							} else {
								userButton[xInitialCo][yInitialCo].setStyle("-fx-background-color: black;");
								AlertBox.displayError(Main.shipType, "All Ships Deployed!");
							}
							Main.shipType = "";
						}
					}

				});

				g_pane2.add(b, columnButtonCount, rowButtonCount);
				columnButtonCount++;
			}
			if (buttonRowIndex < 9) {
				buttonRowIndex++;
			}
		}
		rowButtonCount = 10;
		// placing the letters on the grid
		for (columnButtonCount = 1; columnButtonCount < 12; columnButtonCount += 1) {
			char ch = (char) ('A' + columnButtonCount - 1);
			Text text1 = new Text(Character.toString(ch));
			g_pane2.add(text1, columnButtonCount, rowButtonCount);
		}
	}

	/**
	 * This is overridden method of base class that contains the logic which needs
	 * to be run after getting notified by observables.
	 * 
	 * @param o   Contains object reference for notifying class.
	 * @param arg contains argument set by the notifying class if any.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Player) {
			if (arg.equals("compwon")) {
				if (((Player) o).getCompWon().equals("Won")) {
					AlertBox.displayResult("Hurray!!", "Computer has Won ");
				}
			}
			else if (arg.equals("ShipDeploy")){
				String value = ((Player) o).getReply();
				int coord[] = ((Player) o).getCoords();
				String shipType = ((Player) o).getShipType();
				String axis = ((Player) o).getAxis();
				deployShipsWithColors(coord, shipType, axis);
				if (!(value.equals("Done")) && !value.isEmpty()) {
					userButton[xInitialCo][yInitialCo].setStyle("-fx-background-color: black;");
					AlertBox.displayError(Main.shipType, value);
				}
			}
			else if(arg.equals("ShipGridSet")) {
				String reply = ((Player) o).getReply();
				int coord[] = ((Player) o).getCoords();
				int score = ((Player) o).getScore();
				if (reply.contains("Hit")) {
					setUserShipCoordinates(coord[0], coord[1], "Hit");
				} else {
					setUserShipCoordinates(coord[0], coord[1], "Miss");
				}
				if (Player.PlayerNum == 2) {
					double health = 1 - Player.sunkenShips.size() * 0.2;
					Platform.runLater(() -> Main.healthbarTank2.setProgress(health));
					Platform.runLater(() -> resulttext3.setText("" + score));
					Platform.runLater(() -> resulttext2.setText(reply));
				}else {
					System.out.println("Setting player 1's health");
					double health = 1 - Player.sunkenShips.size() * 0.2;
					Platform.runLater(() -> Main.healthbarTank1.setProgress(health));
					Platform.runLater(() -> resulttext4.setText("" + score));
					Platform.runLater(() -> resulttext1.setText(reply));
				}
			}
		} else if (o instanceof HitStrategy) {
			String reply = ((HitStrategy) o).getReply();
			int coord[] = ((HitStrategy) o).getCoords();
			if (reply.contains("Hit")) {
				lastCompResult = true;
			} else {
				lastCompResult = false;
			}

			if (reply.contains("Hit")) {
				setUserShipCoordinates(coord[0], coord[1], "Hit");
			} else {
				setUserShipCoordinates(coord[0], coord[1], "Miss");
			}

		} else if (o instanceof HitStrategySalvo){
			if (!player.getCompWon().equals("Won")) {
				String reply = ((HitStrategySalvo) o).getReply();
				int score = ((HitStrategySalvo) o).getScore();
				int coord[] = ((HitStrategySalvo) o).getCoords();
				if (reply.contains("Hit")) {
					lastCompResult = true;
					setUserShipCoordinates(coord[0], coord[1], "Hit");
				} else {
					lastCompResult = false;
					setUserShipCoordinates(coord[0], coord[1], "Miss");
				}
				resulttext1.setText(reply);
				resulttext4.setText("" + score);
				ob.callCheckIfCompWon();
				if (player.getCompWon().equals("Won")) {
					AlertBox.displayResult("OOPS:( :(", "Computer Won ");

				}
			}
		}else if (o instanceof LoadClass){
			if(arg.equals("setcoordsuser")) {
				int[] coordState = ((LoadClass) o).getShipGridCoords();
				if(coordState[2] == 0)
					userButton[coordState[0]][coordState[1]].setStyle("-fx-background-color: black; ");
				else if(coordState[2] == 3)
					userButton[coordState[0]][coordState[1]].setStyle("-fx-background-color: #FFFFFF; ");
				else if(coordState[2] == 2)
					userButton[coordState[0]][coordState[1]].setStyle("-fx-background-color: #ff1100; ");
			}
			else if(arg.equals("setscoreuser")){
				int score = ((LoadClass) o).getUserScore();
				resulttext3.setText("" + score);
			}
			else if(arg.equals("setcolorcoords")) {
				String[] coords = ((LoadClass) o).getColoredShipsCoord();
				int rcoord = Integer.parseInt(coords[0]);
				int ccoord = Integer.parseInt(coords[1]);
				if (coords[2].equals("Carrier"))
					userButton[rcoord][ccoord].setStyle("-fx-background-color: #000080; ");
				if (coords[2].equals("Battleship"))
					userButton[rcoord][ccoord].setStyle("-fx-background-color: #D2691E; ");
				if (coords[2].equals("Cruiser"))
					userButton[rcoord][ccoord].setStyle("-fx-background-color: #008000; ");
				if (coords[2].equals("Submarine"))
					userButton[rcoord][ccoord].setStyle("-fx-background-color: #FFA500; ");
				if (coords[2].equals("Destroyer"))
					userButton[rcoord][ccoord].setStyle("-fx-background-color: #FFFF00; ");
			}
			else if(arg.equals("listofsaves")) {
				Stage stage = new Stage();
				stage.setTitle("Choose a game to load"); 

		        TilePane r = new TilePane();
		        Button loadbutton = new Button("Load Game");
		        ComboBox combo_box = 
		                    new ComboBox(FXCollections 
		                              .observableArrayList(((LoadClass) o).getListOfSaves()));
		        combo_box.getSelectionModel().selectFirst();
		        loadbutton.setOnMouseClicked(event -> {
		        	ob.loadGame(combo_box.getValue().toString());
	                stage.close();
		        });
		        Label selected = new Label("List of saves");  
		        TilePane tile_pane = new TilePane(selected, combo_box, loadbutton); 
		        Scene scene = new Scene(tile_pane, 300, 200); 
		  
		        // Set the scene 
		        stage.setScene(scene); 
		  
		        stage.showAndWait();
			}
		}
	}

	/**
	 * This method deploys the ships with different colors
	 * 
	 * @param coords   holds the co-ordinates that will changed as per the ship type
	 * @param shipType The type of ship being placed
	 * @param axis     tell either horizontal of vertical
	 */
	public static void deployShipsWithColors(int[] coords, String shipType, String axis) {

		// if the ship is to be placed along Y-axis
		if (axis.equals("Y")) {
			if (shipType.equals("Carrier"))
				colorShipYCoords(coords, "#000080");
			if (shipType.equals("Battleship"))
				colorShipYCoords(coords, "#D2691E");
			if (shipType.equals("Cruiser"))
				colorShipYCoords(coords, "#008000");
			if (shipType.equals("Submarine"))
				colorShipYCoords(coords, "#FFA500");
			if (shipType.equals("Destroyer"))
				colorShipYCoords(coords, "#FFFF00");
		}

		// if it is to be placed along X-axis
		else {
			if (shipType.equals("Carrier"))
				colorShipXCoords(coords, "#000080");
			if (shipType.equals("Battleship"))
				colorShipXCoords(coords, "#D2691E");
			if (shipType.equals("Cruiser"))
				colorShipXCoords(coords, "#008000");
			if (shipType.equals("Submarine"))
				colorShipXCoords(coords, "#FFA500");
			if (shipType.equals("Destroyer"))
				colorShipXCoords(coords, "#FFFF00");
		}
	}

	/**
	 * The function will color the Y coordinates based on the type of ship
	 * 
	 * @param coords coordinates that need to be updated for ship placement
	 * @param color  Which color needs to be placed
	 */
	public static void colorShipYCoords(int[] coords, String color) {

		for (int i = coords[1]; i <= coords[3]; i++) {
			userButton[i][coords[0]].setStyle("-fx-background-color: " + color + "; ");
			userButton[i][coords[0]].setOnMouseEntered(null);
			userButton[i][coords[0]].setOnMouseExited(null);
			userButton[i][coords[0]].setOnMouseClicked(null);
			userButton[i][coords[0]].setOnDragEntered(null);
			userButton[i][coords[0]].setOnDragExited(null);
			userButton[i][coords[0]].setText("placed");

		}
	}

	/**
	 * The function will color the X coordinates based on the type of ship
	 * 
	 * @param coords coordinates that need to be updated for ship placement
	 * @param color  Which color needs to be placed
	 */
	public static void colorShipXCoords(int[] coords, String color) {

		for (int i = coords[0]; i <= coords[2]; i++) {
			userButton[coords[1]][i].setStyle("-fx-background-color: " + color + "; ");
			userButton[coords[1]][i].setOnMouseEntered(null);
			userButton[coords[1]][i].setOnMouseExited(null);
			userButton[coords[1]][i].setOnMouseClicked(null);
			userButton[coords[1]][i].setOnDragExited(null);
			userButton[coords[1]][i].setOnDragEntered(null);
			userButton[coords[1]][i].setText("placed");

		}
	}

	/**
	 * This function will set the button color as per the response
	 * 
	 * @param x   x-axis coordinate
	 * @param y   y-axis coordinate
	 * @param res response from user
	 */
	public static void setUserShipCoordinates(int x, int y, String res) {
		if (res.equals("Miss")) {
			userButton[x][y].setStyle("-fx-background-color: #FFFFFF; ");
			userButton[x][y].setOnMouseEntered(null);
			userButton[x][y].setOnMouseExited(null);
		} else if (res.equals("Hit"))
			userButton[x][y].setStyle("-fx-background-color: #ff1100; ");

	}

	/**
	 * Method to display the enemy ships that have sunk in the latest round
	 * 
	 * @param sunkenShips list of sunken ships
	 */
	public static void salvaAlertCall(ArrayList<String> sunkenShips) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Salvo Mode");
		if (sunkenShips.isEmpty()) {
			alert.setHeaderText("Number of User ships Hit: 0");
			alert.setContentText("No ships sunk this time");
		} else {
			String ships = new String();
			alert.setHeaderText("Number of User ships Hit: " + sunkenShips.size());
			ships = "Ships destroyed:\n";
			for (int i = 0; i < sunkenShips.size(); i++) {
				ships = ships + sunkenShips.get(i) + "\n";
			}
			alert.setContentText(ships);
		}
		alert.showAndWait();
		double health = 1 - Player.sunkenShips.size() * 0.2;
		Main.healthbarTank1.setProgress(health);
	}

}
