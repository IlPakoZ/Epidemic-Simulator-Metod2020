import sys.Core;
import sys.applications.CommandLineMenu;

public class Main {
    public static void main(String[] args) {
        Core x = new Core();
        x.newSimulation(new CommandLineMenu());
        System.out.println("Ho il coronavirus");
    }
}
