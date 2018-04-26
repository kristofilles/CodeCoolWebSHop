package com.codecool.shop.dao;

import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.ShippingDetail;
import com.codecool.shop.order.Order;

import java.util.List;

public interface ShippingDao {

    void add(ShippingDetail shippingDetail);

    ShippingDetail find(int id);

    void remove(int id);

    List<ShippingDetail> getAll();

    List<ShippingDetail> getBy(int userId);

    public String getEmailByOrderId(Order order, int userId);

}
