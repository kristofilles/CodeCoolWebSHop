package com.codecool.shop.order;

import com.codecool.shop.Globals;
import com.codecool.shop.dao.implementation.Mem.LineItemDaoMem;
import com.codecool.shop.dao.implementation.Mem.OrderDaoMem;
import com.codecool.shop.order.orderProcesses.AddToCart;
import com.codecool.shop.order.orderProcesses.Pay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class belongs to an user and holds {@link com.codecool.shop.model.Product}s in the form of {@link LineItem}s.
 * Its instance's purpose is to hold all necessary information regarding a given order from adding to cart until delivery and after.
 */
public class Order {

    /**
     * Logger class instance
     */
    private Logger logger = LoggerFactory.getLogger(Order.class);
    /**
     * The id of an order which is unique
     */
    private final int id;
    /**
     * The user's id which the given order belongs to
     */
    private final int userId;
    /**
     * The status of the order held in an enum file
     */
    private OrderStatus status;

    /**
     * The constructor for this class for first building objects
     *
     * <p>It differs from the other constructor in generating its own unique id and status</p>
     * @param userId Gets the user's id as a parameter (e.g.: session based id)
     */
    private Order(int userId){
        id = Globals.orderDao.getAll().size();
        this.userId = userId;
        status = OrderStatus.CART;
    }

    /**
     * The second constructor for rebuilding the object based on database data
     * @param id
     * @param userId
     * @param status
     */
    public Order(int id, int userId, String status){
        this.id = id;
        this.userId = userId;
        this.status = OrderStatus.valueOf(status);
    }

    /**
     * A factory method for getting the last open order
     *
     * <p>If the user doesn't have any order, or only has closed orders (e.g.: paid, shipped),
     * returns a new Order object, else it returns the available one</p>
     * @param userId
     * @return
     */
    public static Order getOpenInstance(int userId){
        Order order = Globals.orderDao.getOpenByUserId(userId);
        if(order == null) {
            order = new Order(userId);
            Globals.orderDao.add(order);
        }
        return order;
    }

    /**
     * Returns the unique id of the given order
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the unique id of the user belonging to the given order
     * @return
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Returns the state of the order
     * @return
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the order. Used in order processes
     * @return
     */
    public void setStatus(OrderStatus status) {
        logger.info("Status of order {} set from {} to {}", id, this.status, status);
        this.status = status;
    }

    /**
     * Updates the given lineitem of the order with a quantity addition (or substraction)
     * @param item
     * @param quantityChange
     */
    public void updateLineItem(LineItem item, int quantityChange){

        Globals.lineItemDao.changeQuantity(item.getId(), quantityChange);
    }

    /**
     * Sets the quantity of the given lineitem of the order with a new quantity
     * @param productId
     * @param quantity
     */
    public void setLineItemQuantity(int productId, int quantity){
        Globals.lineItemDao.setQuantity(getLineItem(productId).getId(), quantity);
    }

    /**
     * Deletes a given lineitem connected to the order based on product id
     * @param productId
     */
    public void deleteLineItem(int productId){
        Globals.lineItemDao.remove(Globals.lineItemDao.getAll().stream().filter(t -> t.getProductId() == productId && t.getOrderId() == this.id).findFirst().orElse(null));
    }

    /**
     * Returns the sum of all product quantities in all lineitems of the given order
     * @return
     */
    public int getItemsQuantity(){
        return Globals.lineItemDao.getAllByOrderId(this.id).stream().mapToInt(LineItem::getQuantity).sum();
    }

    /**
     * Returns the gross price of all lineitems of the given order
     * @return
     */
    public double getItemsGrossPrice(){
        return Globals.lineItemDao.getAllByOrderId(this.id).stream().mapToDouble(t -> t.getProduct().getDefaultPrice() * t.getQuantity()).sum();
    }

    /**
     * Returns the gross price of all lineitems of the given order with USD suffix
     * @return
     */
    public String getItemsGrossPriceWithCurrency(){
        return String.valueOf(getItemsGrossPrice()) + " USD";
    }

    /**
     * Returns the lineitem connected to this order with the given productid
     * @param productId
     * @return
     */
    public LineItem getLineItem(int productId){
        LineItem currentItem = Globals.lineItemDao.getAllByOrderId(this.id).stream().filter(t -> t.getProductId() == productId).findFirst().orElse(new LineItem(productId, this.id));
        if(currentItem.getQuantity() == 0){
            Globals.lineItemDao.add(currentItem);
        }
        return currentItem;
    }

    /**
     * Adds the given lineitem to cart or updates it if already exists
     * @param item
     */
    public void addToCart(LineItem item){
        AddToCart addToCart = new AddToCart(this, item);
        addToCart.doTheSteps();
    }

    public void pay(){
        Pay pay = new Pay(this);
        pay.doTheSteps();
    }
}
