import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author o0wen0o
 * @create 2024-02-01 8:11 PM
 */
public class TestForSudoku {

    Sudoku sudoku = new Sudoku();

    @Test
    public void getStrongRelationshipForX() {
        sudoku.setSudoku("src\\data\\test_data.txt");
        ArrayList<ArrayList<Point>> hints = sudoku.getHint();

        ArrayList<StrongRelationshipPoints> strongPointList = sudoku.getStrongRelationship(hints, true);

        // Grouping by numRelated
        Map<String, List<StrongRelationshipPoints>> groupedByNumRelated = strongPointList.stream()
                .collect(Collectors.groupingBy(StrongRelationshipPoints::getNumRelated));

        // Displaying the result
        groupedByNumRelated.forEach((numRelated, group) -> {
            if (group.size() < 2) {
                return;
            }
            System.out.println("numRelated: " + numRelated);
            System.out.println("Group: " + group);
            System.out.println("-----------");
        });
    }

    @Test
    public void xWing() {
        sudoku.setSudoku("src\\data\\test_data.txt");
        ArrayList<ArrayList<Point>> hints = sudoku.getHint();
        System.out.println(hints.get(0).get(1).getNum());

        sudoku.xWing(hints);
        System.out.println(hints.get(0).get(1).getNum());
    }

    @Test
    public void autofill() {
        sudoku.setSudoku("src\\data\\test_data.txt");
        ArrayList<ArrayList<Point>> hints = sudoku.getHint();
        System.out.println(hints.get(0).get(1).getNum());

        sudoku.autofill(hints);
        System.out.println(hints.get(0).get(1).getNum());
    }
}
