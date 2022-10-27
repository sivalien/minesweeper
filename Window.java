import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Window extends JFrame implements ActionListener {
    private static final JRadioButton a = new JRadioButton("Default grid size");
    private static final JRadioButton b = new JRadioButton("Custom grid size");
    private static final JRadioButton c = new JRadioButton("Computer solves the game");
    private static final JLabel label = new JLabel("Select game mode");
    private static final JLabel sizeLabel = new JLabel("Select grid size");
    private static final JLabel bombLabel = new JLabel("Select bombs number");
    private final ButtonGroup group = new ButtonGroup();
    private static int gridSize = 5;
    private static int bombNumber = 3;
    public static String mode;
    public static JFrame frame = new JFrame();
    private static final JButton button = new JButton("Start game");
    private static final JComboBox sizeBox = new JComboBox(new Integer[]{5, 6, 7, 8, 9, 10});
    private static final JComboBox bombBox = new JComboBox(new Integer[]{3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
    private static Grid panel;

    {
        group.add(a);
        group.add(b);
        group.add(c);
        label.setBounds(75, 20, 200, 20);
        a.setBounds(75, 50, 200, 30);
        b.setBounds(75, 100, 200, 30);
        c.setBounds(75, 150, 200, 30);
        sizeLabel.setBounds(75, 20, 100, 30);
        sizeBox.setBounds(75, 50, 100, 30);
        bombLabel.setBounds(75, 100, 150, 30);
        bombBox.setBounds(75, 130, 100, 30);
        button.setBounds(75, 180, 100, 30);
        a.addActionListener(this);
        b.addActionListener(this);
        c.addActionListener(this);
        sizeBox.addActionListener(this);
        bombBox.addActionListener(this);
        button.addActionListener(this);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Minesweeper");
        frame.setSize(420, 420);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setVisible(true);
        setWelcomeView();
    }

    Window() {}

    private static void setWelcomeView() {
        frame.add(label);
        frame.add(a);
        frame.add(b);
        frame.add(c);
        if (mode != null) {
            frame.revalidate();
            frame.repaint();
        }
    }

    private static void removePanel() {
        frame.getContentPane().removeAll();
        frame.setLayout(null);
        frame.setTitle("Minesweeper");
    }

    private static void removeWelcomeView() {
        frame.remove(label);
        frame.remove(a);
        frame.remove(b);
        frame.remove(c);
    }

    private static void setSecondView() {
        frame.add(sizeLabel);
        frame.add(sizeBox);
        frame.add(bombLabel);
        frame.add(bombBox);
        frame.add(button);
        frame.revalidate();
        frame.repaint();
    }

    private static void createBoard() {
        System.out.println("start");
        panel = new Grid(new GridLayout(gridSize, gridSize), gridSize, bombNumber);
        frame.setContentPane(panel);
        frame.setTitle("Grid Size: " + gridSize + " | Bombs Number: " + bombNumber);
        frame.revalidate();
        frame.repaint();
        System.out.println("created");
        if ("c".equals(mode)) {
            Solver solver = new Solver(panel, gridSize, bombNumber);
            solver.solve();
        }
    }

    static void sendMessage(String result) {
        // 0 = да, 1 = нет
        int input = JOptionPane.showConfirmDialog(frame,"Do you want to change the game mode?", result, JOptionPane.YES_NO_OPTION);
        System.out.println(input);
        removePanel();
        if (input == 0) {
            setWelcomeView();
        } else if (mode.equals("a")) {
            createBoard();
        } else {
            setSecondView();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==a) {
            gridSize = 5;
            bombNumber = 4;
            createBoard();
            mode = "a";
        }
        else if (e.getSource()==b) {
            removeWelcomeView();
            setSecondView();
            mode = "b";
        }
        else if (e.getSource()==c) {
            removeWelcomeView();
            setSecondView();
            mode = "c";
        }
        if (e.getSource()==sizeBox) {
            gridSize = (int) sizeBox.getSelectedItem();
        }
        if (e.getSource()==bombBox) {
            bombNumber = (int) bombBox.getSelectedItem();
        }
        if (e.getSource()==button) {
            createBoard();
        }
    }
}
