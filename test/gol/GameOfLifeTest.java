package gol;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GameOfLifeTest {

	private static final int MAX_STEPS = 1;
	private static final int INTERVAL = 1000;
	private static final int SIZE = 5;
	
	private MockDisplay display;
	
	@BeforeMethod
	public void init() {
		display = mockDisplay();
	}
	
	@DataProvider
	public Object[][] positiveInteger() {
		return new Object[][] {
			{1},
			{2},
			{5}
		};
	}
	
	@DataProvider
	public Object[][] illegalArguments() {
		return new Object[][] {
			{0, 1, 1},
			{1, 0, 1},
			{1, 1, 0},
		};
	}
	
	@Test(dataProvider="positiveInteger")
	public void testOneGenerationLifecycle(int maxStep) {
		GameOfLife game = GameOfLife.getBuilder()
				.withSize(SIZE)
				.withInterval(INTERVAL)
				.withMaxSteps(maxStep)
				.build();
		game.attach(display);
		
		game.run();
		
		assertEquals(display.getLifecycle(), maxStep);
	}
	
	@Test(dataProvider="illegalArguments", expectedExceptions=IllegalArgumentException.class)
	public void constructorThrowsIllArgException(int size, int interval, int maxSteps) {
		GameOfLife.getBuilder()
				.withSize(size)
				.withInterval(interval)
				.withMaxSteps(maxSteps)
				.build();
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void builderThrowsIllStateExceptionIfSizeNotSet() {
		GameOfLife.getBuilder()
			.withInterval(1000)
			.withMaxSteps(3)
			.build();
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void builderThrowsIllStateExceptionIfIntervalNotSet() {
		GameOfLife.getBuilder()
			.withInterval(1000)
			.withSize(5)
			.build();
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void builderThrowsIllStateExceptionIfMaxStepsNotSet() {
		GameOfLife.getBuilder()
			.withSize(5)
			.withMaxSteps(3)
			.build();
	}
	
	@Test(dataProvider="positiveInteger")
	public void generationHasExpectedSize(int size) {
		GameOfLife game = GameOfLife.getBuilder()
				.withSize(size)
				.withInterval(INTERVAL)
				.withMaxSteps(MAX_STEPS)
				.build();
		game.attach(display);
		
		game.run();
		boolean[][] generation = display.getGeneration();
		
		assertEquals(generation.length, size);
		assertEquals(generation[0].length, size);
	}
	
	private MockDisplay mockDisplay() {
		return new MockDisplay() { 
			private boolean[][] generation;
			private int lifecycle = 0;
			
			public void refresh(boolean[][] generation) {
				this.generation = generation;
				lifecycle++;
			}
			
			public boolean[][] getGeneration() {
				return generation;
			}
			
			public int getLifecycle() {
				return lifecycle;
			}
		};
	}
	
}
