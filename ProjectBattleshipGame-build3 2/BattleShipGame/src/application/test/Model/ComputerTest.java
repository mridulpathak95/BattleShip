package application.test.Model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import application.Models.Computer;

public class ComputerTest {

	private Computer ob = null;

	@Before
	public void setUp() {
		ob = new Computer();

	}

	@After
	public void cleanUp() {
		ob = null;

	}

	/**
	 * 
	 * Test Case to verify if the score is increased after a hit or not
	 * 
	 */

	@Test
	public void getScoreCompTest() {
		// sets the score to update by 10 if a ship is hit

		ob.setScoreComp(10);

		assertEquals(10, ob.getScoreComp());

	}

	/**
	 * 
	 * Test case to check so that all 5 ships have been deployed or not
	 * 
	 */

	@Test
	public void deployUserGridTest() {
		// checks that all 5 ships are deployed or not
		ob.deployComputerShips();

		assertEquals(5, ob.getCounter());
	}

	/**
	 * 
	 * Test case to verify the Check method for ship placement is working
	 * 
	 */

	@Test
	public void checkTest() {
		// checks that all 5 ships are deployed or not
		String direction = "horizontal";

		// ship type is Cruiser hence points three
		int points = 3;
		int x = 5;
		int y = 5;

		boolean res = ob.check(x, y, direction, points);

		assertTrue(res);
	}
	
	
	/**
	 * 
	 * Test case to verify the Check method for ship placement is working
	 * 
	 */

	@Test
	public void checkTest2() {
		// checks that all 5 ships are deployed or not
		String direction = "Vertical";

		// ship type is Cruiser hence points three
		int points = 3;
		int x = 5;
		int y = 5;

		boolean res = ob.check(x, y, direction, points);

		assertTrue(res);
	}

	/**
	 * Test that the grid has been initialized correctly or not
	 * 
	 */
	@Test
	public void initializeTest() {
		boolean flag = true;

		int rows = 9;
		int cols = 11;

		Integer compGrid[][] = ob.getComputerGrid();

		for (int i = 0; i < rows; i++) {

			for (int j = 0; j < cols; j++) {

				if (!(compGrid[i][j] == 0)) {

					flag = false;
				}

			}
		}

		assertTrue(flag);
	}

	/**
	 * 
	 * Test that user has Won
	 * 
	 */
	@Test
	public void checkIfUserWonTest() {
		boolean flag = true;

		int rows = 9;
		int cols = 11;

		Integer compGrid[][] = ob.getComputerGrid();

		for (int i = 0; i < rows; i++) {

			for (int j = 0; j < cols; j++) {

				if (!(compGrid[i][j] == 0)) {

					flag = false;
				}

			}
		}

		ob.checkIfUserWon();

		assertTrue(flag);
	}

	/**
	 * 
	 * Checking the User 
	 * 
	 * 
	 */

	public void userTurnTest2() {

		ob.setScoreComp(-1);

		assertEquals(-1, ob.getScoreComp());

	}

	/**
	 * 
	 * Checking the setScoreComp for negative score method 
	 * 
	 * 
	 */
	@Test

	public void userTurnTest1() {

		ob.setScoreComp(-1);

		//Check the value is 9
		assertEquals(9, ob.getScoreComp());

	}

}
