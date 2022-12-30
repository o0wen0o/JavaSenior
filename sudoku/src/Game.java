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

                        sudoku.setNum(token[1], "0");
                        break;

                    case "hint":
                        String[][] hints = sudoku.getHint(false);

                        if (token.length == 1) { // list all hint
                            System.out.println("Possible number:");
                            for (int x = 1; x <= 9; x++) {
                                for (int y = 1; y <= 9; y++) {
                                    String hint = hints[x - 1][y - 1];

                                    if (hint == "") { // got number then skip
                                        continue;
                                    }

                                    System.out.println("" + x + y + "[" + hint + "]");
                                }
                            }

                        } else if (token.length == 2 && token[1].length() == 1) { // specified z axis
                            System.out.println("Possible number:");
                            for (int x = 1; x <= 9; x++) {
                                for (int y = 1; y <= 9; y++) {
                                    String hint = hints[x - 1][y - 1];
                                    int z = sudoku.getSudoku().get(x - 1).get(y - 1).getZ(); // traverse all z

                                    if (!token[1].equals(String.valueOf(z))) { // different z axis then skip
                                        continue;
                                    }

                                    if (hint == "") { // got number then skip
                                        continue;
                                    }

                                    System.out.println("" + x + y + "[" + hint + "]");
                                }
                            }

                        } else {
                            System.out.println("Invalid input! Please Try Again: ");
                        }
                        break;

                    case "autofill":
                        sudoku.getHint(true);
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
