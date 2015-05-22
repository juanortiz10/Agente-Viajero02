package agenteviajero02;

public class Main {
public static void main(String[] args) {
        Model md= new Model();
        md.readDocument();
        md.generateArray();
        int firstRoad=md.generateFirstRoad();
        System.out.println("The first road is "+firstRoad);
        md.generateNextRoad(firstRoad);
        System.out.println("The solution is "+md.road);

        System.out.println("\nThe counter of tour is "+md.sizeTour);
    }
}
