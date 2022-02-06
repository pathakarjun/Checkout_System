package Entities;

public class PurchaseInfo extends CustomerCart {
    public String customerName;
    public String bankInfo;

    public PurchaseInfo(int cartId) {
        super(cartId);
    }
}
