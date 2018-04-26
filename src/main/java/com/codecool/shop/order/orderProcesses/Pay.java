package com.codecool.shop.order.orderProcesses;

import com.codecool.shop.Globals;
import com.codecool.shop.order.Order;
import com.codecool.shop.order.OrderStatus;

public class Pay extends AbstractProcess {
    /**
     * The enum type of the concrete Process
     */
    private ProcessName name;
    /**
     * Abstract constructor for processes
     *
     * @param order The order field gets initialized through the constructor
     */
    public Pay(Order order) {
        super(order);
        name = ProcessName.AddToCart;
    }

    @Override
    public ProcessName getName() {
        return name;
    }

    @Override
    public void process() {
        order.setStatus(OrderStatus.PAID);
        Globals.orderDao.updateOrderStatus(order);
    }
}
