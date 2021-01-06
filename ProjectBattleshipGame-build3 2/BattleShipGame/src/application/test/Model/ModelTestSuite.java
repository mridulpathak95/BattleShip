package application.test.Model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)

@SuiteClasses({ ComputerTest.class, PlayerTest.class, HitStrategyTest.class, SaveClassTest.class, LoadClassTest.class})
public class ModelTestSuite {

}
