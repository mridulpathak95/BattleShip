package application.test.Model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import application.Models.Computer;
import application.Models.SaveClass;

public class SaveClassTest {
	
	private SaveClass saveClass = null;

	@Before
	public void setUp() {
		saveClass = new SaveClass();

	}

	@After
	public void cleanUp() {
		saveClass = null;

	}

	/**
	 * 
	 * Test Case to verify if the file path is correct or not
	 * 
	 */

	@Test
	public void getFilePathTest() {
		// sets the score to update by 10 if a ship is hit

		assertEquals("User-Data\\Users-List.txt", saveClass.getFilePath());

	}
	
	/**
	 * Test case to verify if the folder path is correct or not
	 */
	@Test
	public void getFolderPathTest() {
		
		assertEquals("User-Data\\", saveClass.getFolderPath());
		
	}
	
	/**
	 * Test case to verify if the name is being correctly set
	 */
	@Test
	public void getUserNameTest() {
		saveClass.setuName("Sanju");
		
		assertEquals("Sanju", saveClass.getuName());	
	}
	
	/**
	 * Test to verify if the computer's hit coordinates were saved correctly or not
	 */
	@Test
	public void getComputerCoordinatesHitTest() {
		ArrayList<String> tempList = new ArrayList<>();
		tempList.add("4,5");
		saveClass.setComputerCoordinatesHit(tempList);
		
		assertEquals(tempList, saveClass.getComputerCoordinatesHit());
	}
	
	/**
	 * Test to verify if the user's hit coordinates were saved correctly or not
	 */
	@Test
	public void getUserCoordinatesHitTest() {
		ArrayList<String> tempList = new ArrayList<>();
		tempList.add("4,5");
		saveClass.setUserCoordinatesHit(tempList);
		
		assertEquals(tempList, saveClass.getUserCoordinatesHit());
	}
}
