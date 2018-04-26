package com.codecool.shop.order.orderProcesses;

import com.codecool.shop.order.LineItem;
import com.codecool.shop.order.Order;

/**
 * Concrete process for adding or updating a {@link LineItem} of the {@link Order}
 */
public class AddToCart extends AbstractProcess {

    /**
     * The enum type of the concrete Process
     */
    private ProcessName name;
    /**
     * The LineItem being added to cart
     */
    private LineItem item;

    /**
     * Constructor for the concrete process that calls for the AbstractProcess superclass
     * @param order The Order on which it gets called
     * @param item  The LineItem which it adds to the order
     */
    public AddToCart(Order order, LineItem item) {
        super(order);
        this.item = item;
        name = ProcessName.AddToCart;
    }

    /**
     * Returns the enum type name of the process
     * @return enum type Processname
     */
    @Override
    public ProcessName getName() {
        return name;
    }

    /**
     * The process of adding or updating a lineitem belonging to an order
     */
    @Override
    public void process() {
        order.updateLineItem(item, 1);
    }
}
