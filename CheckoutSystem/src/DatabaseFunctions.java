import java.sql.*;

public class DatabaseFunctions {
    // function for adding an account to database
    // Needs information about person to insert
    
    private static Connection c = null;
    
    public static Connection connect() throws Exception {
        if (c == null) {
            c = (Connection) DriverManager.getConnection("jdbc:sqlite::resource:store.db");
        } else {
            c.close();
            c = (Connection) DriverManager.getConnection("jdbc:sqlite::resource:store.db");
        }
        return c;
    }
    
    static void addAccount(int pin, String name, boolean employee, boolean manager, long phone) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            String sql = "INSERT INTO Account (account_pin, employee, manager, name, phone_number, loyal, credit_points) "
                    + "VALUES (?, ?, ?, ?, ?, 1, 0.0);";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, pin);
            pstmt.setBoolean(2, employee);
            pstmt.setBoolean(3, manager);
            pstmt.setString(4, name);
            pstmt.setLong(5, phone);
            pstmt.executeUpdate();

            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    // function for verifying pin/phone combination
    // needs phone and pin returns account_id if valid, null otherwise
    static Integer verifyLogin(int id, int pin) {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            String sql = "SELECT * FROM Account WHERE account_id = ?";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt("account_pin") == pin) {
                    int rt = rs.getInt("account_id");
                    c.close();
                    return rt;
                }
            }
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }

    // function for verifying an employee/manager login
    // needs account pin/phone number and returns two boolean array
    // {employee status, manager status}
    static boolean[] getEmployee(int id, int pin) {
        boolean[] returnItems = new boolean[2];
        Integer test = verifyLogin(id, pin);
        if (test != null) {
            Connection c = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = connect();
                c.setAutoCommit(false);

                String sql = "SELECT * FROM Account WHERE account_id = ?";
                PreparedStatement pstmt = c.prepareStatement(sql);
                pstmt.setLong(1, id);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    int ret1 = rs.getInt("employee");
                    int ret2 = rs.getInt("manager");
                    if (ret1 >= 1) {
                        returnItems[0] = true;
                    } else {
                        returnItems[0] = false;
                    }
                    if (ret2 >= 1) {
                        returnItems[1] = true;
                    } else {
                        returnItems[1] = false;
                    }
                    c.close();
                    return returnItems;
                }
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        returnItems[0] = false;
        returnItems[1] = false;
        return returnItems;
    }

    // function to check customer's loyalty
    // needs phone number, returns integer array
    // {are they loyal, current credit points}
    static double[] checkLoyalty(long phone) {
        Connection c = null;
        double[] returnItems = new double[] { 0, 0 };
        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            String sql = "SELECT * FROM Account WHERE phone_number = ?";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setLong(1, phone);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                do {
                    returnItems[0] = rs.getInt("loyal");
                    returnItems[1] = rs.getDouble("credit_points");
                } while (rs.next());
            } else {
                addAccount(0000, "Not Available", false, false, phone);
                returnItems[0] = 0;
                returnItems[1] = 0;
            }
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return returnItems;
    }

    // function to update credit points
    // adds points to credit points
    static void updateCredit(long phone, double points) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            String sql = "SELECT * FROM Account WHERE phone_number = ?";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setLong(1, phone);
            ResultSet rs = pstmt.executeQuery();
            double total = 0;
            while (rs.next()) {
                total = rs.getDouble("credit_points");
            }
            sql = "UPDATE Account SET credit_points = ? WHERE phone_number = ?";
            pstmt = c.prepareStatement(sql);
            pstmt.setDouble(1, total + points);
            pstmt.setLong(2, phone);
            pstmt.executeUpdate();

            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // function for creating another inventory item
    // Needs information about person to insert
    static void addInventory(String name, double price, String manufacturer, int threshold, double inventory) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            String sql = "INSERT INTO Inventory (item_name, item_price, manufacturer, item_threshold, current_inventory) "
                    + "VALUES (?, ?, ?, ?, ?);";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setString(3, manufacturer);
            pstmt.setInt(4, threshold);
            pstmt.setDouble(5, inventory);
            pstmt.executeUpdate();

            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Function to change item inventory amounts
    // changes current inventory
    static void updateInventory(int id, double change) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            String sql = "SELECT * FROM Inventory WHERE item_id = ?";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            double total = 0;
            while (rs.next()) {
                total = rs.getDouble("current_inventory");
            }
            sql = "UPDATE Inventory SET current_inventory = ? WHERE item_id = ?";
            pstmt = c.prepareStatement(sql);
            pstmt.setDouble(1, total + change);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Function to change item inventory amounts
    // changes current inventory
    static String[] getItem(int id) {
        Connection c = null;
        String[] returnObjects = new String[2];

        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            String sql = "SELECT * FROM Inventory WHERE item_id = ?";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                returnObjects[0] = rs.getString("item_name");
                Double convert = rs.getDouble("item_price");
                returnObjects[1] = convert.toString();
            }
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return returnObjects;
    }

    // Function to change item threshold amounts
    // changes threshold
    static void updateThreshold(int id, int newThreshold) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            String sql = "UPDATE Inventory SET item_threshold = ? WHERE item_id = ?";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, newThreshold);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Function to get low inventory items and the amount they are low by
    // creates orders for said items, waiting to be approved
    static void getLowInventory() {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Inventory WHERE current_inventory < item_threshold");
            while (rs.next()) {
                addOrder(rs.getInt("item_threshold") - rs.getDouble("current_inventory") + 100, rs.getInt("item_id"),
                        c);
            }
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Function to change item price
    // changes price of item
    static void updatePrice(int id, double price) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            String sql = "UPDATE Inventory SET item_price = ? WHERE item_id = ?";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setDouble(1, price);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // function to create an inventory order
    // Status value meanings
    // 1- waiting for approval
    // 2- approved waiting to send
    // 3- sent waiting for delivery
    // 4- fulfilled
    static void addOrder(double amount, int id, Connection c) {

        try {
            boolean found = false;

            String sql = "SELECT * FROM Orders WHERE item_id = ? AND status != 4";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                found = true;
                break;
            }

            if (found == false) {
                sql = "INSERT INTO Orders (item_amount, item_id, status) " + "VALUES (?, ?, 1);";
                pstmt = c.prepareStatement(sql);
                pstmt.setDouble(1, amount);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
            }

            c.commit();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // function to create an inventory order
    // Status value meanings
    // 1- waiting for approval
    // 2- approved waiting to send
    // 3- sent waiting for delivery
    // 4- fulfilled
    static void updateStatus(int id, int status) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            String sql = "SELECT * FROM Orders WHERE item_id = ? AND status != 4";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            Integer order_id = null;
            Integer amt = null;
            while (rs.next()) {
                order_id = rs.getInt("order_id");
                amt = rs.getInt("item_amount");
                break;
            }

            sql = "UPDATE Orders SET status = ? WHERE order_id = ?";
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setInt(2, order_id);
            pstmt.executeUpdate();

            c.commit();
            c.close();
            if (status == 4) {
                updateInventory(id, amt);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    // Function to get orders not yet approved
    // creates orders for said items, waiting to be approved
    static String[] getOrders() {
        Connection c = null;
        String items = "";
        String amount = "";
        String[] returnString = new String[2];

        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Orders WHERE status = 1");
            while (rs.next()) {
                Integer item = rs.getInt("item_id");
                Integer amt = rs.getInt("item_amount");
                items = String.join(",", item.toString());
                amount = String.join(",", amt.toString());
            }
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        returnString[0] = items;
        returnString[1] = amount;
        return returnString;
    }

    // Function to get orders to be sent
    // creates orders for said items, waiting to be approved
    static String[] getOrdersSend() {
        Connection c = null;
        String items = "";
        String amount = "";
        String[] returnString = new String[2];

        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Orders WHERE status = 2");
            while (rs.next()) {
                Integer item = rs.getInt("item_id");
                Integer amt = rs.getInt("item_amount");
                items = String.join(",", item.toString());
                amount = String.join(",", amt.toString());
            }
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        returnString[0] = items;
        returnString[1] = amount;
        return returnString;
    }

    // function to add purchase to database
    // item amount and item list should be formatted like below:
    // item_amount = "number, number, number, number"
    // item_list = "id, id, id, id"
    // They are strings of integers separated by commas
    static int addPurchase(String item_amount, double total, String item_list, int account_id, String payment_info) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = connect();
            c.setAutoCommit(false);

            String sql = "INSERT INTO Purchase (item_amount, total_price, item_list, account_id, bank_info) "
                    + "VALUES (?, ?, ?, ?, ?);";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, item_amount);
            pstmt.setDouble(2, total);
            pstmt.setString(3, item_list);
            pstmt.setInt(4, account_id);
            pstmt.setString(5, payment_info);
            pstmt.executeUpdate();
            c.commit();

            sql = "SELECT * FROM Purchase WHERE bank_info = ?";
            PreparedStatement pstmt2 = c.prepareStatement(sql);
            pstmt2.setString(1, payment_info);
            ResultSet rs = pstmt2.executeQuery();
            Integer order_id = null;
            while (rs.next()) {
                order_id = rs.getInt("cart_id");
                c.close();
                return order_id;
            }
            c.close();
            return 0;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            return 0;
        }
    }
}
