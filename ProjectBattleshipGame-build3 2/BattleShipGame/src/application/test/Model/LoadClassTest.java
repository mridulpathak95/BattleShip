package application.test.Model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import application.Models.LoadClass;

public class LoadClassTest {

	private LoadClass loadClass = null;

	@Before
	public void setUp() {
		loadClass = new LoadClass();

	}

	@After
	public void cleanUp() {
		loadClass = null;

	}

	/**
	 * 
	 * Test Case to verify the coordinates and their state are correct
	 * 
	 */
	@Test
	public void radarGridCoordsTest() {
		
		int[] tempArr = {1, 2, 3};
		loadClass.setRadarGridCoordsT(tempArr[0], tempArr[1], tempArr[2]);
		assertArrayEquals(tempArr, loadClass.getRadarGridCoords());
		
	}
	
	/**
	 * 
	 * Test Case to verify whether the correct score is loaded or not
	 * 
	 */
	@Test
	public void scoreTest() {
		
		loadClass.setScoreT(10);
		assertEquals(10, loadClass.getScore());
		
	}
	
	/**
	 * 
	 * Test Case to verify whether the correct saves are loaded or not
	 * 
	 */

	@Test
	public void savesTest() {
		
		String[] save = {"abc", "def"};
		loadClass.setListOfSavesT(save);;	
		assertArrayEquals(save, loadClass.getListOfSaves());
		
	}
	
	/**
	 * 
	 * Test Case to verify whether the correct userScore is loaded or not
	 * 
	 */
	@Test
	public void userScoreTest() {
		
		loadClass.setUserScoreT(10);;
		assertEquals(10, loadClass.getUserScore());
		
	}
	
	/**
	 * Test case to verify whether the ship coordinates 
	 * are colored correctly or not
	 */
	@Test
	public void coloredShipsCoordsTest() {
		
		String[] coloredCoords = {"1", "2", "Carrier"};
		loadClass.setColoredShipsCoordT("1", "2", "Carrier");
		assertArrayEquals(coloredCoords, loadClass.getColoredShipsCoord());
	
	}
	
	/**
	 * Test case to verify whether the correct date is selected for loading the game
	 */
	@Test
	public void createdOnTest() {
		
		String createdOn = "Tue Aug 06 14:02:21 EDT 2019";
		loadClass.setCreatedOn(createdOn);
		assertEquals(createdOn, loadClass.getCreatedOn());
	
	}
	
}
