// BRADEN JOHNSTON 20005898 - 159234 AS3 2022
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Store {  // Store class, used for constructing simulated objects

    public static void main(String[] args) {
        Staff sale0 = new Staff(false, "p1", "p1");  // make staff
        Staff sale1 = new Staff(false, "p2", "p2");
        Staff sale2 = new Staff(false, "p3", "p3");
        Staff man0 = new Staff(true, "m1", "m1");
        Staff man1 = new Staff(true, "m2", "m2");

        List<Staff> staffList = Arrays.asList(sale0, sale1, sale2, man0, man1);  // Lists to pass into GUI for easy manipulation
        List<Computer> stock = new ArrayList<>();
        File file = new File("src/computers.txt");  // input file

        try {
            Scanner sc = new Scanner(file);  // read file
            while (sc.hasNextLine()) {
                String[] computer = sc.nextLine().split(",");
                stock.add(new Computer(computer));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GUI gui = new GUI(staffList, stock);  // start GUI program
        gui.run();
    }
}
