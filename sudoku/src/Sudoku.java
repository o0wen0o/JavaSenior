import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author o0wen0o
 * @create 2022-12-28 11:51 AM
 */
public class Sudoku {
    private ArrayList<ArrayList<Point>> sudoku = new ArrayList<>(Collections.nCopies(9, null)); // x y

    public Sudoku() {
        int z = 4;
        for (int x = 1; x <= 9; x++) {
            z -= 3;

            sudoku.set(x - 1, new ArrayList<Point>(Collections.nCopies(9, null)));
            ArrayList<Point> points = sudoku.get(x - 1);

            for (int y = 1; y <= 9; y++) {
                points.set(y - 1, new Point(x, y, z, "0"));

                if (y % 3 == 0) {
                    z++;
                }
            }

            if (x % 3 == 0) {
                z += 3;
            }
        }
    }

    public ArrayList<ArrayList<Point>> getSudoku() {
        return sudoku;
    }

    public void setSudoku() {
        Utility.readFile(sudoku);
    }

    public void setNum(String position, String num) {
        int x = Integer.parseInt(position.substring(0, 1)); // get first number
        int y = Integer.parseInt(position.substring(1)); // get second number

        if (!isValid(x, y, num)) {
            System.out.println("Can not insert this number!");
            return;
        }

        sudoku.get(x - 1).get(y - 1).setNum(num);
    }

    public String[][] getHint(boolean autofill) {
        // to collect all possible number in all box
        // first dimension is x
        // second dimension is y/num
        String[][] hints = new String[9][9];

        for (String[] yAxis : hints) {
            Arrays.fill(yAxis, ""); // initialize all point to empty
        }

        // fill 1-9 to all box
        for (int numToFill = 1; numToFill <= 9; numToFill++) { // run 9 times
            for (int x = 1; x <= 9; x++) { // traverser arraylist
                for (int y = 1; y <= 9; y++) {
                    Point point = sudoku.get(x - 1).get(y - 1);
                    if (!point.getNum().equals("0")) { // the position has number
                        continue;
                    }

                    if (!isValid(x, y, String.valueOf(numToFill))) {
                        continue;
                    }

                    hints[x - 1][y - 1] += numToFill;
                }
            }
        }

        if (autofill)
            autofill(hints);

        return hints;
    }

    @Override
    public String toString() {
        String result = "";

        for (int x = 1; x <= 9; x++) {
            ArrayList<Point> points = sudoku.get(x - 1);

            for (int y = 1; y <= 9; y++) {
                Point point = points.get(y - 1);
                String num = point.getNum();

                if (num.equals("0")) {
                    result += "[ ]";
                } else {
                    result += "[" + point.getColor() + num + "\u001B[0m" + "]"; // num
                }

                if (y % 3 == 0) {
                    result += "\t";
                }
            }

            if (x % 3 == 0) {
                result += "\n\n";
            } else {
                result += "\n";
            }
        }

        return result;
    }

    private boolean isValid(int x, int y, String num) {
        // delete number
        if (num.equals("0"))
            return true;

        // x axis
        for (Point point : sudoku.get(x - 1)) {
            if (point.getNum().equals(num))
                return false;
        }

        // y axis
        for (int i = 0; i < 9; i++) {
            Point point = sudoku.get(i).get(y - 1);
            if (point.getNum().equals(num))
                return false;
        }

        // z axis
        int z = sudoku.get(x - 1).get(y - 1).getZ();
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

    private void autofill(String[][] hints) {
        boolean hasHint = false;

        // to find all the possible number in different z-axis
        Point[] zAxisHints = new Point[9];

        for (int x = 1; x <= 9; x++) { // traverse hint list
            for (int y = 1; y <= 9; y++) {
                String hint = hints[x - 1][y - 1];

                if (hint.length() == 1) { // if only one suggested number
                    sudoku.get(x - 1).get(y - 1).setNum(hint);

                    System.out.println("\u001B[33mAutofilled: " + "add " + x + y + " " + hint + "\u001B[0m");
                    hasHint = true;
                }
            }
        }

        // to find the possible number that only occur once in different z-axis
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

                    zAxisHints[zIndex++] = new Point(x, y, z, hints[x - 1][y - 1]);
                }
            }

            // 2.traverse 1-9, find if occur only once
            // then print out and set number to sudoku
            for (int i = 1; i <= 9; i++) {

                int count = 0; // to count the possible number in z-axis
                Point occurOnce = null;
                // int markNum = 0;

                for (int j = 1; j <= 9; j++) {
                    Point point = zAxisHints[j - 1];
                    String hint = point.getNum();

                    if (hint.contains(String.valueOf(i))) { // if only one suggested number
                        count++;
                        occurOnce = point;
                        // markNum = i;
                    }

                    if (count > 1) // occur 2 times then skip to next number
                        break;
                }

                if (count == 1) { // only occur 1 times
                    int x = occurOnce.getX();
                    int y = occurOnce.getY();
                    sudoku.get(x - 1).get(y - 1).setNum(String.valueOf(i));

                    System.out.println("\u001B[33mAutofilled: " + "add " + x + y + " " + i + "\u001B[0m");
                    hasHint = true;
                }
            }
        }

        if (!hasHint) {
            System.out.println("Couldn't find any hints!");
        }
    }
}
