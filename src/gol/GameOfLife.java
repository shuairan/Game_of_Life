package gol;

import gol.patterns.Pattern;

import java.util.HashSet;
import java.util.Set;

public class GameOfLife {

	Set<Display> displays = new HashSet<>();
	
	private int maxSteps;
	private int interval;
	private boolean[][] initialGeneration;
	
	private GameOfLife(int interval, int maxSteps, boolean[][] initialGeneration) {
		this.interval = interval;	
		this.maxSteps = maxSteps;
		this.initialGeneration = initialGeneration;
	}

	public static GameBuilder getBuilder() {
		return new GameBuilder();
	}
	
	public void attach(Display display) {
		displays.add(display);
	}

	public void run() {
		
		for (int i = 0; i < maxSteps; i++) {
			//TODO: Regeln anwenden
			updateDisplays();			
		}
	}

	private void updateDisplays() {
		for (Display display: displays) {
			display.refresh(this.initialGeneration);
		}
	}
	
	public static class GameBuilder {
		private Integer interval;
		private Integer maxSteps;
		private boolean[][] initialGeneration = null;
		
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
		public GameBuilder withPattern(Pattern pattern) {
			if (pattern == null) {
				throw new IllegalArgumentException();
			}
			this.initialGeneration = pattern.startGeneration();
			return this;
		}
		public GameOfLife build() {
			if (interval == null || maxSteps == null) {
				throw new IllegalStateException();
			}
			if (initialGeneration == null) {
				throw new IllegalStateException();
			}

			return new GameOfLife(interval, maxSteps, initialGeneration);
		}
	}
}
