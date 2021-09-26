import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Solver {
    private Field field;
    private boolean[][] isOpened;
    private boolean[][] flag;
    private int[][] values;
    private int fieldSize;
    private int bombNumber;
    private int flagNumber = 0;
    private FileWriter writer;

    Solver(FileWriter writer, int fieldSize, int bombNumber) throws IOException {
        this.writer = writer;
        this.fieldSize = fieldSize;
        this.bombNumber = bombNumber;
        this.field = new Field(writer, fieldSize, bombNumber);
        isOpened = new boolean[fieldSize][fieldSize];
        values = new int[fieldSize][fieldSize];
        flag = new boolean[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                values[i][j] = -1;
                isOpened[i][j] = false;
                flag[i][j] = false;
            }
        }
    }


    void solve() throws IOException {
        Random random = new Random();
        int x = random.nextInt(fieldSize);
        int y = random.nextInt(fieldSize);
        System.out.println(x + " " + y + " Open");
        writer.write(x + " " + y + "Open" + "\n");
        if (!field.makeMove(x, y, "Open"))
            return;
        values = field.getOpenedCells();
        while (true) {
            boolean changed = false;
            for (int i = 0; i < fieldSize; i++) {
                for (int j = 0; j < fieldSize; j++) {
                    if (values[i][j] != -1 && values[i][j] != 0) {
                        int closed = 0;
                        int flaged = 0;
                        for (int k = i - 1; k < i + 2; k++) {
                            for (int l = j - 1; l < j + 2; l++) {
                                if (k >= 0 && k < fieldSize && l >= 0 && l < fieldSize && (k != i || l != j)) {
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
                                    if (k >= 0 && k < fieldSize && l >= 0 && l < fieldSize && (k != i || l != j) && values[k][l] == -1 && !flag[k][l]) {
                                        changed = true;
                                        System.out.println(k + " " + l + " Flag");
                                        writer.write(k + " " + l + "Flag" + "\n");
                                        field.makeMove(k, l, "Flag");
                                        flagNumber++;
                                        flag[k][l] = true;
                                    }
                                }
                            }
                        }
                        if (flaged == values[i][j]) {
                            for (int k = i - 1; k < i + 2; k++) {
                                for (int l = j - 1; l < j + 2; l++) {
                                    if (k >= 0 && k < fieldSize && l >= 0 && l < fieldSize && (k != i || l != j) && !flag[k][l] && values[k][l] == -1) {
                                        changed = true;
                                        System.out.println(k + " " + l + " Open");
                                        writer.write(k + " " + "Open" + "\n");
                                        if (!field.makeMove(k, l, "Open")) {
                                            return;
                                        }
                                    }
                                }
                            }
                            values = field.getOpenedCells();
                        }
                    }
                }
            }
            if (!changed) {
                for (int i = 0; i < fieldSize; i++) {
                    for (int j = 0; j < fieldSize; j++) {
                        if (values[i][j] == -1 && !flag[i][j] && !putBomb(i, j)) {
                            System.out.println(i + " " + j + " Open potential");
                            writer.write(x + " " + y + "Open" + "\n");
                            if (!field.makeMove(i, j, "Open"))
                                return;
                            values = field.getOpenedCells();
                            changed = true;
                        }
                    }
                }
            }
            if (!changed) {
                while (true) {
                    x = random.nextInt(fieldSize);
                    y = random.nextInt(fieldSize);
                    if (values[x][y] == -1 && !flag[x][y]) {
                        System.out.println(x + " " + y + " Open random");
                        writer.write(x + " " + y + "Open" + "\n");
                        if (!field.makeMove(x, y, "Open"))
                            return;
                        values = field.getOpenedCells();
                        break;
                    }
                }
            }
        }
    }

    boolean putBomb(int x, int y) {
        boolean[][] potentialBombs = new boolean[fieldSize][fieldSize];
        boolean[][] potentialNumbers = new boolean[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                potentialBombs[i][j] = false;
                potentialNumbers[i][j] = false;
            }
        }
        potentialBombs[x][y] = true;
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (values[i][j] != -1 && values[i][j] != 0) {
                    int closed = 0;
                    int flaged = 0;
                    for (int k = i - 1; k < i + 2; k++) {
                        for (int l = j - 1; l < j + 2; l++) {
                            if (k >= 0 && l >= 0 && k < fieldSize && l < fieldSize && (k != i || l != j)) {
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
                                if (k >= 0 && k < fieldSize && l >= 0 && l < fieldSize && (k != i || l != j) && values[k][l] == -1 && !flag[k][l] && !potentialBombs[k][l] && !potentialNumbers[k][l]) {
                                    potentialBombs[k][l] = true;
                                }
                            }
                        }
                    }
                    if (flaged == values[i][j]) {
                        for (int k = i - 1; k < i + 2; k++) {
                            for (int l = j - 1; l < j + 2; l++) {
                                if (k >= 0 && k < fieldSize && l >= 0 && l < fieldSize && (k != i || l != j) && !flag[k][l] && values[k][l] == -1 && !potentialBombs[k][l] && !potentialNumbers[k][l]) {
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
