package com.codecool.shop.dao.implementation.Mem;

import com.codecool.shop.dao.LineItemDao;
import com.codecool.shop.order.LineItem;
import com.codecool.shop.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineItemDaoMem implements LineItemDao {

    private List<LineItem> DATA = new ArrayList<>();
    private static LineItemDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private LineItemDaoMem() {
    }

    public static LineItemDaoMem getInstance() {
        if (instance == null) {
            instance = new LineItemDaoMem();
        }
        return instance;
    }
    @Override
    public void add(LineItem lineitem) {
        DATA.add(lineitem);
    }

    @Override
    public LineItem find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void changeQuantity(int id, int quantityChange) {
        find(id).changeQuantity(quantityChange);
    }

    @Override
    public void setQuantity(int id, int quantity) {
        find(id).setQuantity(quantity);
    }

    @Override
    public void remove(LineItem item) {
        DATA.remove(item);
    }

    @Override
    public List<LineItem> getAllByOrderId(int orderId) {
        return DATA.stream().filter(t -> t.getOrderId() == orderId).collect(Collectors.toList());
    }

    @Override
    public List<LineItem> getAll() {
        return DATA;
    }
}
