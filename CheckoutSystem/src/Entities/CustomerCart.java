package Entities;

import java.util.Collection;

public class CustomerCart {
    public int cartId;
    public int itemAmount;
    public double totalPrice;
    public Collection<Item> itemCollection;

    public CustomerCart(int cartId)
    {
       this.cartId = cartId;
       itemAmount = 0;
       totalPrice = 0;
    }

    public void add(Item item)
    {
        itemCollection.add(item);
    }
}
