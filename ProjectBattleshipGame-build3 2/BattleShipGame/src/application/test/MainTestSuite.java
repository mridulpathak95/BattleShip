package application.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import application.test.Controllers.ControllerTestSuite;
import application.test.Model.ModelTestSuite;

/**
 * This is the main Test suite that is used to run other Test suite
 * 
 * @author Prateek
 *
 */

@RunWith(Suite.class)

@SuiteClasses({ ControllerTestSuite.class, ModelTestSuite.class })
public class MainTestSuite {

}
