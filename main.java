import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

// This program assuming the graph is undirected and the city in requested flight is in the adjList
// Meaning there are exactly 2 edges between 2 vertices
// For example: Austin --> Billing and Billing --> Austin

public class main {
    static BufferedReader input = null;
    static BufferedWriter writer = null;
    static Stack<attribute> stack = new Stack<attribute>();
    static ArrayList<Stack<attribute>> paths = new ArrayList<Stack<attribute>>();
    
    public static void main(String[] args) {
        String origin, destination, type;
        String inputLine = "";

        // check number of files
        if (args.length != 3) {
            System.out.println("Number of Files needs to be 3. 2 input files and 1 output file");
            return;
        }

        String file = args[0];

        // Make adjList
        flightData adjList = new flightData(file);

        // Initiate bufferedWriter for output
        Path filePath = Path.of(args[2]);

        try {
            writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        // Initiate bufferedReader for input
        try {
            input = new BufferedReader(new FileReader(args[1]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // How many line of input
        int lines = 0;

        try {  
            lines = Integer.parseInt(input.readLine());  
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Loop through each requests
        for (int numLoop = 0; numLoop < lines; numLoop++) {
            
            // for each request, make array to store visited node
            ArrayList<String> visisted = new ArrayList<String>();

            // read in the request
            try {  
                inputLine = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // split the request into origin, destination and type of sort
            String[] inputArr = inputLine.split(Pattern.quote("|"));
            origin = inputArr[0];
            destination = inputArr[1];
            type = inputArr[2];

            // find the index of the origin city in the adjList
            int index = adjList.orginCity.indexOf(origin);
            
            LinkedList<attribute> current = new LinkedList<attribute>();
            
            // set current to the list of destination of the origin city
            current = adjList.arrayCity.get(index);

            // set currentIndex = 0 and push it to stack, also mark it visited
            current.peek().currentIndex = 0;
            stack.push(current.peek());
            visisted.add(current.peek().city);

            // traverse through the adjList, only end when stack is empty
            while (!stack.empty()) {   
                // check if we already went through all the destination of a certain city
                if (stack.peek().currentIndex < (int)adjList.sizeOfLinkedList.get(index) - 1) {

                    stack.peek().currentIndex++;
                    
                    // keep the index in case we meet dead-end --> backtrack
                    int tempIndex = index;

                    //find index of that city in orginCity array
                    index = adjList.orginCity.indexOf(current.get(stack.peek().currentIndex).city);

                    // handle cycle
                    if (!visisted.contains(current.get(stack.peek().currentIndex).city)) {
                        stack.push(current.get(stack.peek().currentIndex));
                        current = adjList.arrayCity.get(index);
                        visisted.add(current.peek().city);
                    } else {
                        // dead-end --> backtrack or continue to next destination
                        index = tempIndex;
                        continue;
                    }
                   
                    // if found the destination, pop stack and continue
                    if (current.peek().city.equals(destination)) {
                        // unmark it
                        visisted.remove(current.peek().city);

                        // make a deep copy and store it
                        Stack<attribute> copiedStack = (Stack<attribute>) stack.clone();
                        
                        // add to array of stack
                        paths.add(copiedStack);

                        //pop here to backtrack
                        visisted.remove(stack.peek().city);
                        stack.peek().currentIndex = 0;
                        stack.pop();

                        // reset the variable after backtrack
                        index = adjList.orginCity.indexOf(stack.peek().city);
                        current = adjList.arrayCity.get(index);
                    } 
                } else {
                    // if we visited all destination of a city, pop it out and reset the variable
                    // for backtracking
                    visisted.remove(stack.peek().city);
                    stack.peek().currentIndex = 0;

                    stack.pop();          

                    // only reset when stack is not empty
                    if (!stack.empty()) {
                        index = adjList.orginCity.indexOf(stack.peek().city);
                        current = adjList.arrayCity.get(index);
                    }     
                }
            }

            // Sort and Print all the possible paths by time or cost
            if (type.equals("T")) {
                try {
                    writer.write("\nFlight " + (numLoop + 1) + ": " + origin + ", " + destination + " (Time)\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }    
            }
            else {
                try {
                    writer.write("\nFlight " + (numLoop + 1) + ": " + origin + ", " + destination + " (Cost)\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sortAndPrintToFile(type);
            
            // Clear the paths, ready for next request
            paths.clear();
        }

        // close output stream
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // close input stream
        try {
            input.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void sortAndPrintToFile(String type) {
        Stack<attribute> reversePath;
        ArrayList<Integer> costPath = new ArrayList<Integer>();
        ArrayList<Integer> timePath = new ArrayList<Integer>();

        // If there is no path to a destination, print out the statement and return
        if (paths.size() == 0) {
            try {
                writer.write("There is no possible path for your requested flight plan");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // Reverse the paths (because it was stored in stack) and calculate cost and time of each paths
        for (int arrS = 0; arrS < paths.size(); arrS++) {
            reversePath = new Stack<attribute>();
            int cost = 0;
            int time = 0;

            while(!paths.get(arrS).empty()) {
                
                cost += paths.get(arrS).peek().cost;
                time += paths.get(arrS).peek().time;
                reversePath.add(paths.get(arrS).pop());
            }

            costPath.add(arrS, cost);
            timePath.add(arrS, time);

            paths.set(arrS, reversePath);
        }

        // Sort by type, time or cost
        if (type.equals("C")) {
            for (int i = 0; i < costPath.size(); i++) {  
                for (int j = i + 1; j < costPath.size(); j++) {  
                    Stack<attribute> tempPath;
                    int tmp = 0;  
                    int timetmp = 0;
                    if (costPath.get(i) > costPath.get(j)) {  
                        tmp = costPath.get(i);
                        timetmp = timePath.get(i);
                        tempPath = paths.get(i);
                        costPath.set(i, costPath.get(j));  
                        timePath.set(i, timePath.get(j));
                        paths.set(i, paths.get(j));
                        costPath.set(j, tmp);  
                        timePath.set(j, timetmp);
                        paths.set(j, tempPath);
                    }  
                }
            }  

        } else if (type.equals("T")) {
            for (int i = 0; i < timePath.size(); i++) {  
                for (int j = i + 1; j < timePath.size(); j++) {  
                    Stack<attribute> tempPath;
                    int tmp = 0;  
                    int costtmp = 0;
                    if (timePath.get(i) > timePath.get(j)) {  
                        tmp = timePath.get(i);  
                        costtmp = costPath.get(i);
                        tempPath = paths.get(i);
                        timePath.set(i, timePath.get(j));
                        costPath.set(i, costPath.get(j));
                        paths.set(i, paths.get(j));
                        timePath.set(j, tmp);  
                        costPath.set(j, costtmp);
                        paths.set(j, tempPath);
                    }  
                }
            }
        }

        // Print the paths in order after sorted
        print(costPath, timePath);
    }
    
    public static void print(ArrayList<Integer> costPath, ArrayList<Integer> timePath) {
        if (paths.size() <= 3) {
            // Print out all the paths
            for (int arrS = 0; arrS < paths.size(); arrS++) {
                try {
                    writer.write("Path " + (arrS + 1) + ": " + paths.get(arrS).pop().city);
                    while(!paths.get(arrS).empty())
                        writer.write(" --> " + paths.get(arrS).pop().city);
                    writer.write(". Time: " + timePath.get(arrS) + " Cost: " + costPath.get(arrS) + ".00\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // only print 3 fastest paths based on given type for sorting
            for (int arrS = 0; arrS < 3; arrS++) {
                try {
                    writer.write("Path " + (arrS + 1) + ": " + paths.get(arrS).pop().city);
                    while(!paths.get(arrS).empty())
                        writer.write(" --> " + paths.get(arrS).pop().city);
                    writer.write(". Time: " + timePath.get(arrS) + " Cost: " + costPath.get(arrS) + ".00\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}