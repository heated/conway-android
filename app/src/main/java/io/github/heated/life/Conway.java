package io.github.heated.life;

public class Conway {
    public int gridWidth;
    public int gridHeight;
    public boolean[][] grid;
    public boolean torus = false;

    public Conway(int width, int height) {
        gridWidth = width;
        gridHeight = height;
        grid = new boolean[gridWidth][gridHeight];
    }

    public Conway(int width, int height, boolean[] serializedGrid) {
        this(width, height);
        deSerializeGrid(serializedGrid);
    }

    public void nextGeneration() {
        boolean[][] newGrid = new boolean[gridWidth][gridHeight];

        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                newGrid[x][y] = cellLives(x, y);
            }
        }

        grid = newGrid;
    }

    int getNeighbors(int cellX, int cellY) {
        int neighbors = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = cellX + dx;
                int y = cellY + dy;

                if (torus) {
                    x = (x + gridWidth) % gridWidth;
                    y = (y + gridHeight) % gridHeight;
                }

                if ((dx != 0 || dy != 0) &&
                        validPos(x, y)       &&
                        grid[x][y]) {

                    neighbors++;
                }
            }
        }

        return neighbors;
    }

    boolean cellLives(int x, int y) {
        int neighbors = getNeighbors(x, y);

        return neighbors == 3 || (neighbors == 2 && grid[x][y]);
    }

    boolean inRange(int a, int left, int right) {
        return left <= a && a < right;
    }

    boolean validPos(int x, int y) {
        return  inRange(x, 0, gridWidth) &&
                inRange(y, 0, gridHeight);
    }

    public void randomizeGrid() {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                grid[x][y] = Math.random() > .5;
            }
        }
    }

    public void clearGrid() {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                grid[x][y] = false;
            }
        }
    }

    public void setGridToGlider() {
        clearGrid();
        grid[1][0] = true;
        grid[2][1] = true;
        grid[0][2] = true;
        grid[1][2] = true;
        grid[2][2] = true;
    }

    public boolean[] serializeGrid() {
        boolean[] result = new boolean[gridWidth * gridHeight];
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                result[x * gridHeight + y] = grid[x][y];
            }
        }

        return result;
    }

    public void deSerializeGrid(boolean[] serializedGrid) {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                grid[x][y] = serializedGrid[x * gridHeight + y];
            }
        }
    }

    public static Conway Random(int width, int height) {
        Conway conway = new Conway(width, height);
        conway.randomizeGrid();
        return conway;
    }

    public void toggleTorus() {
        torus = !torus;
    }

    public void tryToSet(int x, int y, boolean alive) {
        if (validPos(x, y)) {
            grid[x][y] = alive;
        }
    }
}
