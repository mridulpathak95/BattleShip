package application.Views;

import java.util.Observable;
import java.util.Observer;

import application.Controllers.GridUser;
import application.Models.SaveClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;

/**
 * Class to enter the details for the new as well as the existing users
 * @author Sagar Bhatia
 *
 */
public class UserDetailsWindow implements Observer{
	
	String userName;
	boolean verifyUserName;
	String password;
	String userOptions;
	GridUser ob;
	SaveClass saveClass;
	ToggleGroup radioGroup;
	RadioButton selectedRadioButton;
	String user;
	
	public String getUserName() {
		return this.userName;
	}
	public void setUserName(String name) {
		this.userName = name;
	}
	
	public UserDetailsWindow(){
	
	}
	
	/**
	 * Parameterized constructor to initialize some objects 
	 * @param ob	Object of GridUser
	 * @param saveClass	Object of SaveClass
	 */
	public UserDetailsWindow(GridUser ob, SaveClass saveClass){
		this.ob = ob;
		this.saveClass = saveClass;
	}
	
	public boolean getVerifyUserName() {
		return this.verifyUserName;
	}

	public void setVerifyUserName(boolean verifyUserName) {
		this.verifyUserName = verifyUserName;
	}
	
	
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * method to add the details for the new user
	 */
	public void newUser() {
		
		
		
		Stage stage = new Stage();
		stage.setTitle("New User");
		GridPane grid = new GridPane();
		this.radioGroup = new ToggleGroup();
		
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("User Info");
		stage.setMinWidth(450);
		stage.setMinHeight(300);

		RadioButton rb1 = new RadioButton("New Player");
		RadioButton rb2 = new RadioButton("Existing Player");
		
		rb1.setToggleGroup(radioGroup);
		rb2.setToggleGroup(radioGroup);
		
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Scene scene = new Scene(grid, 450, 300);
		stage.setScene(scene);
		Text scenetitle = new Text("Welcome to the Battleship game");
		scenetitle.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		Label username = new Label("User Name:");
		grid.add(username, 0, 2);

		TextField userTextField = new TextField();
		grid.add(userTextField, 1, 2);

		Label pw = new Label("Password:");
		grid.add(pw, 0, 3);

		PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 3);
		
		Button btn = new Button("Login");
		btn.setOnAction(e -> {
			try {
				
				this.userName = userTextField.getText();
				
				this.password = pwBox.getText();
				selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();
				this.user = selectedRadioButton.getText();
				ob.checkUserName(userName, this.user);
				if(this.user.equals("New Player")) {
					System.out.println(this.user+" "+this.getVerifyUserName());
					if(!this.getVerifyUserName()) {
						setUser("new");
						stage.close();
					}
					else {
						
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Username Error");
						alert.setHeaderText("Username taken. Please select a different username.");
						alert.showAndWait();
						
					}
				}
				else {
					
					if(this.getVerifyUserName()) {
						setUser("existing");
						stage.close();
					}
					else {
						
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Username Error");
						alert.setHeaderText("No such username found.");
						alert.showAndWait();
						
					}
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		HBox h_box = new HBox(10);
		Node label;
		h_box.getChildren().addAll(rb1, rb2);
		h_box.setAlignment(Pos.CENTER);
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(btn);
		grid.add(hbBtn, 1, 4);
		grid.add(h_box, 0, 1);
		stage.showAndWait();
		
	}
	
	/**
	 * Overridden method of the Observer interface 
	 */
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if(o instanceof SaveClass) {
			setVerifyUserName((boolean)arg);
		}
		
	}

}
