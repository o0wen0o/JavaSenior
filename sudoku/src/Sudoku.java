import java.util.ArrayList;
import java.util.Collections;

/**
 * @author o0wen0o
 * @create 2022-12-28 11:51 AM
 */
public class Sudoku {
    private final ArrayList<ArrayList<Point>> sudoku = new ArrayList<>(Collections.nCopies(9, null)); // x y

    public Sudoku() {
        initSudoku(sudoku);
    }

    public ArrayList<ArrayList<Point>> getSudoku() {
        return sudoku;
    }

    public void setSudoku() {
        Utility.readFile(sudoku);
    }

    public void setNum(String position, String num) {
        int x = Integer.parseInt(position.substring(0, 1)) - 1; // get first number
        int y = Integer.parseInt(position.substring(1)) - 1; // get second number

        if (!isValid(x, y, num)) {
            System.out.println("Can not insert this number!");
            return;
        }

        sudoku.get(x).get(y).setNum(num);
    }

    public ArrayList<ArrayList<Point>> getHint() {
        // to collect all possible number in all box
        ArrayList<ArrayList<Point>> hints = new ArrayList<>(Collections.nCopies(9, null));
        initSudoku(hints);

        // fill 1-9 to all box
        for (int num = 1; num <= 9; num++) { // run 9 times
            for (int x = 0; x < 9; x++) { // traverser arraylist
                for (int y = 0; y < 9; y++) {
                    Point point = sudoku.get(x).get(y);
                    if (!point.getNum().isEmpty()) { // the position has number
                        continue;
                    }

                    String numToFill = String.valueOf(num);
                    if (!isValid(x, y, numToFill)) {
                        continue;
                    }

                    Point hint = hints.get(x).get(y);
                    hint.setNum(hint.getNum() + numToFill);
                }
            }
        }

        return hints;
    }

    private boolean isValid(int x, int y, String num) {
        // delete number
        if (num.isEmpty())
            return true;

        // x axis
        for (Point point : sudoku.get(x)) {
            if (point.getNum().equals(num))
                return false;
        }

        // y axis
        for (int i = 0; i < 9; i++) {
            Point point = sudoku.get(i).get(y);
            if (point.getNum().equals(num))
                return false;
        }

        // z axis
        int z = sudoku.get(x).get(y).getZ();
        for (ArrayList<Point> points : sudoku) {
            for (Point point : points) {
                if (point.getZ() == z) { // in same z
                    if (point.getNum().equals(num))
                        return false;
                }
            }
        }
        return true;
    }

    public boolean autofill(ArrayList<ArrayList<Point>> hints) {
        boolean hasFilled = false;

        // Naked Single 唯一候选数法
        // autofill if only one suggested number
        if (nakedSingle(hints)) {
            hasFilled = true;
        }

        // Hidden Single 隐藏候选数法
        // to find the possible number that only occur once in different z-axis
        if (hiddenSingle(hints)) {
            hasFilled = true;
        }

        // X-Wing


        // Swordfish


        return hasFilled;
    }

    private boolean nakedSingle(ArrayList<ArrayList<Point>> hints) {
        boolean hasFilled = false;

        for (int x = 0; x < 9; x++) { // traverse hint list
            for (int y = 0; y < 9; y++) {
                String hint = hints.get(x).get(y).getNum();

                if (hint.length() == 1) { // if only one suggested number
                    sudoku.get(x).get(y).setNum(hint);

                    System.out.println("\u001B[33mAutofilled: " + "add " + x + y + " " + hint + "\u001B[0m");
                    hasFilled = true;
                }
            }
        }
        return hasFilled;
    }

    private boolean hiddenSingle(ArrayList<ArrayList<Point>> hints) {
        boolean hasFilled = false;

        // to find all the possible number in different z-axis
        Point[] zAxisHints = new Point[9];

        for (int z = 1; z <= 9; z++) {
            int zIndex = 0;

            // 1.get the possible number in certain z-axis to zAxisHints[i]
            for (int x = 1; x <= 9; x++) { // traverse hint list
                for (int y = 1; y <= 9; y++) {
                    Point point = sudoku.get(x - 1).get(y - 1);
                    int z1 = point.getZ(); // traverse all z

                    if (z1 != z) { // find possible number by z
                        continue;
                    }

                    zAxisHints[zIndex++] = new Point(x, y, z, hints.get(x - 1).get(y - 1).getNum());
                }
            }

            // 2.traverse 1-9, find if occur only once
            // then print out and set number to sudoku
            for (int i = 1; i <= 9; i++) {

                int count = 0; // to count the possible number in z-axis
                Point occurOncePoint = null;
                // int markNum = 0;

                for (int j = 1; j <= 9; j++) {
                    Point point = zAxisHints[j - 1];
                    String hint = point.getNum();

                    if (hint.contains(String.valueOf(i))) { // if only one suggested number
                        count++;
                        occurOncePoint = point;
                        // markNum = i;
                    }

                    if (count > 1) // occur 2 times then skip to next number
                        break;
                }

                if (count == 1) { // only occur 1 times
                    int x = occurOncePoint.getX();
                    int y = occurOncePoint.getY();
                    sudoku.get(x - 1).get(y - 1).setNum(String.valueOf(i));

                    System.out.println("\u001B[33mAutofilled: " + "add " + x + y + " " + i + "\u001B[0m");
                    hasFilled = true;
                }
            }
        }
        return hasFilled;
    }

    private void initSudoku(ArrayList<ArrayList<Point>> sudoku) {
        int z = 4;
        for (int x = 0; x < 9; x++) {
            z -= 3;

            sudoku.set(x, new ArrayList<>(Collections.nCopies(9, null)));
            ArrayList<Point> points = sudoku.get(x);

            for (int y = 0; y < 9; y++) {
                points.set(y, new Point(x + 1, y + 1, z, ""));

                if (y % 3 == 2) {
                    z++;
                }
            }

            if (x % 3 == 2) {
                z += 3;
            }
        }
    }

    @Override
    public String toString() {
        String result = "";

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Point point = sudoku.get(x).get(y);
                String num = point.getNum();

                if (num.isEmpty()) {
                    result += "[ ]";
                } else {
                    result += "[" + point.getColor() + num + "\u001B[0m" + "]"; // num
                }

                if (y % 3 == 2) {
                    result += "\t";
                }
            }

            if (x % 3 == 2) {
                result += "\n\n";
            } else {
                result += "\n";
            }
        }

        return result;
    }
}

