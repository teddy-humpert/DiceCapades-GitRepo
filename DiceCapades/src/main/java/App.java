import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.util.Date;
import java.util.Scanner;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


// next steps include getting log output to work, so that there can eventually be data analysis.


public class App {

    private final Scanner keyboard = new Scanner(System.in);

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    private LocalDateTime now = LocalDateTime.now();
    private String nowFormat = dtf.format(now);

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        while (true) {
            printTitleCard();
            printMainMenu();
            int mainMenuSelection = promptForMenuSelection("Please choose an option from above: ");
            if (mainMenuSelection == 1) {
                int numberOfDice = promptForInt("How many dice would you like to roll? ");
                int sidesOfDice = promptForInt("How many sides on the dice? ");
                String sortPrompt = promptForSort("Sort [A]scending / [D]escending / [N]either? ");
                String sumPrompt = promptForSum("Return Sum [Y/N]? ");
                List<Integer> rollingArray = new ArrayList<>();
                List<Integer> diceArray;
                List<Integer> rollResults = new ArrayList<>();

                if (sortPrompt.equalsIgnoreCase("a") || sortPrompt.equalsIgnoreCase("d")
                        || sortPrompt.equalsIgnoreCase("n")) {
                    if (sumPrompt.equalsIgnoreCase("y") || sumPrompt.equalsIgnoreCase("n")) {
                        for (int i = 0; i < sidesOfDice; i++) {
                            rollingArray.add(i + 1);
                        }

                        diceArray = crazyShuffle(rollingArray);

                        for (int i = 0; i < numberOfDice; i++) {
                            Collections.shuffle(diceArray);
                            rollResults.add(diceArray.get(0));
                        }

                        if (sortPrompt.equalsIgnoreCase("A")) {
                            Collections.sort(rollResults, Collections.reverseOrder());
                            System.out.println(rollResults);
                            logDiceResults(rollResults);
                        }
                        if (sortPrompt.equalsIgnoreCase("D")) {
                            Collections.sort(rollResults);
                            System.out.println(rollResults);
                            logDiceResults(rollResults);
                        }
                        if (sortPrompt.equalsIgnoreCase("n")) {
                            System.out.println(rollResults);
                            logDiceResults(rollResults);
                        }
                        if (sumPrompt.equalsIgnoreCase("y")) {
                            int sum = 0;
                            for (int number : rollResults) {
                                sum += number;
                            }
                            System.out.println(sum);
                        }
                        int critCount = critCountCheck(rollResults, sidesOfDice);
                        System.out.println(printCritCountResults(critCount, sidesOfDice));
                        System.out.println();
                    } else {
                        displayInputError();
                    }
                } else {
                    displayInputError();
                }
            } else if (mainMenuSelection == 2) {
                int numberOfDice = promptForInt("How many dice would you like to roll? ");
                int sidesOfDice = promptForInt("How many sides on the dice? ");
                int numberToKeep = promptForInt("How many would you like to keep? ");
                String sumPrompt = promptForSum("Return Sum [Y/N]? ");
                List<Integer> diceArray;
                List<Integer> rollResults = new ArrayList<>();
                List<Integer> keptDice = new ArrayList<>();
                List<Integer> rollingArray = new ArrayList<>();

                for (int i = 0; i < sidesOfDice; i++) {
                    rollingArray.add(i + 1);
                }
                diceArray = crazyShuffle(rollingArray);

                for (int i = 0; i < numberOfDice; i++) {
                    Collections.shuffle(diceArray);
                    rollResults.add(diceArray.get(0));
                }

                Collections.sort(rollResults, Collections.reverseOrder());

                for (int i = 0; i < numberToKeep; i++) {
                    keptDice.add(rollResults.get(i));
                }

                if (sumPrompt.equalsIgnoreCase("y")) {
                    int sum = 0;
                    for (int number : keptDice) {
                        sum += number;
                    }
                    System.out.println(keptDice);
                    System.out.println(sum);
                }
                if (sumPrompt.equalsIgnoreCase("n")) {
                    System.out.println(keptDice);
                }
                int critCount = critCountCheck(keptDice, sidesOfDice);
                System.out.println(printCritCountResults(critCount, sidesOfDice));
                System.out.println();

            } else if (mainMenuSelection == 3) {
                int numberOfDice = promptForInt("How many dice would you like to roll? ");
                int sidesOfDice = promptForInt("How many sides on the dice? ");
                int numberToKeep = promptForInt("How many would you like to keep? ");
                String sumPrompt = promptForSum("Return Sum [Y/N]? ");

                List<Integer> diceArray = new ArrayList<>();
                List<Integer> rollResults = new ArrayList<>();
                List<Integer> keptDice = new ArrayList<>();
                List<Integer> rollingArray = new ArrayList<>();

                for (int i = 0; i < sidesOfDice; i++) {
                    rollingArray.add(i + 1);
                }

                diceArray = crazyShuffle(rollingArray);

                for (int i = 0; i < numberOfDice; i++) {
                    Collections.shuffle(diceArray);
                    rollResults.add(diceArray.get(0));
                }

                Collections.sort(rollResults);

                for (int i = 0; i < numberToKeep; i++) {
                    keptDice.add(rollResults.get(i));
                }

                if (sumPrompt.equalsIgnoreCase("y")) {
                    int sum = 0;
                    for (int number : keptDice) {
                        sum += number;
                    }
                    System.out.println(keptDice);
                    System.out.println(sum);
                }

                if (sumPrompt.equalsIgnoreCase("n")) {
                    System.out.println(keptDice);
                }

                int critCount = critCountCheck(keptDice, sidesOfDice);
                System.out.println(printCritCountResults(critCount, sidesOfDice));
                System.out.println();

            } else if (mainMenuSelection == 4) {
                readLogResults();
            }
            else if (mainMenuSelection == 0) {
                break;
            }
        }
    }

    // UI methods

    private void printTitleCard() {
        System.out.println("----------------------");
        System.out.println("Welcome to Dice Capades");
        System.out.println("----------------------");
    }

    private void printMainMenu() {
        System.out.println("1: Roll Dice");
        System.out.println("2: Roll Dice - Keep Highest");
        System.out.println("3: Roll Dice - Keep Lowest");
        System.out.println("4: Run Results");
        System.out.println("0: Exit");
        System.out.println();
    }

    private int promptForMenuSelection(String prompt) {
        System.out.print(prompt);
        int menuSelection;
        try {
            menuSelection = Integer.parseInt(keyboard.nextLine());
        } catch (NumberFormatException e) {
            displayInputError();
            menuSelection = -1;
        }
        return menuSelection;
    }

    private int promptForInt(String prompt) {
        System.out.print(prompt);
        int intInput;
        try {
            intInput = Integer.parseInt(keyboard.nextLine());
        } catch (NumberFormatException e) {
            intInput = -1;
            displayInputError();
            run();
        }
        return intInput;
    }

    private String promptForSort(String prompt) {
        System.out.print(prompt);
        String decision = keyboard.nextLine();
        if (decision.equalsIgnoreCase("a") || decision.equalsIgnoreCase("d") ||
                decision.equalsIgnoreCase("n")) {
            return decision;
        } else {
            displayInputError();
            run();
        }
        return null;

    }

    private String promptForSum(String prompt) {
        System.out.print(prompt);
        String decision = keyboard.nextLine();
        if (decision.equalsIgnoreCase("y") ||
                decision.equalsIgnoreCase("n")) {
            return decision;
        } else {
            displayInputError();
            run();
        }
        return null;
    }


    private List<Integer> crazyShuffle(List<Integer> diceArray) {
        for (int i = 0; i < 50; i ++) {
            Collections.shuffle(diceArray);
        }
        return diceArray;
    }

    private int critCountCheck(List<Integer> rollResults, int sidesOfDice) {
        int critCount = 0;
        for (Integer rollResult : rollResults) {
            if (rollResult == sidesOfDice) {
                critCount++;
            }
        }
        return critCount;
    }

    private String printCritCountResults(int critCount, int sidesOfDice) {
        String critMessage = "";
        if (critCount == 0) {
            critMessage = "You didn't roll any crits.";
        }
        if (critCount == 1) {
            critMessage = "You rolled a |" + sidesOfDice + "|!!!";
        }
        if (critCount > 1) {
            critMessage = "You rolled " + critCount + " |" + sidesOfDice + "|s!!!";
        }
        return critMessage;
    }

    private void displayInputError() {
        System.out.println("***Invalid Selection***");
    }

    private void logDiceResults(List results) {
        String auditPath = "DiceCapades/src/main/resources/logTest.txt";
        File logFile = new File(auditPath);
        String str = results.toString();
        String auditString = str.substring(1, str.length() - 1);
        try (
                PrintWriter log = new PrintWriter(new FileOutputStream(logFile, true))) {
            log.println(auditString);
        } catch (
                FileNotFoundException fnfe) {
            System.out.println("*** Unable to open log file: " + logFile.getAbsolutePath());
        }
    }

    public void readLogResults() {
        Map<String, Integer> output = new HashMap<>();
        String filePath = "DiceCapades/src/main/resources/logTest.txt";
        File bookFile = new File(filePath);
        boolean isFileFound = false;
        String runString = "";
        try (Scanner fileInput = new Scanner(bookFile)) {
            System.out.println(bookFile.getAbsolutePath());
            isFileFound = true;
            while (fileInput.hasNextLine()) {
                runString += fileInput.nextLine();
            }
        } catch (FileNotFoundException fnfe) { // and this worked for notfound
            System.out.println("The file was not found: " + bookFile.getAbsolutePath());

        }
        if (!runString.equalsIgnoreCase(" ")) {
            String[] resultsList = runString.split(" ");
            for (String number : resultsList) {
                if (!output.containsKey(number)) {
                    output.put(number, 1);
                } else {
                    output.put(number, output.get(number) + 1);
                }
            }
        }
        System.out.println(output);
    }
}
