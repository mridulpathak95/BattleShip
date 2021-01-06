package application.Views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class to display the Alert box for required message
 * @author Sagar Bhatia
 *
 */
public class AlertBox {
	static ToggleGroup radioGroup;
	static String difficulty;
	static RadioButton selectedRadioButton;
	static String gameType;
	
	/**
	 * Method to display the alert in case of an error
	 * @param title title of the window
	 * @param msg message to be displayed in the box
	 */
	public static void displayError(String title, String msg) {
		Stage stage = new Stage();

		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(title);
		stage.setMinWidth(450);
		stage.setMinHeight(300);

		Label label1 = new Label(msg);

		Button btn1 = new Button("Ok");

		btn1.setOnAction(e -> {
			stage.close();
		});

		VBox v_box = new VBox();
		v_box.getChildren().addAll(label1, btn1);
		v_box.setAlignment(Pos.CENTER);

		Scene scene = new Scene(v_box);
		stage.setScene(scene);
		stage.showAndWait();

	}
	
	/**
	 * Method to display any sort of result in the alert box
	 * @param title title of the window
	 * @param msg message to be displayed
	 */
	public static void displayResult(String title, String msg) {
		Stage stage = new Stage();

		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(title);
		stage.setMinWidth(450);
		stage.setMinHeight(300);

		Label label1 = new Label(msg);

		Button btn1 = new Button("OK");

		btn1.setOnAction(e -> {
			// put the reinitialize
			stage.close();
		});

		VBox v_box = new VBox();
		v_box.getChildren().addAll(label1, btn1);
		v_box.setAlignment(Pos.CENTER);

		Scene scene = new Scene(v_box);
		stage.setScene(scene);
		stage.showAndWait();

	}
	
	/**
	 * Method to display the difficult
	 * @return difficulty mode in the form string
	 */
	public static String displayDifficulty() {
		Stage stage = new Stage();
		radioGroup = new ToggleGroup();
		difficulty = new String();

		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Game Difficulty");
		stage.setMinWidth(450);
		stage.setMinHeight(300);

		Label label = new Label("Choose Difficulty:");
		RadioButton rb1 = new RadioButton("Easy");
		RadioButton rb2 = new RadioButton("Medium");
		RadioButton rb3 = new RadioButton("Hard");

		rb1.setToggleGroup(radioGroup);
		rb2.setToggleGroup(radioGroup);
		rb3.setToggleGroup(radioGroup);

		Button btn1 = new Button("Ok");
		btn1.setOnAction(e -> {
			try {

				selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();
				difficulty = selectedRadioButton.getText();
				stage.close();

			} catch (Exception e1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Battleship Game");
				alert.setHeaderText("Please select a mode before proceeding.");
				alert.showAndWait();
			}
		});

		VBox v_box = new VBox();
		v_box.getChildren().addAll(label, rb1, rb2, rb3, btn1);
		v_box.setAlignment(Pos.CENTER);

		Scene scene = new Scene(v_box);
		stage.setScene(scene);
		stage.showAndWait();
		return difficulty;

	}
	
	/**
	 * Method display the display the alert 
	 * to choose a type of the game(Classic/ Salvo)
	 * @return gameType returned in the form of a String
	 */
	public static String displayGameType() {
		Stage stage = new Stage();
		radioGroup = new ToggleGroup();
		difficulty = new String();

		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Battleship Game Type");
		stage.setMinWidth(450);
		stage.setMinHeight(300);

		Label label = new Label("Choose Game Type:");
		RadioButton rb1 = new RadioButton("Salvo");
		RadioButton rb2 = new RadioButton("Classic");
		
		rb1.setToggleGroup(radioGroup);
		rb2.setToggleGroup(radioGroup);

		Button btn1 = new Button("Ok");
		btn1.setOnAction(e -> {
			try {

				selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();
				gameType = selectedRadioButton.getText();
				stage.close();

			} catch (Exception e1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Battleship Game");
				alert.setHeaderText("Please select a mode before proceeding.");
				alert.showAndWait();
			}
		});

		VBox v_box = new VBox();
		v_box.getChildren().addAll(label, rb1, rb2, btn1);
		v_box.setAlignment(Pos.CENTER);

		Scene scene = new Scene(v_box);
		stage.setScene(scene);
		stage.showAndWait();
		return gameType;

	}
}
