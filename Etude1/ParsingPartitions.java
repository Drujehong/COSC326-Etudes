package Etude1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ParsingPartitions {
    
    public static void main(String[] args) throws IOException {

        String filePath = "C:\\Users\\Andrew John\\source\\repos\\COSC326-Etudes\\Etude1\\i0.txt";
        File file = new File(filePath);
        FileReader fileRead = new FileReader(file);

        ArrayList<String> rawr = readFileToArrayList(fileRead);
        
        ArrayList<String> processedRawr = proReader(rawr);
        
        for (String line : processedRawr) {
            System.out.println(line);
        }

    }

    /**
     * Method to create an arraylist from a fileReader Object
     * @param file
     * @return
     * @throws IOException
     */
    private static ArrayList<String> readFileToArrayList(FileReader file) throws IOException {
        BufferedReader bufReader = new BufferedReader(file);
        ArrayList<String> listOfLines = new ArrayList<>();

        String line = bufReader.readLine();
        while (line != null) {
        listOfLines.add(line);
        line = bufReader.readLine();
        }
        bufReader.close();
        return listOfLines;
    }

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
                    currentScenario.add(0, "# INVALID SCENARIO");
                    scenarios.add(new ArrayList<>(currentScenario));
                    currentScenario.clear();
                }
            }
        }

        // Add the last scenario
        if(onlyHyphensAndEmptyLines(currentScenario)){
            currentScenario.clear();
        } else if(isValidScenario(currentScenario)){
            scenarios.add(new ArrayList<>(currentScenario));
            currentScenario.clear();
        } else {
            // Handle invalid scenario
            currentScenario.add(0, "# INVALID SCENARIO");
            scenarios.add(new ArrayList<>(currentScenario));
            currentScenario.clear();
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

    private static String identifyAndModifyLineType(String line) {
        if(emptyLine(line)){
            line = line;
        } else if (validateCommentLine(line)) {
            line = line;   
        } else if (validatePartitionLine(line)){
            line = getDescendingOrder(line);
        } else if(identifyScenario(line)){
            line = "--------";
        } else {
            line = markInvalid(line);
        }
        return line;
    }

    private static boolean identifyScenario(String line) {
        return line.matches("-+");        
    }
 
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

    private static boolean emptyLine(String line) {
        return line.isEmpty();
    }

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
    
        // Add a single empty line if the last line was not empty
        if (!previousLineEmpty && !modifiedList.isEmpty()) {
            modifiedList.add("");
        }
    
        scenario.clear();
        scenario.addAll(modifiedList);
    }

    private static boolean validateCommentLine(String line) {
        return line.startsWith("#");
    }

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

    private static boolean onlyHyphensAndEmptyLines(ArrayList<String> scenario) {
        int count = 0;
        for(String line : scenario) {
            if(emptyLine(line) || identifyScenario(line)) {
                count++;
            }
        }
        return count == scenario.size();
    }
    
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

    private static String markInvalid(String line) {
        String newLine = "# INVALID " + line;
        return newLine;
    }

}
