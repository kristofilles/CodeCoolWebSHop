package com.codecool.shop.dao;

import com.codecool.shop.order.Order;

import java.util.List;

public interface OrderDao {

    void add(Order order);
    Order find(int id);
    void remove(int id);
    List<Order> getAll();
    Order getOpenByUserId(int userId);
    List<Order> getOrdersByUserId(int userId);
    void updateOrderStatus(Order order);
    void updateOrderShippingId(int orderId, int shippingId);
    int getOrdersShippingId(Order order);

}
