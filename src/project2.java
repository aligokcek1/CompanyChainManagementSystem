import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class project2 {
    public static PrintWriter out;

    public static void main(String[] args) throws FileNotFoundException {
        //TAKING INPUT FILES
        String initial_input = args[0];
        String output_file_name = args[2];
        File initial_file = new File(initial_input);
        Scanner initial_scanner = new Scanner(initial_file);
        out = new PrintWriter(output_file_name);

        //CREATING CITY AND FILL IT WITH INITIAL INPUT
        City city = new City();
        while (initial_scanner.hasNextLine()) {
            String line, branchId, empName, empType;
            line = initial_scanner.nextLine();
            String[] lineArray = line.split(",");
            empType = lineArray[3].strip();
            empName = lineArray[2].strip();
            branchId = lineArray[0].strip() + "," + lineArray[1].strip();

            city.initialAddEmployee(branchId, empName, empType);
        }
        initial_scanner.close();

        // UPDATE PART
        String input_file = args[1];
        File input = new File(input_file);
        Scanner input_scanner = new Scanner(input);

        // MONTHLY UPDATE PART
        while (input_scanner.hasNextLine()) {
            String line, branchId, empName, empType, function;
            String[] afterFunction;
            line = input_scanner.nextLine();
            String[] lineArray = line.split(":");

            if (lineArray.length == 1) {
                city.resetMonthlyBonus();

            } else {
                function = lineArray[0];
                afterFunction = lineArray[1].split(",");
                branchId = afterFunction[0].strip() + "," + afterFunction[1].strip();
                switch (function) {
                    case "PRINT_MANAGER": // PRINTING THE MANAGER OF THE DESIRED BRANCH
                        city.printManager(branchId);
                        break;
                    case "PERFORMANCE_UPDATE":  // INCREASE OR DECREASE THE PERF. SCORE OF AN EMPLOYEE
                        // AND DECISION OF FIRING HIM OR NOT
                        empName = afterFunction[2].strip();
                        int score = Integer.parseInt(afterFunction[3].strip());
                        city.performanceUpdate(branchId, empName, score);
                        break;
                    case "ADD": // ADDING A NEW EMPLOYEE
                        empName = afterFunction[2].strip();
                        empType = afterFunction[3].strip();
                        city.addEmployee(branchId, empName, empType);
                        break;
                    case "PRINT_MONTHLY_BONUSES":
                        city.printMonthlyBonus(branchId);
                        break;
                    case "PRINT_OVERALL_BONUSES":
                        city.printTotalBonus(branchId);
                        break;
                    case "LEAVE": // WHEN AN EMPLOYEE WANTS TO LEAVE
                        empName = afterFunction[2].strip();
                        city.leave(branchId, empName);
                        break;
                }


            }

        }
        out.close();
        input_scanner.close();
    }

}