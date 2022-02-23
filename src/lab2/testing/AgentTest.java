package lab2.testing;

import static org.junit.Assert.*;

import org.junit.Test;

import lab2.Board;
import lab2.HumanAgent;
import lab2.Move;

public class AgentTest {

	@Test
	public void testGetMove() {
		HumanAgent ha = new HumanAgent(new Board());
		Move m = ha.getMove();
	}
	
	

}
