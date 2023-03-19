If we don't have the .class files, first run <b>javac main.java</b> to compile the project. Run the project in the terminal with the following command: <br><br>
<b>java main "Flight Data.txt" "Requested Flight Plans.txt" "output.txt"</b>
<br><br>
It will output the result in the output.txt file. <br><br>
<b>
Flight Data.txt:</b><br>
- The first line of the file will contain an integer indicating how many rows of data will be in the file.<br>  
- Each subsequent row will contain two city names, the cost of the flight, and the number of minutes of the flight.  Each field will be separated with a pipe (shift-\ on most keyboards). 
<br><br>

<b>
Requested Flight Plans.txt:</b><br>

- The first line will contain an integer indicating the number of flight plans requested.<br>
- The subsequent lines will contain a pipe-delimited list of city pairs with a trailing character to indicate sorting the output of flights by time (T) or cost (C).<br>
<br>

The solution will find all flight paths between these two cities (if any exists) and calculate the total cost of the flights and the total time in the air. 