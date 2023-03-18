/*  
    This class holds the cost and time to a certain city from a specified origin.
    It also holds the index of iteration when backtracking
*/

public class attribute {
    String city;
    int cost;
    int time;
    int currentIndex;

    attribute(String city, int cost, int time) {
        this.city = city;
        this.cost = cost;
        this.time = time;
    }
}
