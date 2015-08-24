package gol;

import static org.testng.Assert.*;
import gol.patterns.Pattern;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GameOfLifeTest {

	private static final int SIZE = 5;
	private static final int MAX_STEPS = 1;
	private static final int INTERVAL = 1000;
	
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
			{0, 1},
			{1, 0},
		};
	}
	
	@Test(dataProvider="positiveInteger")
	public void testOneGenerationLifecycle(int maxStep) {
		GameOfLife game = GameOfLife.getBuilder()
				.withInterval(INTERVAL)
				.withMaxSteps(maxStep)
				.withPattern(mockSquarePattern(SIZE))
				.build();
		game.attach(display);
		
		game.run();
		
		assertEquals(display.getLifecycle(), maxStep);
	}
	
	@Test(dataProvider="illegalArguments", expectedExceptions=IllegalArgumentException.class)
	public void constructorThrowsIllArgException(int interval, int maxSteps) {
		GameOfLife.getBuilder()
				.withInterval(interval)
				.withMaxSteps(maxSteps)
				.build();
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void builderThrowsIllStateExceptionIfIntervalNotSet() {
		GameOfLife.getBuilder()
			.withMaxSteps(MAX_STEPS)
			.withPattern(mockSquarePattern(SIZE))
			.build();
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void builderThrowsIllStateExceptionIfMaxStepsNotSet() {
		GameOfLife.getBuilder()
			.withInterval(INTERVAL)
			.withPattern(mockSquarePattern(SIZE))
			.build();
	}
	
	@Test(expectedExceptions=IllegalStateException.class)
	public void builderThrowsIllStateExceptionIfNoInitialGenerationSet() {
		GameOfLife.getBuilder()
			.withInterval(INTERVAL)
			.withMaxSteps(MAX_STEPS)
			.build();
	}
	
	@Test(dataProvider="positiveInteger")
	public void generationHasExpectedSize(int size) {
		GameOfLife game = GameOfLife.getBuilder()
				.withInterval(INTERVAL)
				.withMaxSteps(MAX_STEPS)
				.withPattern(mockSquarePattern(size))
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
	
	private Pattern mockSquarePattern(int size) {
		return new Pattern() {
			@Override
			public boolean[][] startGeneration() {
				return new boolean[size][size];
			}
		};
	}
	
}
