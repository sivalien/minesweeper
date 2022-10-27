import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;


public class Grid extends JPanel implements MouseListener {
    public ArrayList<Cell> cells = new ArrayList<>();
    private final int size;
    private final int bombNumber;
    private int discoveredCells = 0;
    private ArrayList<int[]> openedCells = new ArrayList<int[]>();

    public Grid(GridLayout g, int size, int bombNumber) {
        super(g);
        this.size = size;
        this.bombNumber = bombNumber;
        createCells();
        putBombs();
        addCells();
    }

    private void createCells() {
        for (int i = 0; i < this.size * this.size; i++) {
            cells.add(new Cell(i));
            cells.get(i).addMouseListener(this);
        }
    }

    private void putBombs() {
        int i = 0;
        Random random = new Random();
        while (i < bombNumber) {
            int position = random.nextInt(this.size * this.size);
            if (cells.get(position).isBomb()) {
                continue;
            }
            cells.get(position).putBomb();
            if (position % size != size - 1 && !cells.get(position + 1).isBomb()) {
                cells.get(position + 1).increaseValue();
            }
            if (position % size != 0 && !cells.get(position - 1).isBomb()) {
                cells.get(position - 1).increaseValue();
            }
            if (position < size * (size - 1) && !cells.get(position + this.size).isBomb()) {
                cells.get(position + size).increaseValue();
            }
            if (position >= size && !cells.get(position - this.size).isBomb()) {
                cells.get(position - this.size).increaseValue();
            }
            if (position % size != size - 1 && position < size * (size - 1) && !cells.get(position + this.size + 1).isBomb()) {
                cells.get(position + this.size + 1).increaseValue();
            }
            if (position % size != 0 && position < size * (size - 1) && !cells.get(position + this.size - 1).isBomb()) {
                cells.get(position + this.size - 1).increaseValue();
            }
            if (position % size != size - 1 && position >= size && !cells.get(position - this.size + 1).isBomb()) {
                cells.get(position - this.size + 1).increaseValue();
            }
            if (position % size != 0 && position >= size && !cells.get(position - this.size - 1).isBomb()) {
                cells.get(position - this.size - 1).increaseValue();
            }
            i++;
        }
    }

    private void addCells() {
        for (Cell cell:
             cells) {
            this.add(cell);
        }
    }

    public ArrayList<int[]> getOpenedCells() {
        return openedCells;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Cell cell = (Cell) e.getSource();
        if(SwingUtilities.isRightMouseButton(e)) {
            cell.flag();
        } else {
            openCell(cell);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public boolean openCell(int x, int y) {
        openCell(cells.get(x * size + y));
        if (cells.get(x * size + y).isBomb() || this.size * this.size - discoveredCells == bombNumber) {
            return false;
        }
        return true;
    }

    private void openCell(Cell cell) {
        if (cell.isFlagged() || cell.isDiscovered())
            return;
        if (cell.isBomb()) {
            openAllCells();
            Window.sendMessage("You lose :c");
            return;
        }
        cell.discover();
        openedCells.add(new int[]{cell.getPosition() / size, cell.getPosition() % size, cell.getValue()});
        discoveredCells++;
        if (this.size * this.size - discoveredCells == bombNumber) {
            openAllCells();
            Window.sendMessage("You win!!!!!");
            return;
        }
        else if (cell.getValue() == 0) {
            int position = cell.getPosition();
            if (position % size != size - 1 && !cells.get(position + 1).isBomb()) {
                openCell(cells.get(position + 1));
            }
            if (position % size != 0 && !cells.get(position - 1).isBomb()) {
                openCell(cells.get(position - 1));
            }
            if (position < size * (size - 1) && !cells.get(position + this.size).isBomb()) {
                openCell(cells.get(position + size));
            }
            if (position >= size && !cells.get(position - this.size).isBomb()) {
                openCell(cells.get(position - this.size));
            }
            if (position % size != size - 1 && position < size * (size - 1) && !cells.get(position + this.size + 1).isBomb()) {
                openCell(cells.get(position + this.size + 1));
            }
            if (position % size != 0 && position < size * (size - 1) && !cells.get(position + this.size - 1).isBomb()) {
                openCell(cells.get(position + this.size - 1));
            }
            if (position % size != size - 1 && position >= size && !cells.get(position - this.size + 1).isBomb()) {
                openCell(cells.get(position - this.size + 1));
            }
            if (position % size != 0 && position >= size && !cells.get(position - this.size - 1).isBomb()) {
                openCell(cells.get(position - this.size - 1));
            }
        }
    }

    private void openAllCells() {
        for (Cell cell:
             cells) {
            if (!cell.isDiscovered())
                cell.discover();
        }
    }
}
