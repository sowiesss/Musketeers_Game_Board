package lab2.testing;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import lab2.ThreeMusketeers;
import lab2.ThreeMusketeers.GameMode;

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
