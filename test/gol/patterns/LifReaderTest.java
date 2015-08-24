package gol.patterns;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class LifReaderTest {

	private static final String TEST_PATH = "test";
	private static final String RESOURCES_SUBPATH = "resources";
	private static final String INVALID_FILE_PATH = "invalidFilePath";

	@Test(dataProvider = "filesAndContents")
	public void startGenerationReturnsFileContent(String file, boolean[][] content) throws IOException {
		Path path = Paths.get(TEST_PATH, RESOURCES_SUBPATH, file);
		LifReader reader = new LifReader(path, 0);
		boolean[][] generation = reader.startGeneration();
				
		Assert.assertEquals(generation, content);
	}
	
	@Test(dataProvider = "shiftedFilesAndContents")
	public void startGenerationIgnoresShift(String file, boolean[][] content) throws IOException {
		Path path = Paths.get(TEST_PATH, RESOURCES_SUBPATH, file);
		LifReader reader = new LifReader(path, 0);
		boolean[][] generation = reader.startGeneration();
				
		Assert.assertEquals(generation, content);
	}
	
	@Test(expectedExceptions = IOException.class)
	public void startGenerationThrowsIOException() throws IOException {
		Path path = Paths.get(TEST_PATH, RESOURCES_SUBPATH, INVALID_FILE_PATH);
		LifReader reader = new LifReader(path, 0);
		reader.startGeneration();
	}
	
	@Test
	public void startGenerationIgnoresComments() throws IOException {
		Path path = Paths.get(TEST_PATH, RESOURCES_SUBPATH, "commentedLine.lif");
		LifReader reader = new LifReader(path, 0);
		boolean[][] generation = reader.startGeneration();
		
		Assert.assertEquals(generation, new boolean[][] {{ true }});
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void startGenerationThrowsIllArgExceptionInvalidLineInFile() throws IOException {
		Path path = Paths.get(TEST_PATH, RESOURCES_SUBPATH, "invalidLine.lif");
		LifReader reader = new LifReader(path, 0);
		reader.startGeneration();
	}
	
	@DataProvider
	public Object[][] filesAndContents() {
		return new Object[][] {
				{ "singleCell.lif", new boolean[][] {{ true }} },
				{ "twoCells.lif", new boolean[][] {{true, true}}}
		};
	}
	
	@DataProvider
	public Object[][] shiftedFilesAndContents() {
		return new Object[][] {
				{ "shiftedSingleCell.lif", new boolean[][] {{ true }} },
				{ "shiftedTwoCells.lif", new boolean[][] {{true, true}}}
		};
	}
}
