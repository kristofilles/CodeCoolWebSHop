package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Here you can find JDBC related queries for the order table.
 * <p>
 * <p>Queries what we are implemented for  the order table are: add,
 * find by id, remove by id and get all of the orders and
 * the orders what checked out by user or in his/her cart.</p>
 */
public class OrderDaoJDBC implements OrderDao {

    private static final Logger logger = LoggerFactory.getLogger( OrderDaoJDBC.class );
    private static OrderDaoJDBC instance = null;
    /**
     * This is a ConnectionHandler type {@link ConnectionHandler} for establish connection.
     */
    private ConnectionHandler ch;


    private OrderDaoJDBC() {

    }

    public static OrderDaoJDBC getInstance() {
        if (instance == null) {
            instance = new OrderDaoJDBC();
        }
        return instance;
    }

    /**
     * Its supposed to add an order to the orders table.
     *
     * @param order
     */
    @Override
    public void add(Order order) {
        logger.debug( "Adding {} in to orders", order.toString() );
        String query = "INSERT INTO orders (id, user_id, status) VALUES (?,?,?)";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( order.getId() ), String.valueOf( order.getUserId() ), order.getStatus().toString() ) );
    }

    /**
     * Its supposed to find a specific order with the given id.
     *
     * @param id of the order
     * @return the desired order
     */
    @Override
    public Order find(int id) {
        logger.debug( "Selecting the order with id: {}", id );
        String query = "SELECT * FROM orders WHERE id = ?";
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( id ) ) );) {
            if (resultSet.next()) {
                logger.debug( "Successfully selected the order." );
                return new Order( id, resultSet.getInt( "user_id" ), resultSet.getString( "status" ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        return null;
    }

    /**
     * Its supposed to remove an order from the orders table.
     *
     * @param id of the order
     */
    @Override
    public void remove(int id) {
        logger.debug( "Deleting the order with id: {}", id );
        String query = "DELETE FROM orders WHERE id = ?";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( id ) ) );
    }

    /**
     * Its supposed to return all of the orders from the orders table.
     *
     * @return all of the orders
     */
    @Override
    public List<Order> getAll() {
        logger.debug( "Selecting all of the orders." );
        String query = "SELECT * FROM orders";
        List<Order> OrderResult = new ArrayList<>();
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, null );) {
            while (resultSet.next()) {
                OrderResult.add( new Order( resultSet.getInt( "id" ), resultSet.getInt( "user_id" ), resultSet.getString( "status" ) ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        logger.debug( "Successfully selected the orders." );
        return OrderResult;
    }

    /**
     * <p>Its supposed to select the orders by user id,
     * which is already in his/her cart or checked out.</p>
     *
     * @param userId its the user_id
     * @return the orders by user id which status CART or CHECKED_OUT.
     */
    @Override
    public Order getOpenByUserId(int userId) {
        logger.debug( "Selecting the orders which is CART or CHECKED_OUT with the given user id: {}", userId );
        String query = "SELECT * FROM orders WHERE user_id = ? AND status = 'CART'";
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( userId ) ) );) {
            if (resultSet.next()) {
                logger.debug( "Successfully selected the order" );
                return new Order( resultSet.getInt( "id" ), resultSet.getInt( "user_id" ), resultSet.getString( "status" ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        return null;
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        logger.debug( "Selecting the orders with the given user id: {}", userId );
        String query = "SELECT * FROM orders WHERE user_id = ?";
        List<Order> OrderResult = new ArrayList<>();
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( userId ) ) )) {
            while (resultSet.next()) {
                logger.debug( "Successfully selected the order" );
                OrderResult.add( new Order( resultSet.getInt( "id" ), resultSet.getInt( "user_id" ), resultSet.getString( "status" ) ) ) ;
            }
        } catch (Exception e) {
            logger.warn( "Connection failed.", e.getMessage() );
        }
        return OrderResult;
    }


    @Override
    public void updateOrderStatus(Order order) {
        logger.debug( "Updating the order with id: {}", order.getId() );
        String query = "UPDATE orders SET status = ? WHERE id = ?";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList(order.getStatus().toString(), String.valueOf(order.getId())));
    }

    @Override
    public void updateOrderShippingId(int orderId, int shippingId) {
        String query = "UPDATE orders SET shipping_detail_id = ? WHERE id = ?";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList(String.valueOf(shippingId), String.valueOf(orderId)));
    }

    @Override
    public int getOrdersShippingId(Order order){
        String query = "SELECT shipping_detail_id FROM orders WHERE id = ?";
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( order.getId() ) ) )) {
           if (resultSet.next()) {
                return resultSet.getInt("shipping_detail_id");}
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        return -1;
    }
}
