package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import application.Controllers.GridUser;
import application.Models.Computer;
import application.Models.HitStrategy;
import application.Models.HitStrategySalvo;
import application.Models.LoadClass;
import application.Models.Player;
import application.Models.SaveClass;
import application.Views.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;

/**
 * It is the driver class for the application
 *
 */
public class Main extends Application {
	Player player;
	Computer computer;
	ShipGrid sg;
	SaveClass saveClass;
	LoadClass loadClass;
	static GridUser ob;
	UserWindow userWindow;
	UserDetailsWindow userDetailsWindow;
	MenuBar menuBar;
	Scene scene1;
	Scene scene2, scene3;
	GridPane g_pane1, g_pane2;
	VBox v_box1, v_box2, v_box3;
	HBox h_box1, h_box2;
	AnchorPane healthbars;
	public static final int TOTAL_SHIPS = 5;
	int rowButtonCount;
	int columnButtonCount;
	int buttonRowIndex;
	public static int playerNum;
	public static ProgressBar healthbarTank1, healthbarTank2;
	static Label resulttext1, resulttext2, resulttext3, resulttext4;
	public static String gameType = "None";
	public static String shipType = "";
	public static String gameMode = "Medium";
	public static Button tossBtn;
	public static boolean newGame = true;
	public static Label resultLabel1, resultLabel2;
	public static Text text;
	
	/**
	 * It is the main method
	 * 
	 * @param args This is System arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * This method is overridden from base class that will be called once the launch
	 * is triggered.
	 * 
	 * @param primaryStage reference to primary stage
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Stage stage = primaryStage;
			stage.setTitle(" Battle Ship Game");
			launchStartupWindow(stage);
			SaveClass saveClass = new SaveClass();
			player = new Player(saveClass);
			computer = new Computer();

			LoadClass loadClass = new LoadClass();
			HitStrategy strategy = new HitStrategy();
			HitStrategySalvo strategySalvo = new HitStrategySalvo();
			userWindow = new UserWindow();
			resulttext1 = new Label();
			resulttext3 = new Label();
			resulttext4 = new Label();
			resulttext2 = new Label();
			ob = new GridUser(player, computer, strategy, strategySalvo, saveClass, loadClass);
			sg = new ShipGrid(player, ob, resulttext1, resulttext4, resulttext3, resulttext2);
			userDetailsWindow = new UserDetailsWindow(ob, saveClass);
			player.addObserver(sg);
			strategy.addObserver(sg);
			strategySalvo.addObserver(sg);
			saveClass.addObserver(userDetailsWindow);
			SplitPane split_pane = new SplitPane();
			// SplitPane split_pane2 = new SplitPane();
			g_pane1 = new GridPane();
			g_pane2 = new GridPane();
			v_box3 = new VBox();
			v_box1 = new VBox();
			v_box2 = new VBox();
			h_box1 = new HBox();
			h_box2 = new HBox();
			v_box1.setId("glass-grey");
			v_box2.setId("glass-grey");
			String[] shipNames = { "Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer" };
			for (String name : shipNames) {
				setShipImages(v_box2, name);
			}
			healthbarTank1 = new ProgressBar();
			healthbarTank1.setMinWidth(200);

			healthbarTank1.setStyle("-fx-accent: red;");
			healthbarTank2 = new ProgressBar();
			healthbarTank2.setMinWidth(200);
			// healthbarTank2 = new Rectangle(200.0, 25.0, Color.BLUE);
			// healthbarTank2 = new Rectangle(200.0, 25.0, Color.BLUE);
			h_box2.getChildren().addAll(healthbarTank2);
			menuBar = battleMenu(v_box1, stage);
			g_pane1.setVgap(10);
			g_pane1.setHgap(10);
			// g_pane1.setVisible(false);
			g_pane2.setVgap(10);
			g_pane2.setHgap(10);
			Label l1 = new Label();
			v_box3.getChildren().add(l1);
			// seeResultComp("User ");
			// seeResultUser("Computer ");

			ScoreComp("Computer");
			Score("Player");
			RadarGrid radarGridObserver = new RadarGrid(resulttext2, resulttext1, resulttext3, resulttext4, ob);
			computer.addObserver(radarGridObserver);
			strategy.addObserver(radarGridObserver);
			loadClass.addObserver(radarGridObserver);
			loadClass.addObserver(sg);
			player.addObserver(radarGridObserver);
			radarGridObserver.setUserRadarGrid(g_pane1, resulttext2);
			sg.setUserShipGrid(g_pane2);
			setShipPlacementActions();
			Button userRandomShips = new Button("Feelin' Lazy?");
			VBox v_box4 = new VBox();
			// v_box1.getChildren().addAll(g_pane1, userRandomShips, g_pane2);
			v_box1.getChildren().addAll(userRandomShips, g_pane2);
			v_box1.setSpacing(20.0);
			userRandomShips.setOnAction((ActionEvent event) -> {
				if (Player.numOfShipsDep == 0)
					ob.deployUserShips();
			});

			v_box2.getChildren().addAll(v_box3);
			split_pane.setDividerPositions(0.7);
			split_pane.getItems().add(v_box1);
			split_pane.getItems().add(v_box2);

			Button startBtn = new Button("Start Playing");
			startBtn.setDisable(false);
			startBtn.setOnAction((ActionEvent event) -> {
				if (Player.numOfShipsDep == 5) {
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 11; j++) {
							RadarGrid.radarButton[i][j].setDisable(false);
						}
					}
					// g_pane1.setVisible(true);
					gameStartScene(stage);
					stage.setScene(scene3);
					//deploy the computer ships only when vs player mode is inactive
					//or load games is not selected
					if (!gameType.equalsIgnoreCase("vsmode") && newGame) {
						ob.deployCompShips();
					}
					if(gameType.equalsIgnoreCase("vsmode")) {
						RadarGrid.disableButtons();
						player.PlayerMode(playerNum);
					}

					// Label timerLabel = new Label(timer.toString());

				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Battleship Game");
					alert.setHeaderText("The battle ships are not placed correctly.");
					alert.setContentText("Please place them before starting.");
					alert.showAndWait();
				}
				//ob.checkingManagedException(" player.deploy()");

				// ob.checkingManagedException(" player.deploy()");
			});

			tossBtn = new Button("Toss");
			// radarGridObserver.addTossAction();
			addTossAction(stage);
			for (Node node : g_pane2.getChildren()) {
				node.setOnMouseEntered((MouseEvent t) -> {
					node.setStyle("-fx-background-color: blue;");
				});

				node.setOnMouseExited((MouseEvent t) -> {
					node.setStyle("-fx-background-color: black;");
				});
			}

			h_box1.getChildren().addAll(healthbarTank1);
			// split_pane2.getItems().addAll(h_box1, h_box2);
			text = new Text("0");
			text.setBoundsType(TextBoundsType.VISUAL);
			text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
			
			Sphere sphere = new Sphere();
			PhongMaterial material = new PhongMaterial();
			material.setDiffuseColor(Color.LIGHTSKYBLUE);
			material.setSpecularColor(Color.BLACK);
			// Setting the properties of the Sphere
			sphere.setRadius(40.0);
			sphere.setMaterial(material);
			StackPane stack = new StackPane();
			stack.setTranslateX(510);
			//stack.setTranslateY(0);
			
			stack.getChildren().addAll(sphere, text);
			
			healthbars = new AnchorPane();

			healthbars.getChildren().addAll(h_box1, h_box2, stack);
			AnchorPane.setBottomAnchor(h_box1, 8.0);
			AnchorPane.setRightAnchor(h_box2, 5.0);
			// AnchorPane.setRightAnchor(timerLabel, 10.0);
			v_box3.getChildren().addAll(startBtn, tossBtn);
			v_box4.getChildren().addAll(menuBar, split_pane);
			v_box1.fillWidthProperty();

			scene1 = new Scene(v_box4, 850, 650);

			v_box1.getStylesheets().add("application/Views/application.css");
			v_box2.getStylesheets().add("application/Views/application.css");
			split_pane.prefHeightProperty().bind(stage.heightProperty());
			stage.show();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * This method adds action listener to the button toss. It calculates the winner
	 * of the toss as per user choice and allows the user to play.
	 * @param stage stage object for setting up the scene
	 */
	public void addTossAction(Stage stage) {
		tossBtn.setOnAction((ActionEvent event) -> {
			if (Player.numOfShipsDep == 5) {

				if (!gameMode.equalsIgnoreCase("vsmode")) {
					ob.deployCompShips();

					String tossResult = InputBox.display("Please choose Head or Tail");
					int r = (int) Math.round(Math.random());
					if ((r == 1 && tossResult.equalsIgnoreCase("Head"))
							|| (r == 0 && tossResult.equalsIgnoreCase("Tail"))) {
						if (Main.gameType.equals("Salvo")) {
							Main.salvoAlertCall();
						}
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Battleship - Toss Result");
						alert.setHeaderText("It's computer turn first.");
						alert.showAndWait();
						ob.computerTurn(RadarGrid.lastCompResult, gameMode);
					} else {
						if (Main.gameType.equals("Salvo")) {
							Main.salvoAlertCall();
						}
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Battleship Game");
						alert.setHeaderText("It's a " + tossResult);
						alert.setContentText("It's your turn first!");
						alert.showAndWait();
					}
				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Battleship Game");
					alert.setHeaderText("The battle ships are not placed correctly.");
					alert.setContentText("Please place them before starting.");
					alert.showAndWait();

				}
			}

			gameStartScene(stage);
			stage.setScene(scene3);
		});
	}
	
	/**
	 * Method to start the game 
	 * @param stage stage object to set the scene
	 */
	public void gameStartScene(Stage stage) {
		// AnchorPane root = new AnchorPane();
		SplitPane root = new SplitPane();
		VBox v_box = new VBox();
		root.setId("glass-grey");
		v_box.setId("glass-grey");

		root.setDividerPositions(0.5);
		root.getItems().add(g_pane2);
		root.getItems().add(g_pane1);
		/*
		 * root.getChildren().addAll(g_pane1, g_pane2); root.setRightAnchor(g_pane1,
		 * 30.0); root.setLeftAnchor(g_pane2, 30.0);
		 */
		root.getStylesheets().add("application/Views/application.css");
		v_box.getStylesheets().add("application/Views/application.css");
		root.prefHeightProperty().bind(stage.heightProperty());
		v_box.getChildren().addAll(menuBar, healthbars, root);
		scene3 = new Scene(v_box, 1100, 600);

	}

	/**
	 * This function will set different action listeners over the button drag
	 * actions
	 */
	public void setShipPlacementActions() {
		for (Node node : g_pane2.getChildren()) {

			node.setOnDragEntered(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					int iniX = Integer.parseInt(node.getId().split(" ")[0]);
					int iniY = Integer.parseInt(node.getId().split(" ")[1]);
					if (event.getDragboard().getString().split(";")[0].equals("Primary")) {
						if (event.getDragboard().getString().split(";")[1].equals("Carrier")) {
							if (iniY - 2 >= 0 && iniY + 2 < 11) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX][iniY - 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY - 1].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX][iniY - 2].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY - 2].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX][iniY + 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 1].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX][iniY + 2].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 2].setStyle("-fx-background-color: blue;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Battleship")) {
							if (iniY - 1 >= 0 && iniY + 2 < 11) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX][iniY - 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY - 1].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX][iniY + 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 1].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX][iniY + 2].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 2].setStyle("-fx-background-color: blue;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Cruiser")) {
							if (iniY - 1 >= 0 && iniY + 1 < 11) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX][iniY - 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY - 1].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX][iniY + 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 1].setStyle("-fx-background-color: blue;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Submarine")) {
							if (iniY - 1 >= 0 && iniY + 1 < 11) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX][iniY - 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY - 1].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX][iniY + 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 1].setStyle("-fx-background-color: blue;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Destroyer")) {
							if (iniY >= 0 && iniY + 1 < 11) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX][iniY + 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 1].setStyle("-fx-background-color: blue;");
							}
						}
					} else {
						if (event.getDragboard().getString().split(";")[1].equals("Carrier")) {
							if (iniX - 2 >= 0 && iniX + 2 < 9) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX - 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX - 1][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX - 2][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX - 2][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX + 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 1][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX + 2][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 2][iniY].setStyle("-fx-background-color: blue;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Battleship")) {
							if (iniX - 1 >= 0 && iniX + 2 < 9) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX - 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX - 1][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX + 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 1][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX + 2][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 2][iniY].setStyle("-fx-background-color: blue;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Cruiser")) {
							if (iniX - 1 >= 0 && iniX + 1 < 9) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX - 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX - 1][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX + 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 1][iniY].setStyle("-fx-background-color: blue;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Submarine")) {
							if (iniX - 1 >= 0 && iniX + 1 < 9) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX - 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX - 1][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX + 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 1][iniY].setStyle("-fx-background-color: blue;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Destroyer")) {
							if (iniX >= 0 && iniX + 1 < 9) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: blue;");
								if (!ShipGrid.userButton[iniX + 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 1][iniY].setStyle("-fx-background-color: blue;");
							}
						}
					}
					event.consume();
				}
			});

			node.setOnDragExited(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					int iniX = Integer.parseInt(node.getId().split(" ")[0]);
					int iniY = Integer.parseInt(node.getId().split(" ")[1]);
					if (event.getDragboard().getString().split(";")[0].equals("Primary")) {
						if (event.getDragboard().getString().split(";")[1].equals("Carrier")) {
							if (iniY - 2 >= 0 && iniY + 2 < 11) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX][iniY - 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY - 1].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX][iniY - 2].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY - 2].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX][iniY + 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 1].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX][iniY + 2].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 2].setStyle("-fx-background-color: black;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Battleship")) {
							if (iniY - 1 >= 0 && iniY + 2 < 11) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX][iniY - 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY - 1].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX][iniY + 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 1].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX][iniY + 2].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 2].setStyle("-fx-background-color: black;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Cruiser")) {
							if (iniY - 1 >= 0 && iniY + 1 < 11) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX][iniY - 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY - 1].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX][iniY + 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 1].setStyle("-fx-background-color: black;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Submarine")) {
							if (iniY - 1 >= 0 && iniY + 1 < 11) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX][iniY - 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY - 1].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX][iniY + 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 1].setStyle("-fx-background-color: black;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Destroyer")) {
							if (iniY >= 0 && iniY + 1 < 11) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX][iniY + 1].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY + 1].setStyle("-fx-background-color: black;");
							}
						}
					} else {
						if (event.getDragboard().getString().split(";")[1].equals("Carrier")) {
							if (iniX - 2 >= 0 && iniX + 2 < 9) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX - 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX - 1][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX - 2][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX - 2][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX + 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 1][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX + 2][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 2][iniY].setStyle("-fx-background-color: black;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Battleship")) {
							if (iniX - 1 >= 0 && iniX + 2 < 9) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX - 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX - 1][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX + 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 1][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX + 2][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 2][iniY].setStyle("-fx-background-color: black;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Cruiser")) {
							if (iniX - 1 >= 0 && iniX + 1 < 9) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX - 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX - 1][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX + 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 1][iniY].setStyle("-fx-background-color: black;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Submarine")) {
							if (iniX - 1 >= 0 && iniX + 1 < 9) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX - 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX - 1][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX + 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 1][iniY].setStyle("-fx-background-color: black;");
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Destroyer")) {
							if (iniX >= 0 && iniX + 1 < 9) {
								if (!ShipGrid.userButton[iniX][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX][iniY].setStyle("-fx-background-color: black;");
								if (!ShipGrid.userButton[iniX + 1][iniY].getText().contains("placed"))
									ShipGrid.userButton[iniX + 1][iniY].setStyle("-fx-background-color: black;");
							}
						}
					}
					event.consume();
				}
			});
			node.setOnDragOver(new EventHandler<DragEvent>() {
				public void handle(DragEvent event) {
					Dragboard db = event.getDragboard();
					if (db.hasImage()) {
						event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
					}
					event.consume();
				}
			});
			node.setOnDragDropped(new EventHandler<DragEvent>() {
				@Override
				public void handle(DragEvent event) {
					String initialCoordinates, finalCoordinates;
					int iniX = Integer.parseInt(node.getId().split(" ")[0]);
					int iniY = Integer.parseInt(node.getId().split(" ")[1]);
					if (event.getDragboard().getString().split(";")[0].equals("Primary")) {
						if (event.getDragboard().getString().split(";")[1].equals("Carrier")) {
							if (iniY - 2 >= 0 && iniY + 2 < 11) {
								initialCoordinates = Integer.toString(iniX) + " " + Integer.toString(iniY - 2);
								finalCoordinates = Integer.toString(iniX) + " " + Integer.toString(iniY + 2);
								if (!player.areAllShipsDeployed()) {
									if (!player.isShipDeployed("Carrier")) {
										String res = initialCoordinates + " " + finalCoordinates;
										ob.callDeployUserGrid(res, "Carrier");
									} else {
										AlertBox.displayError("Carrier", "Already Deployed!");
									}
								} else {
									AlertBox.displayError("Carrier", "All Ships Deployed!");
								}
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Battleship")) {
							if (iniY - 1 >= 0 && iniY + 2 < 11) {
								initialCoordinates = Integer.toString(iniX) + " " + Integer.toString(iniY - 1);
								finalCoordinates = Integer.toString(iniX) + " " + Integer.toString(iniY + 2);
								if (!player.areAllShipsDeployed()) {
									if (!player.isShipDeployed("Battleship")) {
										String res = initialCoordinates + " " + finalCoordinates;
										ob.callDeployUserGrid(res, "Battleship");
									} else {
										AlertBox.displayError("Battleship", "Already Deployed!");
									}
								} else {
									AlertBox.displayError("Battleship", "All Ships Deployed!");
								}
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Cruiser")) {
							if (iniY - 1 >= 0 && iniY + 1 < 11) {
								initialCoordinates = Integer.toString(iniX) + " " + Integer.toString(iniY - 1);
								finalCoordinates = Integer.toString(iniX) + " " + Integer.toString(iniY + 1);
								if (!player.areAllShipsDeployed()) {
									if (!player.isShipDeployed("Cruiser")) {
										String res = initialCoordinates + " " + finalCoordinates;
										ob.callDeployUserGrid(res, "Cruiser");
									} else {
										AlertBox.displayError("Cruiser", "Already Deployed!");
									}
								} else {
									AlertBox.displayError("Cruiser", "All Ships Deployed!");
								}
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Submarine")) {
							if (iniY - 1 >= 0 && iniY + 1 < 11) {
								initialCoordinates = Integer.toString(iniX) + " " + Integer.toString(iniY - 1);
								finalCoordinates = Integer.toString(iniX) + " " + Integer.toString(iniY + 1);
								if (!player.areAllShipsDeployed()) {
									if (!player.isShipDeployed("Submarine")) {
										String res = initialCoordinates + " " + finalCoordinates;
										ob.callDeployUserGrid(res, "Submarine");
									} else {
										AlertBox.displayError("Submarine", "Already Deployed!");
									}
								} else {
									AlertBox.displayError("Submarine", "All Ships Deployed!");
								}
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Destroyer")) {
							if (iniY >= 0 && iniY + 1 < 11) {
								initialCoordinates = Integer.toString(iniX) + " " + Integer.toString(iniY);
								finalCoordinates = Integer.toString(iniX) + " " + Integer.toString(iniY + 1);
								if (!player.areAllShipsDeployed()) {
									if (!player.isShipDeployed("Destroyer")) {
										String res = initialCoordinates + " " + finalCoordinates;
										ob.callDeployUserGrid(res, "Destroyer");
									} else {
										AlertBox.displayError("Destroyer", "Already Deployed!");
									}
								} else {
									AlertBox.displayError("Destroyer", "All Ships Deployed!");
								}
							}
						}
					} else {
						if (event.getDragboard().getString().split(";")[1].equals("Carrier")) {
							if (iniX - 2 >= 0 && iniX + 2 < 9) {
								initialCoordinates = Integer.toString(iniX - 2) + " " + Integer.toString(iniY);
								finalCoordinates = Integer.toString(iniX + 2) + " " + Integer.toString(iniY);
								if (!player.areAllShipsDeployed()) {
									if (!player.isShipDeployed("Carrier")) {
										String res = initialCoordinates + " " + finalCoordinates;
										ob.callDeployUserGrid(res, "Carrier");
									} else {
										AlertBox.displayError("Carrier", "Already Deployed!");
									}
								} else {
									AlertBox.displayError("Carrier", "All Ships Deployed!");
								}
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Battleship")) {
							if (iniX - 1 >= 0 && iniX + 2 < 9) {
								initialCoordinates = Integer.toString(iniX - 1) + " " + Integer.toString(iniY);
								finalCoordinates = Integer.toString(iniX + 2) + " " + Integer.toString(iniY);
								if (!player.areAllShipsDeployed()) {
									if (!player.isShipDeployed("Battleship")) {
										String res = initialCoordinates + " " + finalCoordinates;
										ob.callDeployUserGrid(res, "Battleship");
									} else {
										AlertBox.displayError("Battleship", "Already Deployed!");
									}
								} else {
									AlertBox.displayError("Battleship", "All Ships Deployed!");
								}
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Cruiser")) {
							if (iniX - 1 >= 0 && iniX + 1 < 9) {
								initialCoordinates = Integer.toString(iniX - 1) + " " + Integer.toString(iniY);
								finalCoordinates = Integer.toString(iniX + 1) + " " + Integer.toString(iniY);
								if (!player.areAllShipsDeployed()) {
									if (!player.isShipDeployed("Cruiser")) {
										String res = initialCoordinates + " " + finalCoordinates;
										ob.callDeployUserGrid(res, "Cruiser");
									} else {
										AlertBox.displayError("Cruiser", "Already Deployed!");
									}
								} else {
									AlertBox.displayError("Cruiser", "All Ships Deployed!");
								}
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Submarine")) {
							if (iniX - 1 >= 0 && iniX + 1 < 9) {
								initialCoordinates = Integer.toString(iniX - 1) + " " + Integer.toString(iniY);
								finalCoordinates = Integer.toString(iniX + 1) + " " + Integer.toString(iniY);
								if (!player.areAllShipsDeployed()) {
									if (!player.isShipDeployed("Submarine")) {
										String res = initialCoordinates + " " + finalCoordinates;
										ob.callDeployUserGrid(res, "Submarine");
									} else {
										AlertBox.displayError("Submarine", "Already Deployed!");
									}
								} else {
									AlertBox.displayError("Submarine", "All Ships Deployed!");
								}
							}
						} else if (event.getDragboard().getString().split(";")[1].equals("Destroyer")) {
							if (iniX >= 0 && iniX + 1 < 9) {
								initialCoordinates = Integer.toString(iniX) + " " + Integer.toString(iniY);
								finalCoordinates = Integer.toString(iniX + 1) + " " + Integer.toString(iniY);
								if (!player.areAllShipsDeployed()) {
									if (!player.isShipDeployed("Destroyer")) {
										String res = initialCoordinates + " " + finalCoordinates;
										ob.callDeployUserGrid(res, "Destroyer");
									} else {
										AlertBox.displayError("Destroyer", "Already Deployed!");
									}
								} else {
									AlertBox.displayError("Destroyer", "All Ships Deployed!");
								}
							}
						}
					}
				}
			});
		}
	}
	
	/**
	 * Set the images of the ships in the window
	 * @param v_box2 vertical box object
	 * @param shipName name of the ships that are to be displayed
	 */
	public void setShipImages(VBox v_box2, String shipName) {
		try {
			Image image = new Image(new FileInputStream("images/ships/" + shipName + ".png"));
			ImageView imageView = new ImageView(image);
			imageView.setFitHeight(70);
			imageView.setFitWidth(100);
			imageView.setPreserveRatio(true);
			imageView.fitWidthProperty().bind(v_box2.widthProperty());
			Image image2 = new Image(new FileInputStream("images/ships/" + shipName + "90.png"));
			Label shipNameLabel = new Label(shipName);
			shipNameLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 18));
			shipNameLabel.setTextFill(Color.web("#c40831"));
			v_box2.getChildren().addAll(shipNameLabel, imageView);

			imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
					ClipboardContent content = new ClipboardContent();

					if (event.getButton() == MouseButton.PRIMARY) {
						content.putString("Primary;" + shipName);
						content.putImage(imageView.getImage());
					} else if (event.getButton() == MouseButton.SECONDARY) {
						content.putString("Secondary;" + shipName);
						content.putImage(image2);
					}
					db.setContent(content);
					event.consume();
				}
			});
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This method sets the menu bar on the scene
	 * 
	 * @param v_box1 reference to vertical box for holding the menubar
	 * @param stage  stage reference
	 * @return MenuBar that is created
	 */
	public MenuBar battleMenu(VBox v_box1, Stage stage) {
		Menu menu1 = new Menu("Game");
		Menu menu2 = new Menu("BattleShip");
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(menu1);
		menuBar.getMenus().add(menu2);
		MenuItem menu1Item1 = new MenuItem("Start new game");
		MenuItem menu1Item2 = new MenuItem("Exit");
		MenuItem menu1Item3 = new MenuItem("Save game");
		MenuItem menu1Item4 = new MenuItem("Load game");
		menu1Item1.setOnAction(e -> {
			stage.close();
			try {
				start(new Stage());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		menu1Item2.setOnAction(e -> {
			Boolean res = ConfirmBox.display("Confirmation box", "Are you sure?");
			if (res)
				stage.close();
		});
		menu1Item3.setOnAction(e -> {
			Boolean res = ConfirmBox.display("Save game", "Do you wish to save the game?");
			if (res) {
				ob.saveGame(gameMode, gameType);
			}

		});
		menu1Item4.setOnAction(e -> {
			ob.getSelectedLoadGame();
			newGame = false;
		});

		menu1.getItems().add(menu1Item1);
		menu1.getItems().add(menu1Item2);
		menu1.getItems().add(menu1Item3);
		menu1.getItems().add(menu1Item4);

		Menu place_ship = new Menu("Place");
		MenuItem Carrier = new MenuItem("Carrier (5)");
		Carrier.setGraphic(new ImageView("file:images/blue1.png"));
		MenuItem Battleship = new MenuItem("Battleship (4)");
		Battleship.setGraphic(new ImageView("file:images/brown.png"));
		MenuItem Cruiser = new MenuItem("Cruiser (3)");
		Cruiser.setGraphic(new ImageView("file:images/green.png"));
		MenuItem Submarine = new MenuItem("Submarine (3)");
		Submarine.setGraphic(new ImageView("file:images/orange.png"));
		MenuItem Destroyer = new MenuItem("Destroyer (2)");
		Destroyer.setGraphic(new ImageView("file:images/yellow.png"));
		place_ship.getItems().add(Carrier);
		place_ship.getItems().add(Battleship);
		place_ship.getItems().add(Cruiser);
		place_ship.getItems().add(Submarine);
		place_ship.getItems().add(Destroyer);
		menu2.getItems().add(place_ship);
		Carrier.setOnAction(e -> {
			shipType = "Carrier";

		});
		Battleship.setOnAction(e -> {
			shipType = "Battleship";
		});
		Cruiser.setOnAction(e -> {
			shipType = "Cruiser";
		});
		Submarine.setOnAction(e -> {
			shipType = "Submarine";
		});
		Destroyer.setOnAction(e -> {
			shipType = "Destroyer";
		});

		return menuBar;
	}

	/**
	 * The startup window gets launched with this method
	 * 
	 * @param stg It is a temorary stage used a start up screen
	 * @throws Exception may throw file not found error
	 */
	public void launchStartupWindow(Stage stg) throws Exception {

		GridPane root1 = new GridPane();
		scene2 = new Scene(root1, 800, 600);
		root1.setVgap(10);
		root1.setHgap(10);
		Image image = new Image(new FileInputStream("images/battleship.png"));
		BackgroundImage backgroundimage = new BackgroundImage(image, BackgroundRepeat.ROUND, BackgroundRepeat.ROUND,
				BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background background = new Background(backgroundimage);
		root1.setBackground(background);

		Button btn5 = new Button("Player 1");
		btn5.setStyle("-fx-background-color: #a3a0a0; ");
		btn5.setVisible(false);
		Button btn6 = new Button("Player 2");
		btn6.setStyle("-fx-background-color: #a3a0a0; ");
		btn6.setVisible(false);

		Button btn1 = new Button("Vs Player");
		btn1.setStyle("-fx-background-color: #a3a0a0; ");
		Button btn2 = new Button("Exit Game");
		btn2.setStyle("-fx-background-color: #a3a0a0; ");
		Button btn3 = new Button("Vs Computer Classic Mode");
		btn3.setStyle("-fx-background-color: #a3a0a0; ");
		Button btn4 = new Button("Vs Computer Salvo Mode");
		btn4.setStyle("-fx-background-color: #a3a0a0; ");
		btn1.setOnAction((ActionEvent event) -> {
			// add playr 1 or 2
			gameType = "vsmode";
			tossBtn.setVisible(false);
			btn5.setVisible(true);
			btn6.setVisible(true);
		});

		btn5.setOnAction((ActionEvent event) -> {
			playerNum = 1;
			//player.PlayerMode(1);
			userDetailsWindow.newUser();
			stg.setScene(scene1);
		});

		btn6.setOnAction((ActionEvent event) -> {
			playerNum = 2;
			//player.PlayerMode(2);
			userDetailsWindow.newUser();
			stg.setScene(scene1);
		});

		btn3.setOnAction((ActionEvent event) -> {
			gameType = "Classic";

			gameMode = AlertBox.displayDifficulty();
			userDetailsWindow.newUser();
			stg.setScene(scene1);
		});
		btn4.setOnAction((ActionEvent event) -> {
			userDetailsWindow.newUser();
			gameType = "Salvo";
			stg.setScene(scene1);
		});
		btn2.setOnAction((ActionEvent event) -> {
			Boolean res = ConfirmBox.display("Confirmation box", "Are you sure?");
			if (res)
				stg.close();
		});

		root1.setAlignment(Pos.CENTER);
		root1.add(btn1, 0, 1);
		root1.add(btn5, 2, 1);
		root1.add(btn6, 3, 1);
		root1.add(btn3, 0, 2);
		root1.add(btn4, 0, 3);
		root1.add(btn2, 0, 4);

		stg.setScene(scene2);
	}

	
	/**
	 * Places the Score of the user on the screen
	 * 
	 * @param title To display name of caller (User or CPU).
	 */
	public void Score(String title) {
		VBox vBox = new VBox();
		resultLabel1 = new Label();
		// resultLabel1.setGraphic(new ImageView("file:images/icon2.jpg"));
		ImageView imageView = new ImageView("file:images/icon5.gif");
		imageView.setFitHeight(50);
		imageView.setFitWidth(50);
		imageView.setPreserveRatio(true);
		// imageView.fitWidthProperty().bind(v_box2.widthProperty());
		// resultLabel1.setGraphic(imageView);
		resultLabel1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
		resultLabel1.setTextFill(Color.web("#c40831"));
		// resulttext3.setStyle("-fx-background-color: white;");
		resulttext3.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 16));
		resulttext3.setTextFill(Color.web("#c40831"));
		// resulttext2.setStyle("-fx-background-color: white;");
		resulttext2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 16));
		resulttext2.setTextFill(Color.web("#c40831"));
		vBox.getChildren().addAll(resultLabel1, resulttext3, resulttext2);
		h_box1.getChildren().addAll(imageView, vBox);

	}

	/**
	 * Places the Score of the Computer on the screen
	 * 
	 * @param title To display name of caller (User or CPU).
	 */
	public void ScoreComp(String title) {
		resultLabel2 = new Label(title);
		VBox vBox = new VBox();
		ImageView imageView = new ImageView("file:images/icon4.gif");
		imageView.setFitHeight(50);
		imageView.setFitWidth(50);
		imageView.setPreserveRatio(true);
		// imageView.fitWidthProperty().bind(v_box2.widthProperty());
		// resultLabel2.setGraphic(imageView);
		resultLabel2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
		resultLabel2.setTextFill(Color.web("#00bfff"));
		// resulttext4.setStyle("-fx-background-color: white;");
		resulttext4.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 16));
		resulttext4.setTextFill(Color.web("#00bfff"));
		// resulttext1.setStyle("-fx-background-color: white;");
		resulttext1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 16));
		resulttext1.setTextFill(Color.web("#00bfff"));
		vBox.getChildren().addAll(resultLabel2, resulttext4, resulttext1);
		h_box2.getChildren().addAll(vBox, imageView);

	}

	/**
	 * Method to call the alert box for salvo variation in the starting
	 * 
	 */
	public static void salvoAlertCall() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Salvo Mode");
		alert.setHeaderText("You are in Salvo mode.");
		alert.setContentText("The number of shots you take depends on the number of ships you have left.");
		alert.showAndWait();
	}

}
