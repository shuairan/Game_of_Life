package gol;

import java.util.HashSet;
import java.util.Set;

public class GameOfLife {

	Set<Display> displays = new HashSet<>();
	
	private int maxSteps;
	private int interval;
	private boolean[][] initialGeneration;
	
	private GameOfLife(int size, int interval, int maxSteps) {
		this.interval = interval;	
		this.maxSteps = maxSteps;

	}

	public static GameBuilder getBuilder() {
		return new GameBuilder();
	}
	
	public void attach(Display display) {
		displays.add(display);
	}

	public void run() {
		for (int i = 0; i < maxSteps; i++) {
			updateDisplays();			
		}
	}

	private void updateDisplays() {
		for (Display display: displays) {
			display.refresh(new boolean[1][1]);
		}
	}
	
	public static class GameBuilder {
		private Integer size;
		private Integer interval;
		private Integer maxSteps;
		private boolean[][] initialGeneration = null;
		
		public GameBuilder withSize(int size) {
			if (size < 1) {
				throw new IllegalArgumentException();
			}
			this.size = size;
			return this;
		}
		public GameBuilder withInterval(int interval) {
			if (interval < 1) {
				throw new IllegalArgumentException();
			}
			this.interval = interval;
			return this;
		}
		public GameBuilder withMaxSteps(int maxSteps) {
			if (maxSteps < 1) {
				throw new IllegalArgumentException();
			}
			this.maxSteps = maxSteps;
			return this;
		}
		public GameOfLife build() {
			if (size == null || interval == null || maxSteps == null) {
				throw new IllegalStateException();
			}

			return new GameOfLife(size, interval, maxSteps);
		}
	}
}
