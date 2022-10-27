import java.util.ArrayList;
import java.util.Random;

public class Solver {
    private final Grid grid;
    private boolean[][] isOpened;
    private boolean[][] flag;
    private int[][] values;
    private final int gridSize;
    private final int bombNumber;
    private int flagNumber = 0;

    Solver(Grid grid, int gridSize, int bombNumber) {
        this.grid = grid;
        this.gridSize = gridSize;
        this.bombNumber = bombNumber;
        isOpened = new boolean[gridSize][gridSize];
        values = new int[gridSize][gridSize];
        flag = new boolean[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                values[i][j] = -1;
                isOpened[i][j] = false;
                flag[i][j] = false;
            }
        }
    }

    private void refreshValues() {
        ArrayList<int []> opened = grid.getOpenedCells();
        for (int[] cell:
             opened) {
             values[cell[0]][cell[1]] = cell[2];
        }
    }


    void solve() {
        Random random = new Random();
        int x = random.nextInt(gridSize);
        int y = random.nextInt(gridSize);
        System.out.println(x + " " + y + " Open");
        if (!grid.openCell(x, y)) {
            return;
        }
        refreshValues();
        while (true) {
            boolean changed = false;
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if (values[i][j] != -1 && values[i][j] != 0) {
                        int closed = 0;
                        int flaged = 0;
                        for (int k = i - 1; k < i + 2; k++) {
                            for (int l = j - 1; l < j + 2; l++) {
                                if (k >= 0 && k < gridSize && l >= 0 && l < gridSize && (k != i || l != j)) {
                                    if (values[k][l] == -1 && !flag[k][l])
                                        closed++;
                                    if (flag[k][l])
                                        flaged++;
                                }
                            }
                        }
                        if (closed == values[i][j] - flaged) {
                            for (int k = i - 1; k < i + 2; k++) {
                                for (int l = j - 1; l < j + 2; l++) {
                                    if (k >= 0 && k < gridSize && l >= 0 && l < gridSize && (k != i || l != j) && values[k][l] == -1 && !flag[k][l]) {
                                        changed = true;
                                        System.out.println(k + " " + l + " Flag");
                                        flagNumber++;
                                        flag[k][l] = true;
                                    }
                                }
                            }
                        }
                        if (flaged == values[i][j]) {
                            for (int k = i - 1; k < i + 2; k++) {
                                for (int l = j - 1; l < j + 2; l++) {
                                    if (k >= 0 && k < gridSize && l >= 0 && l < gridSize && (k != i || l != j) && !flag[k][l] && values[k][l] == -1) {
                                        changed = true;
                                        System.out.println(k + " " + l + " Open");
                                        if (!grid.openCell(k, l)) {
                                            return;
                                        }
                                    }
                                }
                            }
                            refreshValues();
                        }
                    }
                }
            }
            if (!changed) {
                for (int i = 0; i < gridSize; i++) {
                    for (int j = 0; j < gridSize; j++) {
                        if (values[i][j] == -1 && !flag[i][j] && !putBomb(i, j)) {
                            System.out.println(i + " " + j + " Open potential");
                            if (!grid.openCell(i, j))
                                return;
                            refreshValues();
                            changed = true;
                        }
                    }
                }
            }
            if (!changed) {
                while (true) {
                    x = random.nextInt(gridSize);
                    y = random.nextInt(gridSize);
                    if (values[x][y] == -1 && !flag[x][y]) {
                        System.out.println(x + " " + y + " Open random");
                        if (!grid.openCell(x, y))
                            return;
                        refreshValues();
                        break;
                    }
                }
            }
        }
    }

    boolean putBomb(int x, int y) {
        boolean[][] potentialBombs = new boolean[gridSize][gridSize];
        boolean[][] potentialNumbers = new boolean[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                potentialBombs[i][j] = false;
                potentialNumbers[i][j] = false;
            }
        }
        potentialBombs[x][y] = true;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (values[i][j] != -1 && values[i][j] != 0) {
                    int closed = 0;
                    int flaged = 0;
                    for (int k = i - 1; k < i + 2; k++) {
                        for (int l = j - 1; l < j + 2; l++) {
                            if (k >= 0 && l >= 0 && k < gridSize && l < gridSize && (k != i || l != j)) {
                                if (values[k][l] == -1 && !flag[k][l] && !potentialBombs[k][l] && !potentialNumbers[k][l])
                                    closed++;
                                if (flag[k][l] || potentialBombs[k][l]) {
                                    flaged++;
                                    System.out.println(k + " " + l);
                                }
                            }
                        }
                    }
                    System.out.println(x + " " + y + " " + i + " " + j + " " + closed + " " + flaged);
                    if (closed + flaged < values[i][j] || flaged > values[i][j])
                        return false;
                    if (closed == values[i][j] - flaged) {
                        for (int k = i - 1; k < i + 2; k++) {
                            for (int l = j - 1; l < j + 2; l++) {
                                if (k >= 0 && k < gridSize && l >= 0 && l < gridSize && (k != i || l != j) && values[k][l] == -1 && !flag[k][l] && !potentialBombs[k][l] && !potentialNumbers[k][l]) {
                                    potentialBombs[k][l] = true;
                                }
                            }
                        }
                    }
                    if (flaged == values[i][j]) {
                        for (int k = i - 1; k < i + 2; k++) {
                            for (int l = j - 1; l < j + 2; l++) {
                                if (k >= 0 && k < gridSize && l >= 0 && l < gridSize && (k != i || l != j) && !flag[k][l] && values[k][l] == -1 && !potentialBombs[k][l] && !potentialNumbers[k][l]) {
                                    potentialNumbers[k][l]  = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}

