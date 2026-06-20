import java.sql.*;
import java.util.Scanner;

public class EmployeePayrollSystem {

    static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    static final String USER = "system";
    static final String PASSWORD = "12345";

    static Connection getConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    static void insertEmployee(Scanner sc) {
        try (Connection con = getConnection()) {
            System.out.print("Enter Employee ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Employee Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Salary: ");
            double salary = sc.nextDouble();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO employee VALUES (?, ?, ?)"
            );

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setDouble(3, salary);

            ps.executeUpdate();
            con.commit();

            System.out.println("Employee inserted successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void viewEmployees() {
        try (Connection con = getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM employee ORDER BY empid");

            System.out.println("\n--------------------------------------");
            System.out.printf("%-10s %-15s %-10s%n", "ID", "NAME", "SALARY");
            System.out.println("--------------------------------------");

            while (rs.next()) {
                System.out.printf(
                    "%-10d %-15s %-10.2f%n",
                    rs.getInt("empid"),
                    rs.getString("empname"),
                    rs.getDouble("salary")
                );
            }

            System.out.println("--------------------------------------\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void updateSalary(Scanner sc) {
        try (Connection con = getConnection()) {
            System.out.print("Enter Employee ID: ");
            int id = sc.nextInt();

            System.out.print("Enter New Salary: ");
            double salary = sc.nextDouble();

            PreparedStatement ps = con.prepareStatement(
                "UPDATE employee SET salary = ? WHERE empid = ?"
            );

            ps.setDouble(1, salary);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            con.commit();

            if (rows > 0)
                System.out.println("Salary updated successfully.");
            else
                System.out.println("Employee not found.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void deleteEmployee(Scanner sc) {
        try (Connection con = getConnection()) {
            System.out.print("Enter Employee ID to delete: ");
            int id = sc.nextInt();

            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM employee WHERE empid = ?"
            );

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            con.commit();

            if (rows > 0)
                System.out.println("Employee deleted successfully.");
            else
                System.out.println("Employee not found.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void calculateBonus(Scanner sc) {
        System.out.print("Enter Salary: ");
        double salary = sc.nextDouble();
        double bonus = salary * 0.10;
        System.out.println("Bonus (10%): " + bonus);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Employee Payroll System ===");
            System.out.println("1. Insert Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Update Salary");
            System.out.println("4. Delete Employee");
            System.out.println("5. Calculate Bonus");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    insertEmployee(sc);
                    break;
                case 2:
                    viewEmployees();
                    break;
                case 3:
                    updateSalary(sc);
                    break;
                case 4:
                    deleteEmployee(sc);
                    break;
                case 5:
                    calculateBonus(sc);
                    break;
                case 6:
                    System.out.println("Thank you!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
