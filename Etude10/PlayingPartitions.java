import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class PlayingPartitions {

    private static List<String> finalOutput = new ArrayList<>(); // The final output

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        List<List<FerrersBoard>> scenarios = new ArrayList<>();
        List<FerrersBoard> scenario = new ArrayList<>();

        // Read in all individual scenarios
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("---")) {
                scenarios.add(scenario);
                scenario = new ArrayList<>();
            }
            if (isValidPartition(line)) {
                scenario.add(new FerrersBoard(formatPartition(line)));
            }
        }
        if (!scenario.isEmpty()) {
            scenarios.add(scenario);
        }

        // Process each scenario not comments should be left in the scenario
        // a comment is a line that starts with # it could be any line if the sceario so
        // must be checked first
        for (List<FerrersBoard> boards : scenarios) {

            FerrersBoard start = boards.get(0);
            List<FerrersBoard> targets = boards.subList(1, boards.size());
            String outcome = evaluateGameOutcome(start, targets);

            finalOutput.add(start + "\n");
            for (FerrersBoard target : targets) {
                finalOutput.add(target.toString());
            }
            finalOutput.add(outcome);
            if (scenarios.get(scenarios.size() - 1) != boards) {
                finalOutput.add("--------");
            }
        }

        // Write the contents of finalOutput to text file given in args in terminal
        if (args.length > 0) {
            try {
                java.io.FileWriter writer = new java.io.FileWriter(args[0]);
                for (String line : finalOutput) {
                    writer.write(line + "\n");
                }
                writer.close();
            } catch (java.io.IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } else {
            for (String line : finalOutput) {
                System.out.println(line);
            }
        }

    }

    /**
     * Determines the game's outcome using a modified breadth-first search.
     */
    private static String evaluateGameOutcome(FerrersBoard initial, List<FerrersBoard> targetBoards) {
        Set<FerrersBoard> winningPositions = new HashSet<>();
        Set<FerrersBoard> losingPositions = new HashSet<>(targetBoards);
        Queue<FerrersBoard> positionsToCheck = new LinkedList<>(targetBoards);

        while (!positionsToCheck.isEmpty()) {
            FerrersBoard currentBoard = positionsToCheck.remove();

            if (losingPositions.contains(currentBoard)) {
                // Analyze potential moves leading to this losing position.
                for (int row = 0; row < currentBoard.getBoard().size(); row++) {
                    FerrersBoard previousBoard = currentBoard.copy();
                    previousBoard.makeReverseMove(row);
                    if (!winningPositions.contains(previousBoard) && !losingPositions.contains(previousBoard)) {
                        winningPositions.add(previousBoard);
                        positionsToCheck.add(previousBoard);
                    }
                }
            } else if (winningPositions.contains(currentBoard)) {
                // Analyze potential moves leading to this winning position.
                for (int row = 0; row <= currentBoard.getBoard().size() - 1; row++) {
                    FerrersBoard previousBoard = currentBoard.copy();
                    previousBoard.makeReverseMove(row);
                    if (!winningPositions.contains(previousBoard) && !losingPositions.contains(previousBoard)) {
                        boolean leadsToWin = true;
                        for (int column = 0; column < previousBoard.getBoard().get(0); column++) {
                            FerrersBoard nextBoard = previousBoard.copy();
                            nextBoard.makeMove(column);
                            if (!winningPositions.contains(nextBoard)) {
                                leadsToWin = false;
                                break;
                            }
                        }
                        if (leadsToWin) {
                            losingPositions.add(previousBoard);
                            positionsToCheck.add(previousBoard);
                        }
                    }
                }
            }
        }

        if (winningPositions.contains(initial)) {
            return "# WIN";
        } else if (losingPositions.contains(initial)) {
            return "# LOSE";
        } else {
            return "# DRAW";
        }
    }

    /**
     * A method to check if the given string line is a valid partition
     */
    public static boolean isValidPartition(String line) {
        if (line.trim().endsWith(","))
            return false;
        String[] parts = line.split(line.contains(",") ? "," : "\\s+");
        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.isBlank()) {
                return false;
            }
            try {
                int number = Integer.parseInt(trimmed);
                if (number < 1 || part.startsWith("0")) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * A method to format the given string line into a partition
     */
    private static ArrayList<Integer> formatPartition(String line) {
        String[] parts = line.split(line.contains(",") ? "," : "\\s+");
        ArrayList<Integer> numbers = new ArrayList<>();
        for (String part : parts) {
            part = part.trim();
            numbers.add(Integer.parseInt(part));
        }
        Collections.sort(numbers, Collections.reverseOrder());
        return numbers;
    }
}
