package application.Views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserWindow {
	
	ToggleGroup radioGroup;
	RadioButton selectedRadioButton;
	String user;
	
	/**
	 * method to display the window to select a user
	 * @return new user or existing user
	 */
	public String selectUser() {
		Stage stage = new Stage();
		this.radioGroup = new ToggleGroup();
		
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("User Info");
		stage.setMinWidth(450);
		stage.setMinHeight(300);

		RadioButton rb1 = new RadioButton("New Player");
		RadioButton rb2 = new RadioButton("Existing Player");
		
		rb1.setToggleGroup(radioGroup);
		rb2.setToggleGroup(radioGroup);
		
		Button btn1 = new Button("Ok");
		btn1.setOnAction(e -> {
			try {

				selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();
				this.user = selectedRadioButton.getText();
				if(this.user.equals("New User"))
					//call relevant method
					user = "new";
				else
					//call another relevant method
					user = "existing";
				stage.close();

			} catch (Exception e1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Battleship Game");
				alert.setHeaderText("Please select an option.");
				alert.showAndWait();
			}
		});

		HBox h_box = new HBox(10);
		Node label;
		h_box.getChildren().addAll(rb1, rb2, btn1);
		h_box.setAlignment(Pos.CENTER);

		Scene scene = new Scene(h_box);
		stage.setScene(scene);
		stage.showAndWait();
		return user;
	}

}
