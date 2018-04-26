package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.Globals;
import com.codecool.shop.dao.LineItemDao;
import com.codecool.shop.order.LineItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <p>This class is supposed to handle the line items in the database.
 * We implemented the add, find by id, change quantity, set quantity,
 * remove, get all by order id and get all line items query.</p>
 */
public class LineItemDaoJDBC implements LineItemDao {

    /**
     * This is a ConnectionHandler type {@link ConnectionHandler} for establish connection.
     */
    private static final Logger logger = LoggerFactory.getLogger( LineItemDaoJDBC.class );
    private static LineItemDaoJDBC instance = null;
    private ConnectionHandler ch;

    /**
     * A private Constructor prevents any other class from instantiating.
     */
    private LineItemDaoJDBC() {
    }

    public static LineItemDaoJDBC getInstance() {
        if (instance == null) {
            instance = new LineItemDaoJDBC();
        }
        return instance;
    }

    /**
     * Its supposed to add a line item to the line_item table.
     *
     * @param lineitem
     */
    @Override
    public void add(LineItem lineitem) {
        logger.debug( "{} adding to the line_item table", lineitem.toString() );
        String query = "INSERT INTO line_item (id, product_id, quantity, orders_id) VALUES (?,?,?,?)";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( lineitem.getId() ), String.valueOf( lineitem.getProductId() ), String.valueOf( lineitem.getQuantity() ), String.valueOf( lineitem.getOrderId() ) ) );
    }

    /**
     * Its supposed to find a line item with the given id from the line_item table.
     *
     * @param id for search a line item
     * @return a line item with the given id
     */
    @Override
    public LineItem find(int id) {
        logger.debug( "Selecting a line item with the id: {}", id );
        String query = "SELECT * FROM line_item WHERE id = ?";
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( id ) ) );) {
            if (resultSet.next()) {
                return new LineItem( id, Globals.productDao.find( resultSet.getInt( "product_id" ) ), resultSet.getInt( "product_id" ), resultSet.getInt( "quantity" ), resultSet.getInt( "orders_id" ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        return null;
    }

    /**
     * Its supposed to change the quantity of the selected line item.
     *
     * @param id             of the line item
     * @param quantityChange number for changing the quantity
     */
    @Override
    public void changeQuantity(int id, int quantityChange) {
        logger.debug( "Changing the quantity by {} of the line item with id: {}", quantityChange, id );
        String query = "UPDATE line_item SET quantity = quantity + ? WHERE id = ?";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( quantityChange ), String.valueOf( id ) ) );
    }

    /**
     * Its supposed to set the selected line item to the given quantity.
     *
     * @param id       of the line item
     * @param quantity number of the quantity for setting it
     */
    @Override
    public void setQuantity(int id, int quantity) {
        logger.debug( "Updating the line item id: {} -s quantity to {}", id, quantity );
        String query = "UPDATE line_item SET quantity = ? WHERE id = ?";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( quantity ), String.valueOf( id ) ) );
    }

    /**
     * Its supposed to remove a line item from the line_item table
     *
     * @param item to remove
     */
    @Override
    public void remove(LineItem item) {
        logger.debug( "Deleting the line item: {}", item.toString() );
        String query = "DELETE FROM line_item WHERE id = ?";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( item.getId() ) ) );
    }

    /**
     * <p>Its supposed to select all of the line items by the given order id
     * from the line_item table.</p>
     *
     * @param orderId
     * @return all line item by the given order id
     */
    @Override
    public List<LineItem> getAllByOrderId(int orderId) {
        logger.debug( "Selecting the line items with the order id: {}", orderId );
        String query = "SELECT * FROM line_item WHERE orders_id = ?";
        List<LineItem> lineItemResult = new ArrayList<>();
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( orderId ) ) );) {
            while (resultSet.next()) {
                lineItemResult.add( new LineItem( resultSet.getInt( "id" ), Globals.productDao.find( resultSet.getInt( "product_id" ) ), resultSet.getInt( "product_id" ), resultSet.getInt( "quantity" ), resultSet.getInt( "orders_id" ) ) );
            }
            return lineItemResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug( "Successfully selected the line items with the order id" );
        return lineItemResult;
    }

    /**
     * Its supposed to select all of the line items from the line_item table.
     *
     * @return all line item
     */
    @Override
    public List<LineItem> getAll() {
        logger.debug( "Getting the line items from the database" );
        String query = "SELECT * FROM line_item";
        List<LineItem> lineItemResult = new ArrayList<>();
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, null )) {
            while (resultSet.next()) {
                lineItemResult.add( new LineItem( resultSet.getInt( "id" ), Globals.productDao.find( resultSet.getInt( "product_id" ) ), resultSet.getInt( "product_id" ), resultSet.getInt( "quantity" ), resultSet.getInt( "orders_id" ) ) );
            }
            return lineItemResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug( "Successfully selected all of the line items." );
        return lineItemResult;
    }
}
