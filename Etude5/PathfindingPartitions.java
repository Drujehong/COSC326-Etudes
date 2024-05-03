import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Scanner;

public class PathfindingPartitions {

    /**
     * Method to create an arraylist from a Scanner Object
     * 
     * @param scanner
     * @return listOfLines
     * @throws IOException
     */
    private static ArrayList<String> readLinesToArrayList(Scanner scanner) {
        ArrayList<String> listOfLines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            listOfLines.add(scanner.nextLine());
        }
        return listOfLines;
    }

    private static ArrayList<String> proReader(ArrayList<String> inputArr) {
        // Create a new ArrayList to store scenarios
        ArrayList<ArrayList<String>> scenarios = new ArrayList<>();

        // Create a new Arraylist to store current scenario's contents
        ArrayList<String> currentScenario = new ArrayList<>();

        for (String line : inputArr) {
            currentScenario.add(line);
            if (line.startsWith("#")) {
                currentScenario.remove(line);
            }
            if (identifyScenario(line)) {
                // onlyHyphensAndEmptyLines
                if (onlyHyphensAndEmptyLines(currentScenario)) {
                    currentScenario.clear();
                } else if (isValidScenario(currentScenario)) {

                    String initialParStr = getDescendingOrder(currentScenario.get(0));
                    String targetParStr = getDescendingOrder(currentScenario.get(1));

                    // Convert string partitions into List<Integer>
                    List<Integer> initialPartition = convertPartitionToList(initialParStr);
                    List<Integer> targetPartition = convertPartitionToList(targetParStr);

                    ArrayList<String> finalResult = new ArrayList<>();

                    if (validParPair(initialPartition, targetPartition)) {
                        FerrersBoard initialBoard = new FerrersBoard(initialPartition);
                        FerrersBoard targetBoard = new FerrersBoard(targetPartition);
                        PathResult results = initialBoard.findShortestPath(initialBoard, targetBoard);

                        finalResult.add("# Moves required: " + results.moves);
                        for (FerrersBoard boardState : results.path) {
                            finalResult.add(ferrersBoardToString(boardState));
                        }
                    } else {
                        finalResult.add("# No solution possible");
                        finalResult.add(initialParStr);
                        finalResult.add(targetParStr);
                    }
                    scenarios.add(finalResult);
                    if (identifyScenario(currentScenario.get(2))) {
                        ArrayList<String> seperator = new ArrayList<>();
                        seperator.add("--------");
                        scenarios.add(seperator);
                    }
                    currentScenario.clear();
                } else {
                    // Handle invalid scenario
                    currentScenario.add(0, "# INVALID SCENARIO (More or less than 2 valid partitions)");
                    scenarios.add(new ArrayList<>(currentScenario));
                    currentScenario.clear();
                }
            }
        }

        // Adding the last scenario if the scenario isn't empty
        if (!currentScenario.isEmpty()) {
            // Add the last scenario
            if (onlyHyphensAndEmptyLines(currentScenario)) {
                currentScenario.clear();
            } else if (isValidScenario(currentScenario)) {
                String initialParStr = getDescendingOrder(currentScenario.get(0));
                String targetParStr = getDescendingOrder(currentScenario.get(1));

                // Convert string partitions into List<Integer>
                List<Integer> initialPartition = convertPartitionToList(initialParStr);
                List<Integer> targetPartition = convertPartitionToList(targetParStr);

                ArrayList<String> finalResult = new ArrayList<>();

                if (validParPair(initialPartition, targetPartition)) {
                    FerrersBoard initialBoard = new FerrersBoard(initialPartition);
                    FerrersBoard targetBoard = new FerrersBoard(targetPartition);
                    PathResult results = initialBoard.findShortestPath(initialBoard, targetBoard);

                    finalResult.add("# Moves required: " + results.moves);
                    for (FerrersBoard boardState : results.path) {
                        finalResult.add(ferrersBoardToString(boardState));
                    }
                } else {
                    finalResult.add("# No solution possible");
                    finalResult.add(initialParStr);
                    finalResult.add(targetParStr);
                }
                scenarios.add(finalResult);
            } else {
                // Handle invalid scenario
                currentScenario.add(0, "# INVALID SCENARIO (More or less than 2 valid partitions)");
                scenarios.add(new ArrayList<>(currentScenario));
                currentScenario.clear();
            }
        }

        ArrayList<String> flattenedScenarios = new ArrayList<>();

        for (ArrayList<String> innerList : scenarios) {
            flattenedScenarios.addAll(innerList);
        }

        // Check if there are elements in the flattened list
        if (!flattenedScenarios.isEmpty()) {
            // Get the last line of the flattened list
            String lastLine = flattenedScenarios.get(flattenedScenarios.size() - 1);

            // Check if it's a hyphen line
            if (identifyScenario(lastLine)) {
                flattenedScenarios.remove(flattenedScenarios.size() - 1);
            }

        } else {
            // The flattened scenario is empty
            System.out.println("Flattened Scenario is empty");
        }

        // Remove consecutive empty lines with a single empty line
        replaceConsecutiveEmptyLines(flattenedScenarios);

        return flattenedScenarios;
    }

    /**
     * Method to return input line into a descending order
     * 
     * @param line
     * @return
     */
    private static String getDescendingOrder(String line) {
        String[] elements = line.split("\\s*,\\s*|\\s+");

        int[] numbers = new int[elements.length];
        for (int i = 0; i < elements.length; i++) {
            numbers[i] = Integer.parseInt(elements[i]);
        }

        Arrays.sort(numbers);

        StringBuilder result = new StringBuilder();

        // Append the numbers in descending order to the result StringBuilder
        for (int i = numbers.length - 1; i >= 0; i--) {
            result.append(numbers[i]).append(" ");
        }

        return result.toString().trim(); // Trim to remove trailing space
    }

    /**
     * Method to replace consecutive emptylines with only 1 empty line in a scenario
     * 
     * @param scenario
     */
    private static void replaceConsecutiveEmptyLines(ArrayList<String> scenario) {
        ArrayList<String> modifiedList = new ArrayList<>();
        boolean previousLineEmpty = false;

        for (String line : scenario) {
            if (emptyLine(line)) {
                if (!previousLineEmpty) {
                    modifiedList.add(line);
                }
                previousLineEmpty = true;
            } else {
                modifiedList.add(line);
                previousLineEmpty = false;
            }
        }
        scenario.clear();
        scenario.addAll(modifiedList);
    }

    /**
     * Boolean method to identify scenario lines
     * 
     * @param line
     * @return
     */
    private static boolean identifyScenario(String line) {
        return line.matches("-{3,}");
    }

    /**
     * Boolean method that returns comma in the beginning
     * 
     * @param line
     * @return
     */
    private static boolean commaInBeginning(String line) {
        return line.startsWith(",");
    }

    /**
     * Boolean method that returns consecutives commas
     * 
     * @param line
     * @return
     */
    private static boolean hasConsectiveCommas(String line) {
        for (int i = 0; i < line.length() - 1; i++) {
            if (line.charAt(i) == ',' && line.charAt(i + 1) == ',') {
                return true;
            }
        }
        return false;
    }

    /**
     * Boolean method that returns comma at the end
     * 
     * @param line
     * @return
     */
    private static boolean commaInEnd(String line) {
        return line.endsWith(",");
    }

    /**
     * Boolean method that validates a partition line
     * 
     * @param line
     * @return
     */
    private static boolean validatePartitionLine(String line) {
        // Create a new line from remove all the whitespaces in the original line
        String lineWithoutWhitespaces = String.join("", line.split("\\s+"));
        if (commaInBeginning(lineWithoutWhitespaces) || commaInEnd(lineWithoutWhitespaces)
                || hasConsectiveCommas(lineWithoutWhitespaces) || negativeNum(lineWithoutWhitespaces)) {
            return false;
        }
        int numbers = countNumbers(line);
        int commas = countCommas(line);
        if (commas != numbers - 1 && commas != 0) {
            return false;
        }

        String[] elements = line.split("\\s*,\\s*|\\s+");
        int sum = 0;
        for (String element : elements) {
            // Find zeros in the array
            if (startsWithZero(element)) {
                return false;
            }
            try {
                int num = Integer.parseInt(element);
                sum += num;
            } catch (NumberFormatException e) {
                // Handle invalid integers
                return false;
            }
        }

        return sum > 0;
    }

    /**
     * Boolean method that returns negative number
     * 
     * @param line
     * @return
     */
    private static boolean negativeNum(String line) {
        return line.contains("-");
    }

    /**
     * Boolean method that return true if the scenario only has hyphens and empty
     * lines
     * 
     * @param scenario
     * @return
     */
    private static boolean onlyHyphensAndEmptyLines(ArrayList<String> scenario) {
        int count = 0;
        for (String line : scenario) {
            if (emptyLine(line) || identifyScenario(line)) {
                count++;
            }
        }
        return count == scenario.size();
    }

    /**
     * Boolean method to identify empty line
     * 
     * @param line
     * @return
     */
    private static boolean emptyLine(String line) {
        return line.isEmpty();
    }

    /**
     * Boolean method to check if given string starts with 0
     * 
     * @param element
     * @return
     */
    private static boolean startsWithZero(String element) {
        return element.startsWith("0");
    }

    // Method to count numbers from a string
    private static int countNumbers(String line) {
        String[] elements = line.split("\\s*,\\s*|\\s+");
        int count = 0;

        for (int i = 0; i < elements.length; i++) {
            try {
                count++;
            } catch (NumberFormatException e) {
                // do nothing
            }
        }

        return count;
    }

    /**
     * Int method that counts the amount of commas in a line
     * 
     * @param line
     * @return
     */
    private static int countCommas(String line) {
        int count = 0;

        // Iterate through each character in the string
        for (char c : line.toCharArray()) {
            // Check if the character is a comma
            if (c == ',') {
                count++;
            }
        }

        return count;
    }

    /**
     * Boolean method to go through a scenario to check for requirements that make
     * it valid
     * 
     * @param scenario
     * @return
     */
    private static boolean isValidScenario(ArrayList<String> scenario) {
        // If the scenario is empty, it's considered valid
        if (scenario.isEmpty()) {
            return true;
        }

        // Check if there is exactly 1 valid partition line in the scenario
        int validPartitionCount = 0;
        for (String line : scenario) {
            if (validatePartitionLine(line)) {
                validPartitionCount++;
            }
        }

        return validPartitionCount == 2;
    }

    private static boolean validParPair(List<Integer> par1, List<Integer> par2) {
        int sum1 = 0;
        int sum2 = 0;

        // Calculate sum of par1
        for (int i : par1) {
            sum1 += i;
        }
        // Calculate sum of par2
        for (int i : par2) {
            sum2 += i;
        }
        return sum1 == sum2;
    }

    // Helper function to convert String representation to List<Integer>
    private static List<Integer> convertPartitionToList(String partitionStr) {
        List<Integer> partition = new ArrayList<>();
        String[] strNumbers = partitionStr.split("\\s+"); // Split by whitespace
        for (String strNumber : strNumbers) {
            partition.add(Integer.parseInt(strNumber));
        }
        return partition;
    }

    // Helper method: Converts a FerrersBoard into a string representation
    private static String ferrersBoardToString(FerrersBoard board) {
        StringBuilder boardStr = new StringBuilder();
        for (int row : board.getBoard()) {
            boardStr.append(row + " "); // Append each row with spaces
        }

        return boardStr.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        File inputFile = new File("C:\\Users\\Andrew John\\source\\repos\\COSC326-Etudes\\Etude5\\input.txt");
        Scanner scanner = new Scanner(new FileReader(inputFile));
        ArrayList<String> inputArr = readLinesToArrayList(scanner);

        ArrayList<String> processedRawr = proReader(inputArr);
        for (String line : processedRawr) {
            System.out.println(line);
        }
    }
}