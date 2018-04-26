package com.codecool.shop;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.JDBC.*;
import com.codecool.shop.dao.implementation.Mem.*;

/**
 * All the calls from other parts of the program to DAO implementations arrive here.
 * <p>The common interface guarantees an easy switch between implementations.</p>
 */
public class Globals {
/*
    public static ProductDao productDao = ProductDaoMem.getInstance();
    public static ProductCategoryDao productCategoryDao = ProductCategoryDaoMem.getInstance();
    public static SupplierDao supplierDao = SupplierDaoMem.getInstance();
    public static OrderDao orderDao = OrderDaoMem.getInstance();
    public static LineItemDao lineItemDao = LineItemDaoMem.getInstance();*/

    public static ProductDao productDao = ProductDaoJDBC.getInstance();
    public static ProductCategoryDao productCategoryDao = ProductCategoryJDBC.getInstance();
    public static SupplierDao supplierDao = SupplierDaoJDBC.getInstance();
    public static OrderDao orderDao = OrderDaoJDBC.getInstance();
    public static LineItemDao lineItemDao = LineItemDaoJDBC.getInstance();
    public static UserDao userDao = UserDaoJDBC.getInstance(); // TODO: mem implementation
    public static ShippingDao shippingDao = ShippingDaoJDBC.getInstance();
}
