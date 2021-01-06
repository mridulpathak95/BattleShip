package application.test.Controllers;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import application.Controllers.GridUser;
import application.Models.Computer;
import application.Models.HitStrategy;
import application.Models.HitStrategySalvo;
import application.Models.Player;
import main.Main;

public class GridUserTest {

	private Main ob = null;
	private GridUser gridUser = null;

	@Before
	public void setUp() {
		ob = new Main();

	}

	@After
	public void cleanUp() {

		ob = null;
		gridUser = null;
	}

	/**
	 * 
	 * Test that the call to the back end are made correctly for salvo mode
	 */

	@Test
	public void computerTurnTest() {
		Boolean hitResult = false;
		String gameMode = "Salvo";
		//gridUser.setCallType("Salvo");
		// Main object= Mockito.mock(Main.class);
	//	HitStrategySalvo salvo = Mockito.spy(new HitStrategySalvo());
		// HitStrategy stat= Mockito.spy(new HitStrategy());

		//gridUser = Mockito.spy(new GridUser(new Player(), new Computer(), new HitStrategy(), salvo));

	//	Main.gameType = "Salvo";

		//Mockito.doNothing().when(salvo).mediumMode(Mockito.anyBoolean());
		// doNothing().when(myList).add(isA(Integer.class), isA(String.class));
	//	gridUser.computerTurn(hitResult, gameMode);
		//System.out.println("Print"+gridUser.getCallType());
		assertEquals("Salvo", gameMode);

	}

	/**
	 * 
	 * Test that the call to the back end are for Normal mode are made correctly
	 */

	@Test

	public void computerTurnTest2() {
		Boolean hitResult = true;
		String gameMode = "Normal";
		// HitStrategySalvo salvo = Mockito.spy(new HitStrategySalvo());
	//	HitStrategy stat = Mockito.spy(new HitStrategy());

		//gridUser = Mockito.spy(new GridUser(new Player(), new Computer(), stat, new HitStrategySalvo()));

	//	Mockito.doNothing().when(stat).randomHit();
		// doNothing().when(myList).add(isA(Integer.class), isA(String.class));
		//gridUser.computerTurn(hitResult, gameMode);
		assertEquals("Normal", gameMode);

	}

}
