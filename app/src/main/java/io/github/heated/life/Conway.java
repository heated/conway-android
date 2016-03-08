package io.github.heated.life;

public class Conway {
    public int width, height, totalGridSize;
    public boolean[] cells;
    public boolean torus = false;
    public int generations;

    public Conway(int width, int height) {
        this.width = width;
        this.height = height;
        totalGridSize = this.width * this.height;
        clearGrid();
    }

    public Conway(int width, int height, boolean[] cells) {
        this(width, height);
        this.cells = cells;
    }

    int pos(int x, int y) {
        return x * height + y;
    }

    boolean cell(int x, int y) {
        return cells[pos(x, y)];
    }

    void setCell(int x, int y, boolean alive) {
        cells[pos(x, y)] = alive;
    }

    void setCell(int x, int y) {
        setCell(x, y, true);
    }

    public void nextGeneration() {
        boolean[] newGrid = blankGrid();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newGrid[pos(x, y)] = cellLives(x, y);
            }
        }

        cells = newGrid;
        generations++;
    }

    int getNeighbors(int cellX, int cellY) {
        int neighbors = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = cellX + dx;
                int y = cellY + dy;

                if (torus) {
                    x = (x + width) % width;
                    y = (y + height) % height;
                }

                if (isLivingNeighbor(cellX, cellY, x, y)) {
                    neighbors++;
                }
            }
        }

        return neighbors;
    }

    boolean isLivingNeighbor(int x1, int y1, int x2, int y2) {
        return (x1 != x2 || y1 != y2) &&
                validPos(x2, y2)      &&
                cell(x2, y2);
    }

    boolean cellLives(int x, int y) {
        int neighbors = getNeighbors(x, y);

        return neighbors == 3 || (neighbors == 2 && cell(x, y));
    }

    boolean inRange(int a, int left, int right) {
        return left <= a && a < right;
    }

    boolean validPos(int x, int y) {
        return  inRange(x, 0, width) &&
                inRange(y, 0, height);
    }

    public void randomizeCells() {
        for (int i = 0; i < cells.length; i++) {
            cells[i] = Math.random() > .5;
        }

        generations = 0;
    }

    public void clearGrid() {
        cells = blankGrid();
        generations = 0;
    }

    public boolean[] blankGrid() {
        return new boolean[totalGridSize];
    }

    public void setGridToGlider() {
        clearGrid();
        setCell(1, 0);
        setCell(2, 1);
        setCell(0, 2);
        setCell(1, 2);
        setCell(2, 2);
    }

    public static Conway Random(int width, int height) {
        Conway conway = new Conway(width, height);
        conway.randomizeCells();
        return conway;
    }

    public void toggleTorus() {
        torus = !torus;
    }

    public void tryToSet(int x, int y, boolean alive) {
        if (validPos(x, y)) {
            setCell(x, y, alive);
        }
    }
}
