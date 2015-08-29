package gol.patterns;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A Pattern creating a generation from a .LIF 1.06 file
 */
public class LifReader implements Pattern {
	
	private static final boolean ALIVE = true;
	private static final String COMMENT_INDICATOR = "#";
	private static final String SEPARATOR = " ";
	
	private static final String COORDINATE_REGEX = "-??\\d+\\s+-??\\d+";
	
	private boolean[][] generation;

	/**
	 * Creates a new Pattern using the provided Path of a LIF 1.06 file and a given amount of padding cells
	 * @param path The Path of the LIF 1.06 file
	 * @param padding The amount of padding cell rows and lines to add around the pattern given in the file
	 * @throws IOException Forwards all IOExceptions from the underlying File IO.
	 */
	public LifReader(Path path, int padding) throws IOException {
		checkInputParameters(path, padding);
		
		this.generation = readGenerationFromFile(path, padding);
	}
	
	@Override
	public boolean[][] startGeneration() {
		return generation;
	}

	private boolean[][] readGenerationFromFile(Path path, int padding) throws IOException {
		List<String> lines = Files.readAllLines(path);
		List<Coordinate> coordinates = extractCoordinates(lines);
		
		if (coordinates.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("File %s contained no valid cell definitions", path.getFileName()));
		}
				
		return createGenerationFromCoordinates(coordinates, padding);
	}

	private boolean[][] createGenerationFromCoordinates(List<Coordinate> coordinates, int padding) {
		Coordinate maxCoordinate  = calculateMaxCoordinate(coordinates, padding);
		Coordinate minCoordinate  = calculateMinCoordinate(coordinates);
		
		int sizeX = maxCoordinate.getX() - minCoordinate.getX() + 1;
		int sizeY = maxCoordinate.getY() - minCoordinate.getY() + 1;
		boolean[][] generation = new boolean[sizeX][sizeY];
		
		for (Coordinate coordinate : coordinates) {
			int normalizedPositionX = coordinate.getX() - minCoordinate.getX();
			int normalizedPositionY = coordinate.getY() - minCoordinate.getY();
			
			int positionWithPaddingX = normalizedPositionX + padding;
			int positionWithPaddingY = normalizedPositionY + padding;
			
			generation[positionWithPaddingX][positionWithPaddingY] = ALIVE;
		}
		
		return generation;
	}

	private Coordinate calculateMaxCoordinate(List<Coordinate> coordinates, int padding) {
		int max_x = Integer.MIN_VALUE;
		int max_y = Integer.MIN_VALUE;
		
		for (Coordinate coordinate : coordinates) {
			int x = coordinate.getX();
			int y = coordinate.getY();
			
			if (x >= max_x) { max_x = x; }
			if (y >= max_y) { max_y = y; }
		}
		
		max_x += padding * 2;
		max_y += padding * 2;
		
		return new Coordinate(max_x, max_y);
	}
	
	private Coordinate calculateMinCoordinate(List<Coordinate> coordinates) {
		int min_x = Integer.MAX_VALUE;
		int min_y = Integer.MAX_VALUE;
		
		for (Coordinate coordinate : coordinates) {
			int x = coordinate.getX();
			int y = coordinate.getY();
			
			if (x <= min_x) { min_x = x; }
			if (y <= min_y) { min_y = y; }
		}
		
		return new Coordinate(min_x, min_y);
	}

	private List<Coordinate> extractCoordinates(List<String> lines) {
		return lines.stream()
					.filter(line -> !line.startsWith(COMMENT_INDICATOR))
					.map(line -> buildCoordinate(line))
					.collect(Collectors.toList());
	}
	
	private Coordinate buildCoordinate(String line) {
		if (!line.matches(COORDINATE_REGEX)) {
			throw new IllegalArgumentException(String.format("Invalid line: '%s'", line));
		}
		
		String[] coordinateStrings = line.split(SEPARATOR);
		try {
			int x = Integer.parseInt(coordinateStrings[0]);
			int y = Integer.parseInt(coordinateStrings[1]);
			return new Coordinate(x, y);
		}
		catch (NumberFormatException ex) {
			throw new IllegalArgumentException(String.format("Invalid coordinate: '%s'", line));
		}
	}
	
	private void checkInputParameters(Path path, int padding) {
		if (path == null) {
			throw new IllegalArgumentException("Path may not be null");
		}
		else if (padding < 0) {
			throw new IllegalArgumentException("Padding may not be negative");
		}
	}
	
	private static class Coordinate {
		private int x;
		private int y;
		
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
		
	}

}
