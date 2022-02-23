package game.testing;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import game.ThreeMusketeers;
import game.ThreeMusketeers.GameMode;

public class ThreeMusketeersTest {
	private static final ThreeMusketeers game = new ThreeMusketeers("Boards/Starter.txt");
	
	@BeforeClass
	public static void start() {
		game.play(GameMode.HumanRandom);
	}

	@Test
	public void runGame() {
		
	}

}
