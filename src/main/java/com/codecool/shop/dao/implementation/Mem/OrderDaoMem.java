package com.codecool.shop.dao.implementation.Mem;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.order.Order;
import com.codecool.shop.order.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderDaoMem implements OrderDao {

    private List<Order> DATA = new ArrayList<>();
    private static OrderDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private OrderDaoMem() {
    }

    public static OrderDaoMem getInstance() {
        if (instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Order order) {
        DATA.add(order);
    }

    @Override
    public Order find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        DATA.remove(find(id));
    }

    @Override
    public List<Order> getAll() {
        return DATA;
    }

    @Override
    public Order getOpenByUserId(int userId) {
        return DATA.stream().filter(t -> (t.getUserId() == userId && t.getStatus() == OrderStatus.CART)).findFirst().orElse(null);
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        // TODO
        return null;
    }

    @Override
    public void updateOrderStatus(Order order) { }

    @Override
    public void updateOrderShippingId(int orderId, int shippingId) {

    }

    @Override
    public int getOrdersShippingId(Order order) {
        return 0;
    }
}
