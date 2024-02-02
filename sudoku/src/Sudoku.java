import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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

    public void setSudoku(String filePath) {
        Utility.readFile(sudoku, filePath);
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
        if (xWing(hints)) {
            hasFilled = true;
        }

        // Swordfish


        return hasFilled;
    }

    public boolean nakedSingle(ArrayList<ArrayList<Point>> hints) {
        boolean hasFilled = false;

        for (int x = 0; x < 9; x++) { // traverse hint list
            for (int y = 0; y < 9; y++) {
                String hint = hints.get(x).get(y).getNum();

                if (hint.length() == 1) { // if only one suggested number
                    sudoku.get(x).get(y).setNum(hint);
                    hints.get(x).get(y).setNum(""); // remove the hints for next iteration

                    System.out.println("\u001B[33mAutofilled: " + "add " + x + y + " " + hint + "\u001B[0m");
                    hasFilled = true;
                }
            }
        }
        return hasFilled;
    }

    public boolean hiddenSingle(ArrayList<ArrayList<Point>> hints) {
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

                for (int j = 1; j <= 9; j++) {
                    Point point = zAxisHints[j - 1];
                    String hint = point.getNum();

                    if (hint.contains(String.valueOf(i))) { // if only one suggested number
                        count++;
                        occurOncePoint = point;
                    }

                    if (count > 1) // occur 2 times then skip to next number
                        break;
                }

                if (count == 1) { // only occur 1 times
                    int x = occurOncePoint.getX();
                    int y = occurOncePoint.getY();
                    sudoku.get(x - 1).get(y - 1).setNum(String.valueOf(i));
                    hints.get(x - 1).get(y - 1).setNum("");

                    System.out.println("\u001B[33mAutofilled: " + "add " + x + y + " " + i + "\u001B[0m");
                    hasFilled = true;
                }
            }
        }
        return hasFilled;
    }

    public boolean xWing(ArrayList<ArrayList<Point>> hints) {
        boolean hasChanged = false;

        // For x-axis
        ArrayList<StrongRelationshipPoints> strongPointListForX = getStrongRelationship(hints, true);
        doXWingByAxis(strongPointListForX, hints, true);

        // For y-axis
        ArrayList<StrongRelationshipPoints> strongPointListForY = getStrongRelationship(hints, false);
        doXWingByAxis(strongPointListForY, hints, false);

        return hasChanged;
    }

    public ArrayList<StrongRelationshipPoints> getStrongRelationship(ArrayList<ArrayList<Point>> hints, boolean checkX) {
        ArrayList<StrongRelationshipPoints> strongPointList = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            for (int j = 0; j < 9; j++) {
                ArrayList<Point> temp = new ArrayList<>();

                for (int k = 0; k < 9; k++) {
                    Point point = checkX ? hints.get(k).get(j) : hints.get(j).get(k);
                    String num = point.getNum();

                    if (num.contains(String.valueOf(i))) {
                        temp.add(point);
                    }

                    if (temp.size() > 2) { // mean that row don't have strong relationship
                        break;
                    }
                }

                if (temp.size() == 2) {
                    strongPointList.add(new StrongRelationshipPoints(temp.get(0), temp.get(1), String.valueOf(i)));
                }
            }
        }

        return strongPointList;
    }

    public boolean doXWingByAxis(ArrayList<StrongRelationshipPoints> strongPointList, ArrayList<ArrayList<Point>> hints, boolean isXAxis) {
        AtomicBoolean hasChanged = new AtomicBoolean(false); // AtomicBoolean to avoid ConcurrentModificationException

        // Grouping by numRelated
        Map<String, List<StrongRelationshipPoints>> groupedByNumRelated = strongPointList.stream()
                .collect(Collectors.groupingBy(StrongRelationshipPoints::getNumRelated));

        // Eliminate redundant numRelated in hints
        groupedByNumRelated.forEach((numRelated, group) -> {
            if (group.size() < 2) {
                return;
            }

            for (int i = 0; i < group.size(); i++) {
                for (int j = i + 1; j < group.size(); j++) {
                    StrongRelationshipPoints strongPoints1 = group.get(i);
                    StrongRelationshipPoints strongPoints2 = group.get(j);

                    // Eliminate if the axes are same
                    if (isXAxis ? strongPoints1.getY().equals(strongPoints2.getY()) : strongPoints1.getX().equals(strongPoints2.getX())) {
                        HashSet<Integer> combineSet = new HashSet<>();
                        combineSet.addAll(isXAxis ? strongPoints1.getX() : strongPoints1.getY());
                        combineSet.addAll(isXAxis ? strongPoints2.getX() : strongPoints2.getY());

                        HashSet<Integer> targetSet = isXAxis ? strongPoints1.getY() : strongPoints1.getX();

                        // Eliminate numRelated in hints for the column in y/x
                        boolean hasEliminated = eliminateRedundantHintForXWing(hints, combineSet, targetSet, numRelated, isXAxis);
                        hasChanged.set(hasEliminated);
                    }
                }
            }
        });

        return hasChanged.get();
    }

    /**
     * eliminate redundant hint for xWing method
     *
     * @param hints
     * @param combineSet The rows/columns that should be skipped
     * @param targetSet  The rows/columns that the num should be replaced
     * @param numRelated
     * @param checkX     Determine if it is x axis
     * @return
     */
    public boolean eliminateRedundantHintForXWing(ArrayList<ArrayList<Point>> hints, HashSet<Integer> combineSet, HashSet<Integer> targetSet, String numRelated, boolean checkX) {
        boolean hasEliminated = false;

        for (ArrayList<Point> hint : hints) {
            for (Point point : hint) {

                // skip for strong points row
                if ((checkX && combineSet.contains(point.getX())) || (!checkX && combineSet.contains(point.getY()))) {
                    continue;
                }

                // replace redundant numRelated in hints to empty string
                if ((checkX && targetSet.contains(point.getY())) || (!checkX && targetSet.contains(point.getX())) && point.getNum().contains(numRelated)) {
                    String result = point.getNum().replace(numRelated, "");
                    point.setNum(result);
                    hasEliminated = true;
                }
            }
        }

        return hasEliminated;
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

