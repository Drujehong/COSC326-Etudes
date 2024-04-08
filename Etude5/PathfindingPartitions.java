package Etude5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PathfindingPartitions {

    /**
     * Method to create an arraylist from a Scanner Object
     * @param scanner
     * @return listOfLines
     * @throws IOException
     */
    private static ArrayList<String> readLinesToArrayList(Scanner scanner){
        ArrayList<String> listOfLines = new ArrayList<>();
        while(scanner.hasNextLine()) {
            listOfLines.add(scanner.nextLine());
        }
        return listOfLines;
    }

    private static void handlePathfinding(ArrayList<String> inputArr) {
        ArrayList<String> currScenario = new ArrayList<>();
        ArrayList<String> scenario = new ArrayList<>();

        for (int i = 0; i < inputArr.size(); i++) { // Loop through every line (using an index)
            String line = inputArr.get(i); 
            currScenario.add(line);
        
            if (line.matches("-{3,}") || i == inputArr.size() - 1) { // Check for separator or last line
                if (line.matches("-{3,}")) { // Only remove if it's the separator
                    currScenario.remove(line);
                }
        
                if (currScenario.size() < 3 && currScenario.size() > 1) {
                    String line1 = currScenario.get(0);
                    String line2 = currScenario.get(1);
                    //TODO: IMPLEMENT LOGIC HERE!
                } else {
                    // INVALID PARTITION
                }
                currScenario.clear(); // Clear the scenario after processing
            }
        }
    }

    private static boolean validParPair(int[] par1, int[] par2) {
        int sum1 = 0;
        int sum2 = 0;
        
        // Calculate sum of par1
        for(int i : par1) {
            sum1 += i;
        }
        // Calculate sum of par2
        for(int i : par2) {
            sum2 += i;
        }

        return sum1 == sum2;
    }

    public static void main(String[] args) throws FileNotFoundException{
        File inputFile = new File("C:\\Users\\Andrew John\\source\\repos\\COSC326-Etudes\\Etude5\\input.txt");  // Specify input file path
        Scanner scanner = new Scanner(new FileReader(inputFile));
        ArrayList<String> inputArr = readLinesToArrayList(scanner);
        
        handlePathfinding(inputArr);
    }
}