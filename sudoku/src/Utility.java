import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author o0wen0o
 * @create 2022-12-28 11:51 AM
 */
public class Utility {

    private static Scanner scanner = new Scanner(System.in);

    public static void readFile(ArrayList<ArrayList<Point>> sudoku, String filePath) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));

            String line;
            for (int i = 0; i < 9 && ((line = br.readLine()) != null); i++) {
                for (int j = 0; j < 9; j++) {
                    Point point = sudoku.get(i).get(j);
                    String num = line.substring(j, j + 1);

                    if (num.equals("0")) {
                        num = "";
                    }

                    point.setNum(num);

                    if (!point.getNum().isEmpty()) { // give a blue color
                        point.setColor("\u001B[34m");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // read a string which not greater than the limit
    public static String readString(int limit) {
        return readKeyBoard(limit, false);
    }

    // read a character which not greater than the limit, return current value if
    // user does not intend to change to new value
    public static String readString(int limit, String defaultValue) {
        String str = readKeyBoard(limit, true);
        return str.equals("") ? defaultValue : str;
    }

    // read 'Y' or 'N' only
    public static char readConfirmSelection() {
        char c;
        for (; ; ) {
            String str = readKeyBoard(1, false).toUpperCase();
            c = str.charAt(0);
            if (c == 'Y' || c == 'N') {
                break;
            } else {
                System.out.print("Invalid Input! Please Try Again: ");
            }
        }
        return c;
    }

    // The program will continue to run after the user press enter
    public static void readReturn() {
        System.out.print("Press Enter To Continue...... ");
        readKeyBoard(100, true);
    }

    private static String readKeyBoard(int limit, boolean blankReturn) {
        String line = "";

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.length() == 0) {
                if (blankReturn)
                    return line;
                else {
                    System.out.print("Invalid Input! Please Try Again: ");
                    continue;
                }
            }

            if (line.length() < 1 || line.length() > limit) {
                System.out.print("Input exceed " + limit + ". Please Try Again: ");
                continue;
            }
            break;
        }
        return line;
    }
}
