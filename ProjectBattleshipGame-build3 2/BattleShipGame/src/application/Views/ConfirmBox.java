package application.Views;

/**
 * This class contains function display which serves a purpose of confirming an input from user.
 * 
 * @author arsalaan
 */

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
	
	static Boolean result;
	
	/**
	 * Method to display a confirm box to ensure a result
	 * @param title title of the confirm box
	 * @param msg message to be displayed in it
	 * @return boolean value based on Yes/No selected by the user
	 */
	public static Boolean display(String title, String msg ) {
	Stage stage = new Stage();
	
	stage.initModality(Modality.APPLICATION_MODAL);
	stage.setTitle(title);
	stage.setMinWidth(550);
	stage.setMinHeight(300);
	
	Label label1 = new Label(msg);
	
	Button btn1 = new Button("Yes");
	Button btn2 = new Button("No");
	
	btn1.setOnAction(e -> {
		result = true;
		stage.close();
	});
	
	btn2.setOnAction(e -> {
		result = false;
		stage.close();
	});
	
	VBox v_box = new VBox();
	v_box.getChildren().addAll(label1, btn1, btn2 );
	v_box.setAlignment(Pos.CENTER);
	
	Scene scene = new Scene(v_box);
	stage.setScene(scene);
	stage.showAndWait();
	
	return result;
	
	}
}
