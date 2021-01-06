package application.Views;

import java.util.Timer;
import java.util.TimerTask;
import application.Models.Computer;
import javafx.application.Platform;
import main.Main;

/**
 * Class to implement the timer functionality in the game 
 * @author Ishita Bhola
 *
 */
public class RemindTask extends TimerTask {
	Thread newThread;
	Timer timer;
	
	/**
	 * parameterized constructor to set some objects
	 * @param newThread new Thread Object
	 * @param timer Timer object
	 */
	public RemindTask(Thread newThread, Timer timer) {
		this.newThread = newThread;
		this.timer = timer;
	}
	
	/**
	 * overriden method to run a thread
	 */
	public void run() {
		//System.out.println("Time's up!");
		newThread.stop();
		timer.cancel(); // Terminate the timer thread
		Computer.scoringComp = Computer.scoringComp - 10; 
		//Platform.runLater(() -> AlertBox.displayError("TIMER", "Your Time is Up! "));
	}
}
