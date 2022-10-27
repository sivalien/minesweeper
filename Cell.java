import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Cell extends JButton {
    private int value;
    private boolean isBomb;
    private int position;
    private boolean discovered;
    private boolean flagged;

    {
        discovered = false;
        flagged = false;
        isBomb = false;
        value = 0;
    }

    public Cell(int position) {
        this.position = position;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void flag() {
        if (!this.discovered) {
            if (this.flagged) {
                this.flagged = false;
                this.setText("");
            } else {
                this.flagged = true;
                this.setText("F");
            }
        }
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void discover() {
        this.discovered = true;
        this.setEnabled(false);
        if (this.isBomb()) {
            this.setText("X");
        }
        else if (this.value > 0) {
            this.setText(String.valueOf(this.value));
        }
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void putBomb() {
        isBomb = true;
    }

    public int getValue() {
        return value;
    }

    public void increaseValue() {
        this.value++;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
