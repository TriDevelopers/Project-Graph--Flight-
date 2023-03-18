/*
    This class make an adjacency list from the input file
*/

// undirected graph, auto populate

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class flightData{
    static BufferedReader input = null;

    // array to hold the order of city in the arrayCity arraylist
    public ArrayList orginCity = new ArrayList<String>();

    // array to hold the size of each linkedlist in the arrayCity arraylist
    public ArrayList sizeOfLinkedList = new ArrayList<Integer>();

    // array of linkedlist, this will be our adjacency list
    public ArrayList<LinkedList<attribute>> arrayCity = new ArrayList<LinkedList<attribute>>();

    // Constructor pass in the input file and read it
    flightData(String filename) {
        readData(filename);
    }

    // function to read the file and make adjacency list
    public void readData(String filename) {
        String inputLine = "";
        int numInputLine = 0;
        
        // initiate the buffer reader
        try {
            input = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Read in how many input lines
        try {  
            numInputLine = Integer.parseInt(input.readLine());  
        } catch (IOException e) {
            e.printStackTrace();
        }

        // loop through the input file to make the list
        for (int i = 0; i < numInputLine; i++) {

            // read in each line
            try {  
                inputLine = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // split the line into components
            String[] inputArr = inputLine.split(Pattern.quote("|"));
            
            // if the origin isn't added, add it. If it's added, add a new destination to it
            if (!orginCity.contains(inputArr[0])) {
                orginCity.add(inputArr[0]);

                // make a linkedlist of destination for the city
                LinkedList listDestination = new LinkedList<attribute>();

                // add the origion to the list first
                attribute temp = new attribute(inputArr[0], 0, 0);
                listDestination.add(temp);

                // add the first destination
                temp = new attribute(inputArr[1], Integer.parseInt(inputArr[2]), Integer.parseInt(inputArr[3]));
                listDestination.add(temp);

                // add to the adjcency list
                arrayCity.add(listDestination);

            } else {
                // find the position of the list of destination of that city
                int index = orginCity.indexOf(inputArr[0]);

                // add the next destination
                attribute temp = new attribute(inputArr[1], Integer.parseInt(inputArr[2]), Integer.parseInt(inputArr[3]));
                arrayCity.get(index).add(temp);
            }

            // Add an edge to go back, make it undirected

            String tempStringForArray = inputArr[0];
            inputArr[0] = inputArr[1];
            inputArr[1] = tempStringForArray;

            if (!orginCity.contains(inputArr[0])) {
                orginCity.add(inputArr[0]);

                // make a linkedlist of destination for the city
                LinkedList listDestination = new LinkedList<attribute>();

                // add the origion to the list first
                attribute temp = new attribute(inputArr[0], 0, 0);
                listDestination.add(temp);

                // add the first destination
                temp = new attribute(inputArr[1], Integer.parseInt(inputArr[2]), Integer.parseInt(inputArr[3]));
                listDestination.add(temp);

                // add to the adjcency list
                arrayCity.add(listDestination);

            } else {
                // find the position of the list of destination of that city
                int index = orginCity.indexOf(inputArr[0]);

                // add the next destination
                attribute temp = new attribute(inputArr[1], Integer.parseInt(inputArr[2]), Integer.parseInt(inputArr[3]));
                arrayCity.get(index).add(temp);
            }
        }

        // populate the array that hold size/number of destinations for each city
        for (int i = 0; i < arrayCity.size(); i++) {
            sizeOfLinkedList.add(arrayCity.get(i).size());
        }
    } 
}