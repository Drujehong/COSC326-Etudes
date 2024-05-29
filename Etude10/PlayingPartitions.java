import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class PlayingPartitions {

    private static List<String> gameResults = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<List<FerrersBoard>> gameScenarios = new ArrayList<>();
        List<FerrersBoard> currentScenario = new ArrayList<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.matches("-{3,}")) { // Scenario separator
                gameScenarios.add(currentScenario);
                currentScenario = new ArrayList<>();
            } else if (isValidPartition(line)) { // Valid partition line
                currentScenario.add(new FerrersBoard(parsePartition(line)));
            }
            // Ignore invalid lines (comments, empty lines, etc.)
        }
        if (!currentScenario.isEmpty()) { // Add the last scenario if not empty
            gameScenarios.add(currentScenario);
        }

        for (List<FerrersBoard> scenario : gameScenarios) {
            FerrersBoard startingBoard = scenario.get(0);
            List<FerrersBoard> targetBoards = scenario.subList(1, scenario.size());

            gameResults.add(startingBoard + "\n");
            targetBoards.forEach(target -> gameResults.add(target.toString()));
            gameResults.add(evaluateGameOutcome(startingBoard, targetBoards));

            if (scenario != gameScenarios.get(gameScenarios.size() - 1)) {
                gameResults.add("--------");
            }
        }

        writeOutput(args);
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
     * A method to write the output to the console or a file
     * 
     * @param args
     */
    private static void writeOutput(String[] args) {
        try {
            FileWriter writer = args.length > 0 ? new FileWriter(args[0]) : null;
            for (String line : gameResults) {
                if (writer != null) {
                    writer.write(line + "\n");
                } else {
                    System.out.println(line);
                }
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error writing output: " + e.getMessage());
        }
    }

    /**
     * A method to format the given string line into a partition
     */
    private static List<Integer> parsePartition(String line) {
        return Arrays.stream(line.split("[\\s,]+")) // Split and parse to integers
                .map(Integer::parseInt)
                .sorted(Comparator.reverseOrder()) // Sort in descending order
                .toList();
    }
}
