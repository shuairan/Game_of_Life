package gol.patterns;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class LifReader implements Pattern {
	
	private static final String COMMENT_INDICATOR = "#";
	private static final String SEPARATOR = " ";
	private boolean[][] generation;

	public LifReader(Path path, int padding) throws IOException {
		this.generation = readGenerationFromFile(path);
	}

	private boolean[][] readGenerationFromFile(Path path) throws IOException {
		List<String> lines = Files.readAllLines(path);
		List<Coordinate> coordinates = extractCoordinates(lines);
				
		return createGenerationFromCoordinates(coordinates);
	}

	private boolean[][] createGenerationFromCoordinates(List<Coordinate> coordinates) {
		Coordinate maxCoordinate  = calculateMaxCoordinate(coordinates);
		Coordinate minCoordinate  = calculateMinCoordinate(coordinates);
		
		int sizeX = maxCoordinate.getX() - minCoordinate.getX() + 1;
		int sizeY = maxCoordinate.getY() - minCoordinate.getY() + 1;
		boolean[][] generation = new boolean[sizeX][sizeY];
		
		for (Coordinate coordinate : coordinates) {
			generation[coordinate.getX() - minCoordinate.getX()][coordinate.getY() - minCoordinate.getY()] = true;
		}
		
		return generation;
	}

	private Coordinate calculateMaxCoordinate(List<Coordinate> coordinates) {
		int max_x = Integer.MIN_VALUE;
		int max_y = Integer.MIN_VALUE;
		
		for (Coordinate coordinate : coordinates) {
			int x = coordinate.getX();
			int y = coordinate.getY();
			
			if (x >= max_x) { max_x = x; }
			if (y >= max_y) { max_y = y; }
		}
		
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

	@Override
	public boolean[][] startGeneration() {
		return generation;
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
