package game.testing;

import static org.junit.Assert.*;

import org.junit.Test;

import game.Board;
import game.HumanAgent;
import game.Move;

public class AgentTest {

	@Test
	public void testGetMove() {
		HumanAgent ha = new HumanAgent(new Board());
		Move m = ha.getMove();
	}
	
	

}
