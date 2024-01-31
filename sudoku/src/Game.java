import java.util.ArrayList;

/**
 * @author o0wen0o
 * @create 2022-12-28 12:24 PM
 */
public class Game {

    public void enterMainMenu() {
        Sudoku sudoku = new Sudoku();
        sudoku.setSudoku();

        boolean loopFlag = true;

        while (loopFlag) {
            System.out.print(sudoku);
            System.out.println("Give instruction:");
            System.out.println("1.add xy num");
            System.out.println("2.del xy");
            System.out.println("3.hint [1-9]");
            System.out.println("4.autofill");
            System.out.println("5.exit");

            String line = Utility.readString(100);
            String[] token = line.split(" ");
            System.out.println();

            try {
                switch (token[0]) {
                    case "add":
                        if (token.length != 3 || token[1].length() != 2 || token[2].length() != 1) {
                            System.out.println("Invalid input! Please Try Again: ");
                            break;
                        }

                        sudoku.setNum(token[1], token[2]);
                        break;

                    case "del":
                        if (token.length != 2 || token[1].length() != 2) {
                            System.out.println("Invalid input! Please Try Again: ");
                            break;
                        }

                        sudoku.setNum(token[1], "");
                        break;

                    case "hint":
                        ArrayList<ArrayList<Point>> hints = sudoku.getHint();

                        if (token.length == 1) { // list all hint
                            System.out.println("Possible number:");
                            for (int x = 0; x < 9; x++) {
                                for (int y = 0; y < 9; y++) {
                                    String hint = hints.get(x).get(y).getNum();

                                    if (hint.isEmpty()) { // got number then skip
                                        continue;
                                    }

                                    System.out.println("" + (x + 1) + (y + 1) + "[" + hint + "]");
                                }
                            }

                        } else if (token.length == 2 && token[1].length() == 1) { // specified z axis
                            System.out.println("Possible number:");

                            for (int x = 0; x < 9; x++) {
                                for (int y = 0; y < 9; y++) {
                                    String hint = hints.get(x).get(y).getNum();
                                    int z = sudoku.getSudoku().get(x).get(y).getZ(); // traverse all z

                                    if (!token[1].equals(String.valueOf(z))) { // different z axis then skip
                                        continue;
                                    }

                                    if (hint.isEmpty()) { // got number then skip
                                        continue;
                                    }

                                    System.out.println("" + (x + 1) + (y + 1) + "[" + hint + "]");
                                }
                            }

                        } else {
                            System.out.println("Invalid input! Please Try Again: ");
                        }
                        break;

                    case "autofill":
                        boolean hasFilled = sudoku.autofill(sudoku.getHint());
                        if (!hasFilled) {
                            System.out.println("Can't fill in anymore!");
                        }
                        break;

                    case "exit":
                        System.out.print("Confirm exit?（Y/N）：");
                        char isExit = Utility.readConfirmSelection();
                        if (isExit == 'Y') {
                            loopFlag = false;
                        }
                        break;

                    default:
                        System.out.println("Invalid input! Please Try Again: ");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input! Please Try Again: ");
            }
            Utility.readReturn();
            System.out.println();
        }
    }


    public static void main(String[] args) {
        Game game = new Game();
        game.enterMainMenu();
    }
}
