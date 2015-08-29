package gol.patterns;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class LifReaderTest {

	private static final String EMPTY_LIF = "empty.lif";
	private static final int NO_PADDING = 0;
	private static final int DOUBLE_CELL_PADDING = 2;
	private static final String TEST_PATH = "test";
	private static final String RESOURCES_SUBPATH = "resources";
	
	private static final int SINGLE_CELL_PADDING = 1;
	
	private static final boolean ALIVE = true;
	private static final boolean DEAD = false;
	
	private static final String SHIFTED_TWO_CELLS_LIF = "shiftedTwoCells.lif";
	private static final String SHIFTED_SINGLE_CELL_LIF = "shiftedSingleCell.lif";
	private static final String TWO_CELLS_LIF = "twoCells.lif";
	private static final String COMMENTED_LINE_LIF = "commentedLine.lif";
	private static final String INVALID_LINE_LIF = "invalidLine.lif";
	private static final String SINGLE_CELL_LIF = "singleCell.lif";
	private static final String INVALID_FILE_PATH = "invalidFilePath";

	@Test(dataProvider = "filesAndContents")
	public void startGenerationReturnsFileContent(String file, boolean[][] content) throws IOException {
		Path path = getPath(file);
		LifReader reader = new LifReader(path, NO_PADDING);
		
		boolean[][] generation = reader.startGeneration();
				
		Assert.assertEquals(generation, content);
	}
	
	@Test(dataProvider = "shiftedFilesAndContents")
	public void startGenerationIgnoresShift(String file, boolean[][] content) throws IOException {
		Path path = getPath(file);
		LifReader reader = new LifReader(path, NO_PADDING);
		
		boolean[][] generation = reader.startGeneration();
				
		Assert.assertEquals(generation, content);
	}
	
	@Test(expectedExceptions = IOException.class)
	public void startGenerationThrowsIOExceptions() throws IOException {
		Path path = getPath(INVALID_FILE_PATH);
		LifReader reader = new LifReader(path, NO_PADDING);
		reader.startGeneration();
	}
	
	@Test
	public void startGenerationIgnoresComments() throws IOException {
		Path path = getPath(COMMENTED_LINE_LIF);
		LifReader reader = new LifReader(path, NO_PADDING);
		
		boolean[][] generation = reader.startGeneration();
		
		Assert.assertEquals(generation, new boolean[][] {{ ALIVE }});
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void constructorThrowsIllArgExceptionIfInvalidLineInFile() throws IOException {
		Path path = getPath(INVALID_LINE_LIF);
		new LifReader(path, NO_PADDING);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void constructorThrowsIllArgExceptionIfFileEmpty() throws IOException {
		Path path = getPath(EMPTY_LIF);
		new LifReader(path, NO_PADDING);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "getOutOfBoundsFiles")
	public void constructorThrowsIllArgExceptionIfCoordinatesOutOfBounds(String outOfBoundFile) throws IOException {
		Path path = getPath(outOfBoundFile);
		new LifReader(path, NO_PADDING);
	}
	
	@DataProvider
	public Object[][] getOutOfBoundsFiles() {
		return new Object[][] {
				{ "negativeOutOfBoundsX.lif" },
				{ "negativeOutOfBoundsY.lif" },
				{ "positiveOutOfBoundsX.lif" },
				{ "positiveOutOfBoundsY.lif" },
		};
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "getFilesWithAdditionalContent")
	public void constructorThrowsIllArgExceptionIfLineContainsNonCoordinateContent(String outOfBoundFile) throws IOException {
		Path path = getPath(outOfBoundFile);
		new LifReader(path, NO_PADDING);
	}
	
	@DataProvider
	public Object[][] getFilesWithAdditionalContent() {
		return new Object[][] {
				{ "additionalNonCoordinateLineContent.lif" },
				{ "multipleValuesPerLine.lif" },
		};
	}
	
	@Test(dataProvider = "getPaddingAndGeneration")
	public void startGenerationContainsPadding(int padding, boolean[][] expectedGeneration) throws IOException {
		Path path = getPath(SINGLE_CELL_LIF);
		LifReader reader = new LifReader(path, padding);
		
		boolean[][] generation = reader.startGeneration();
		
		Assert.assertEquals(generation, expectedGeneration);
	}
	
	@DataProvider
	public Object[][] getPaddingAndGeneration() {
		
		boolean[][] unpaddedGeneration = {
				{ ALIVE }
		};
		
		boolean[][] singlyPaddedGeneration = {
				{ DEAD, DEAD, DEAD }, 
				{ DEAD, ALIVE, DEAD }, 
				{ DEAD, DEAD, DEAD }
		};  
	
		boolean[][] doublyPaddedGeneration = {
				{ DEAD, DEAD, DEAD, DEAD, DEAD }, 
				{ DEAD, DEAD, DEAD, DEAD, DEAD },
				{ DEAD, DEAD, ALIVE, DEAD, DEAD }, 
				{ DEAD, DEAD, DEAD, DEAD, DEAD }, 
				{ DEAD, DEAD, DEAD, DEAD, DEAD },
		};  
		
		return new Object[][] {
				{ NO_PADDING, unpaddedGeneration },
				{ SINGLE_CELL_PADDING, singlyPaddedGeneration },
				{ DOUBLE_CELL_PADDING, doublyPaddedGeneration },
		};
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "getIllegalPaddings")
	public void startGenerationThrowsIllArgExceptionIfPaddingNegative(int padding) throws IOException {
		Path path = getPath(SINGLE_CELL_LIF);
		LifReader reader = new LifReader(path, padding);
		reader.startGeneration();
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void startGenerationThrowsIllArgExceptionIfPathNull() throws IOException {
		new LifReader(null, 1);
	}
	
	@DataProvider
	public Object[][] getIllegalPaddings() {
		return new Object[][] {
				{ -1 },
				{ -10 },
		};
	}
	
	private Path getPath(String fileName) {
		return Paths.get(TEST_PATH, RESOURCES_SUBPATH, fileName);
	}
	
	@DataProvider
	public Object[][] filesAndContents() {
		return new Object[][] {
				{ SINGLE_CELL_LIF, new boolean[][] {{ ALIVE }} },
				{ TWO_CELLS_LIF, new boolean[][] {{ALIVE, ALIVE}}}
		};
	}
	
	@DataProvider
	public Object[][] shiftedFilesAndContents() {
		return new Object[][] {
				{ SHIFTED_SINGLE_CELL_LIF, new boolean[][] {{ true }} },
				{ SHIFTED_TWO_CELLS_LIF, new boolean[][] {{true, true}}}
		};
	}
}
