package com.codecool.shop.dao;

import com.codecool.shop.order.LineItem;
import com.codecool.shop.order.Order;

import javax.sound.sampled.Line;
import java.util.List;

public interface LineItemDao {

    void add(LineItem lineitem);
    LineItem find(int id);
    void changeQuantity(int id, int quantityChange);
    void remove(LineItem item);
    List<LineItem> getAllByOrderId(int orderId);
    List<LineItem> getAll();
    void setQuantity(int id, int quantity);
    }
