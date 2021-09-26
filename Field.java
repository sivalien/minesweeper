import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Field {
    private int[][] field;
    private boolean[][] isOpened;
    private boolean[][] flag;
    private int fieldSize = 5;
    private final Random random = new Random();
    private int openedCells = 0;
    private int bombNumber = 0;
    private boolean firstMove = true;
    private FileWriter writer;

    Field(){}

    Field(FileWriter writer) throws IOException {
        this.writer = writer;
        field = new int[fieldSize][fieldSize];
        isOpened = new boolean[fieldSize][fieldSize];
        flag = new boolean[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                field[i][j] = 0;
                isOpened[i][j] = false;
                flag[i][j] = false;
            }
        }
        bombNumber = random.nextInt(4) + 2;
        printOpenedCells();
        writeOpenedCells();
    }

    Field(FileWriter writer, int fieldSize, int bombNumber) throws IOException {
        this.writer = writer;
        this.fieldSize = fieldSize;
        this.bombNumber = bombNumber;
        field = new int[fieldSize][fieldSize];
        isOpened = new boolean[fieldSize][fieldSize];
        flag = new boolean[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                field[i][j] = 0;
                isOpened[i][j] = false;
                flag[i][j] = false;
            }
        }
        printOpenedCells();
        writeOpenedCells();
    }

    private void putBombs(int x, int y) {
        int k = 0;
        while (k < bombNumber){
            int row = random.nextInt(fieldSize);
            int column = random.nextInt(fieldSize);
            if (field[row][column] == -1 || row == x && column == y)
                continue;
            k++;
            field[row][column] = -1;
            if (row - 1 >= 0 && column - 1 >= 0 && field[row - 1][column - 1] != -1) {
                field[row - 1][column - 1]++;
            }
            if (row - 1 >= 0 && field[row - 1][column] != -1) {
                field[row - 1][column]++;
            }
            if (row - 1 >= 0 && column + 1 < fieldSize && field[row - 1][column + 1] != -1) {
                field[row - 1][column + 1]++;
            }
            if (column - 1 >= 0 && field[row][column - 1] != -1) {
                field[row][column - 1]++;
            }
            if (column + 1 < fieldSize && field[row][column + 1] != -1) {
                field[row][column + 1]++;
            }
            if (row + 1 < fieldSize && column - 1 >= 0 && field[row + 1][column - 1] != -1) {
                field[row + 1][column - 1]++;
            }
            if (row + 1 < fieldSize && field[row + 1][column] != -1) {
                field[row + 1][column]++;
            }
            if (row + 1 < fieldSize && column + 1 < fieldSize && field[row + 1][column + 1] != -1) {
                field[row + 1][column + 1]++;
            }
        }
    }

    int getFieldSize() {
        return fieldSize;
    }

    private void openCell(int x, int y) {
        if (x < 0 || y < 0 || x >= fieldSize || y >= fieldSize) {
            return;
        }
        if (isOpened[x][y])
            return;
        isOpened[x][y] = true;
        openedCells++;
        if (field[x][y] != 0) {
            return;
        }
        openCell(x - 1, y - 1);
        openCell(x - 1, y);
        openCell(x - 1, y + 1);
        openCell(x, y - 1);
        openCell(x, y + 1);
        openCell(x + 1, y - 1);
        openCell(x + 1, y);
        openCell(x + 1, y + 1);
    }

    public boolean makeMove(int x, int y, String action) throws IOException {
        if (firstMove) {
            putBombs(x, y);
            firstMove = false;
        }
        if (isOpened[x][y] || flag[x][y]) {
            System.out.println("This cell has been already opened or marked as flag");
            writer.write("This cell has been already opened or marked as flag\n");
            return true;
        }
        else if ("Flag".equals(action)) {
            flag[x][y] = true;
            printOpenedCells();
            writeOpenedCells();
            return true;
        }
        else {
            if (field[x][y] == -1) {
                System.out.println("Game over!");
                writer.write("Game over!\n");
                printField();
                writeField();
                return false;
            }
            openCell(x, y);
            if (fieldSize * fieldSize - openedCells == bombNumber) {
                System.out.println("You win!");
                writer.write("You win!\n");
                printField();
                writeField();
                return false;
            }
            printOpenedCells();
            writeOpenedCells();
            return true;
        }
    }

    public void printOpenedCells() {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (flag[i][j])
                    System.out.print('f');
                else if (isOpened[i][j])
                    System.out.print(field[i][j]);
                else {
                    System.out.print('-');
                }
            }
            System.out.println();
        }
    }

    private void printField() {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                System.out.print(field[i][j] == -1 ? "*" : field[i][j]);
            }
            System.out.println();
        }
    }

    public int[][] getOpenedCells() {
        int[][] res = new int[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (isOpened[i][j])
                    res[i][j] = field[i][j];
                else
                    res[i][j] = -1;
            }
        }
        return res;
    }

    void writeOpenedCells() throws IOException {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (flag[i][j])
                    writer.write('f');
                else if (isOpened[i][j])
                    writer.write(field[i][j] + '0');
                else {
                    writer.write('-');
                }
            }
            writer.write('\n');
        }
    }

    void writeField() throws IOException {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (field[i][j] == -1)
                    writer.write("*");
                else
                    writer.write(field[i][j] + '0');
            }
            writer.write('\n');
        }
    }
}
