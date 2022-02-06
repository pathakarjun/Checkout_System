import java.sql.*;

public class Database {
   public static void main(String args[]) {
      Connection c = null;
      Statement stmt = null;

      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite::resource:store.db");
         stmt = c.createStatement();
         String sql = "CREATE TABLE Account" 
               + "(account_id      INTEGER  PRIMARY KEY  AUTOINCREMENT,"
               + " account_pin     INT      NOT NULL, " + " employee        INT      NOT NULL, "
               + " manager         INT      NOT NULL, " + " name            CHAR(50) NOT NULL, "
               + " phone_number    INT      NOT NULL, " + " loyal           INT      NOT NULL,"
               + " credit_points   REAL     NOT NULL)";
         stmt.executeUpdate(sql);
         stmt = c.createStatement();
         sql = "CREATE TABLE Inventory" 
               + "(item_id             INTEGER     PRIMARY KEY  AUTOINCREMENT,"
               + " item_name           char(255)   NOT NULL, " + " item_price      REAL        NOT NULL, "
               + " manufacturer        CHAR(255)   NOT NULL, " + " item_threshold  INT         NOT NULL, "
               + " current_inventory   Real        NOT NULL, " + " item_discount   REAL        NOT NULL)";
         stmt.executeUpdate(sql);
         stmt = c.createStatement();
         sql = "CREATE TABLE Orders" 
               + "(order_id        INTEGER     PRIMARY KEY  AUTOINCREMENT,"
               + " item_amount     INT         NOT NULL, " + " item_id         INT         NOT NULL, "
               + " status          INT         NOT NULL, " + " FOREIGN KEY(item_id) REFERENCES Inventory(item_id))";
         stmt.executeUpdate(sql);
         stmt = c.createStatement();
         sql = "CREATE TABLE Purchase" 
               + "(cart_id         INTEGER     PRIMARY KEY  AUTOINCREMENT,"
               + " item_amount     char(255)   NOT NULL, " + " total_price     REAL        NOT NULL, "
               + " item_list       char(255)   NOT NULL, " + " account_id      INT         NOT NULL, "
               + " bank_info       char(255)   NOT NULL, " + " FOREIGN KEY(account_id) REFERENCES Account(account_id))";
         stmt.executeUpdate(sql);
         stmt.close();
         c.close();
         System.out.println("Tables created successfully.");
      } catch (Exception e) {
         System.err.println(e.getClass().getName() + ": " + e.getMessage());
         System.exit(0);
      }
   }
}
