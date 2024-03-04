package Etude1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ParsingPartitions {
    
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        ArrayList<String> rawr = readLinesToArrayList(scanner);
        
        ArrayList<String> processedRawr = proReader(rawr);
        
        for (String line : processedRawr) {
            System.out.println(line);
        }

    }

    /**
     * Method to create an arraylist from a Scanner Object
     * @param scanner
     * @return listOfLines
     * @throws IOException
     */
    private static ArrayList<String> readLinesToArrayList(Scanner scanner) throws IOException {
        ArrayList<String> listOfLines = new ArrayList<>();
        while(scanner.hasNextLine()) {
            listOfLines.add(scanner.nextLine());
        }
        return listOfLines;
    }

    /**
     * Method that takes inputArr and returns a clean arraylist based on the requirements
     * @param inputArr
     * @return flattendScenarios
     */
    private static ArrayList<String> proReader(ArrayList<String> inputArr) {
        
        // Create a new ArrayList to store scenarios
        ArrayList<ArrayList<String>> scenarios = new ArrayList<>();

        // Create a new Arraylist to store current scenario's contents
        ArrayList<String> currentScenario = new ArrayList<>();

        // Read through each line of inputArr
        for(String line : inputArr) {
            // Add the line to the current scenario's contents
            line = identifyAndModifyLineType(line);
            currentScenario.add(line);
            if (identifyScenario(line)) { 
                
                if(onlyHyphensAndEmptyLines(currentScenario)){
                    currentScenario.clear();
                }
                else if(isValidScenario(currentScenario)){
                    scenarios.add(new ArrayList<>(currentScenario));
                    currentScenario.clear();
                } else {
                    // Handle invalid scenario
                    currentScenario.add(0, "# INVALID SCENARIO (Missing Valid Partition)");
                    scenarios.add(new ArrayList<>(currentScenario));
                    currentScenario.clear();
                }
            }
        }

        // Adding the last scenario if the scenario isn't empty
        if(!currentScenario.isEmpty()) {
            // Add the last scenario
            if(onlyHyphensAndEmptyLines(currentScenario)){
                currentScenario.clear();
            } else if(isValidScenario(currentScenario)){
                scenarios.add(new ArrayList<>(currentScenario));
                currentScenario.clear();
            } else {
                // Handle invalid scenario
                currentScenario.add(0, "# INVALID SCENARIO (Missing Valid Partition)");
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
                flattenedScenarios.remove(flattenedScenarios.size()-1);
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
     * String method to identify type of string and modify it to the desired output
     * @param line
     * @return
     */
    private static String identifyAndModifyLineType(String line) {
        if(emptyLine(line)){
            // null ?
        } else if (validateCommentLine(line)) {
            // null ?
        } else if (validatePartitionLine(line)){
            line = getDescendingOrder(line);
        } else if(identifyScenario(line)){
            line = "--------";
        } else {
            line = markInvalid(line);
        }
        return line;
    }

    /**
     * Boolean method to identify scenario lines
     * @param line
     * @return
     */
    private static boolean identifyScenario(String line) {
        return line.matches("-+");        
    }
 
    /**
     * Boolean method to go through a scenario to check for requirements that make it valid
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
    
        return validPartitionCount > 0;
    }    

    /**
     * Boolean method to identify empty line
     * @param line
     * @return
     */
    private static boolean emptyLine(String line) {
        return line.isEmpty();
    }

    /**
     * Method to replace consecutive emptylines with only 1 empty line in a scenario
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
     * Boolean method that validates Comment Lines
     * @param line
     * @return
     */
    private static boolean validateCommentLine(String line) {
        return line.startsWith("#");
    }

    /**
     * Boolean method that validates a partition line
     * @param line
     * @return
     */
    private static boolean validatePartitionLine(String line) {
        int index = 0;
        String[] delimiters = {",", " ", ", ", " ,"};
    
        while (index < delimiters.length) {
            String[] elements = line.split(delimiters[index]);
    
            int sum = 0;
            boolean validPartition = true;
    
            for (String element : elements) {
                try {
                    int num = Integer.parseInt(element);
                    sum += num;
                } catch (NumberFormatException e) {
                    // Handle invalid integers
                    validPartition = false;
                    break;  // Break out of the loop if an invalid integer is found
                }
            }
    
            if (validPartition && sum > 0) {
                return true;  // Valid partition found
            }
    
            index++;  // Move on to the next delimiter
        }
    
        return false;  // No consistent delimiter found
    }

    /**
     * Boolean method that return true if the scenario only has hyphens and empty lines
     * @param scenario
     * @return
     */
    private static boolean onlyHyphensAndEmptyLines(ArrayList<String> scenario) {
        int count = 0;
        for(String line : scenario) {
            if(emptyLine(line) || identifyScenario(line)) {
                count++;
            }
        }
        return count == scenario.size();
    }

    /**
     * Method to return input line into a descending order
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
     * Method to mark invalid lines
     * @param line
     * @return
     */
    private static String markInvalid(String line) {
        String newLine = "# INVALID " + line;
        return newLine;
    }

}
