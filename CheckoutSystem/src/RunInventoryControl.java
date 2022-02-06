public class RunInventoryControl {
    static void runInventory(){
        DatabaseFunctions.getLowInventory();
    }

    static String[] getOrders(){
        return DatabaseFunctions.getOrders();
    }

    static void approveOrder(int item_id){
        DatabaseFunctions.updateStatus(item_id, 2);
    }
}
