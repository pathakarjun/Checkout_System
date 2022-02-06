public class InventoryControl {
    static void sendOrders() {
        String[] orders = DatabaseFunctions.getOrdersSend();
        try {
            String[] arrOfItems = orders[0].split(",");
            for (String item : arrOfItems) {
                int itm = Integer.parseInt(item);
                DatabaseFunctions.updateStatus(itm, 3);
            }
        } catch (Exception e) {
            ;
        }
    }

    static void recieveOrder(int item_id) {
        DatabaseFunctions.updateStatus(item_id, 4);
    }
}
