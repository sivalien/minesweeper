import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        FileWriter writer = new FileWriter("saved_games.txt", true);
        System.out.println("Please, select game mode (a, b or c)");
        writer.write("Please, select game mode (a, b or c)\n");
        String mode = scanner.next();
        Field field = new Field();
        writer.write(mode + "\n");
        if ("c".equals(mode)) {
            int fieldSize = scanner.nextInt();
            int bombNumber = scanner.nextInt();
            Solver solver = new Solver(writer, fieldSize, bombNumber);
            solver.solve();
        }
        else {
            if ("a".equals(mode)) {
                field = new Field(writer);
            }
            else if ("b".equals(mode)) {
                int fieldSize = scanner.nextInt();
                int bombNumber = scanner.nextInt();
                field = new Field(writer, fieldSize, bombNumber);
            }
            while (true) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                String action = scanner.next();
                writer.write(x + " " + y + " " + action + "\n");
                if (x < 0 || x >= field.getFieldSize() || y < 0 || y >= field.getFieldSize()) {
                    System.out.println("Incorrect cell");
                    writer.write("Incorrect cell\n");
                    continue;
                }
                if (!"Open".equals(action) && !"Flag".equals(action)) {
                    System.out.println("Incorrect action");
                    writer.write("Incorrect action\n");
                    continue;
                }
                if (!field.makeMove(x, y, action)) {
                    break;
                }
            }
        }
        writer.flush();
    }
}
