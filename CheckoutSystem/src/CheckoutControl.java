import java.util.concurrent.TimeUnit;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

public class CheckoutControl {
    //Funciton to checkout customer
    //Requires total

    //function to run card, requires total, card, pin, and user account number
    // item_amount = "number, number, number, number"
    // item_list = "id, id, id, id"
    static boolean card(double total, long card, int pin, String item_amount, String item_list, int id){
        boolean bank = BankInterface.verify(pin, card);
        if (bank){
            String[] arrOfAmts = item_amount.split(",");
            String[] arrOfItems = item_list.split(",");
            int i = 0;
            for (String item: arrOfItems){
                item = item.replace(" ", "");
                arrOfAmts[i] = arrOfAmts[i].replace(" ", "");
                int itm = Integer.parseInt(item);
                int amt = Integer.parseInt(arrOfAmts[i]);
                int change = (~(amt - 1));
                DatabaseFunctions.updateInventory(itm, change);
                i++;
            }
            Double tot = total;
            String payment = String.join(" ", "card", tot.toString());
            DatabaseFunctions.addPurchase(item_amount, total, item_list, id, payment);
            return true;
        }
       return false;
    }

    //function to run check, needs 
    // item_amount = "number, number, number, number"
    // item_list = "id, id, id, id"
    static boolean check(double total, long check, String item_amount, String item_list, int id){
        boolean valid = CheckReader.verify(check);
        if (valid){
            Long ck = check;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();
            String payment = String.join(" ", "check", ck.toString());
            Integer order = DatabaseFunctions.addPurchase(item_amount, total, item_list, id, payment);
            String[] arrOfAmts = item_amount.split(",");
            String[] arrOfItems = item_list.split(",");
            int i = 0;
            for (String item: arrOfItems){
                item = item.replace(" ", "");
                arrOfAmts[i] = arrOfAmts[i].replace(" ", "");
                int itm = Integer.parseInt(item);
                int amt = Integer.parseInt(arrOfAmts[i]);
                int change = (~(amt - 1));
                DatabaseFunctions.updateInventory(itm, change);
                i++;
            }
            //simulates printing on check
            try {
                TimeUnit.SECONDS.sleep(2);  // Wait 2 seconds
            }
            catch (InterruptedException e) {
                ;
            }
            return true;
        }
        return false;
    }

    //function to run cash, needs cash given, account id, etc.
    // item_amount = "number, number, number, number"
    // item_list = "id, id, id, id"
    public static void cash(double total, double cash, String item_amount, String item_list, int id){
  
        String[] arrOfAmts = item_amount.split(",");
        String[] arrOfItems = item_list.split(",");
        int i = 0;
        for (String item: arrOfItems){
            item = item.replace(" ", "");
            arrOfAmts[i] = arrOfAmts[i].replace(" ", "");
            int itm = Integer.parseInt(item);
            int amt = Integer.parseInt(arrOfAmts[i]);
            int change = (~(amt - 1));
            DatabaseFunctions.updateInventory(itm, change);
            i++;
        }
        Double tot = total;
        String payment = String.join(" ", "cash", tot.toString());
        DatabaseFunctions.addPurchase(item_amount, total, item_list, id, payment);
    }
}
