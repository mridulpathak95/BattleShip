package application.test.Controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.Main;

public class MainStartUpTest {
	private Main ob = null;

	@Before
	public void setUp() {
		ob = new Main();

	}

	@After
	public void cleanUp() {
		ob = null;

	}

	/**
	 * check if ships deployed only then startup otherwise throw Alert
	 * 
	 */

	@Test
	public void startTest() {

		/*Stage primaryStage = new Stage();

		ob.start(primaryStage);*/

		//assertEquals("Please place them before starting.", ob.getAlertBoxSet());

	}

}
