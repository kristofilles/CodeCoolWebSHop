package com.codecool.shop.order;

import com.codecool.shop.Globals;
import com.codecool.shop.dao.implementation.Mem.LineItemDaoMem;
import com.codecool.shop.dao.implementation.Mem.ProductDaoMem;
import com.codecool.shop.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A container class which among others consits of one {@link Product}, its quantity, and an {@link Order}'s id
 * which it belongs to.
 */
public class LineItem {
    /**
     * Logger class instance
     */
    private Logger logger = LoggerFactory.getLogger(LineItem.class);
    /**
     * The id of the lineitem which is unique
     */
    private final int id;
    /**
     * The product object which it holds
     */
    private final Product product;
    /**
     * The unique id of this product
     */
    private final int productId;
    /**
     * The current quantity of the given product in this order
     */
    private int quantity;
    /**
     * The order's id which this object belongs to
     */
    private int orderId;

    /**
     * The constructor for this class for first building objects
     *
     * <p>It differs from the other constructor in generating its own unique id and quantity</p>
     * @param productId
     * @param orderId
     */
    public LineItem(int productId, int orderId) {
        id = Globals.lineItemDao.getAll().size();
        this.orderId = orderId;
        this.productId = productId;
        product = Globals.productDao.find(productId);
        quantity = 0;
    }

    /**
     * The second constructor for rebuilding the object based on database data
     * @param id
     * @param product
     * @param productId
     * @param quantity
     * @param orderId
     */
    public LineItem(int id, Product product, int productId, int quantity, int orderId) {
        this.id = id;
        this.product = product;
        this.productId = productId;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    /**
     * Returns the product object which this lineitem holds
     * @return
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Returns the product id which this lineitem holds
     * @return
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Returns the quantity of the product which this lineitem holds
     * @return
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the current quantity to the param quantity
     * @param quantity The new quantity of the product
     */
    public void setQuantity(int quantity) {
        logger.info("Changing quantity of lineitem {} from {} to {}", id, this.quantity, quantity);
        this.quantity = quantity;
    }

    /**
     * Returns the product price multiplied by quantity
     * @return
     */
    public String getFullPrice() {
        return String.valueOf(product.getDefaultPrice() * quantity) + " " + product.getDefaultCurrency().toString();
    }

    /**
     * Changes the quantity of the product by the quantity given as a parameter
     * @param quantity
     */
    public void changeQuantity(int quantity) {
        logger.info("Changing quantity {} of lineitem {} by {}", this.quantity, id, quantity);
        this.quantity += quantity;
    }

    /**
     * Returns the id of the lineitem
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the order id which this lineitem belongs to
     * @return
     */
    public int getOrderId() {
        return orderId;
    }
}
