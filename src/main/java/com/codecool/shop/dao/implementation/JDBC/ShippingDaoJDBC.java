package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.Globals;
import com.codecool.shop.dao.ShippingDao;
import com.codecool.shop.model.ShippingDetail;
import com.codecool.shop.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShippingDaoJDBC implements ShippingDao {

    private static ShippingDaoJDBC instance = null;
    private ConnectionHandler ch;
    private final static Logger logger = LoggerFactory.getLogger( ProductDaoJDBC.class );


    private ShippingDaoJDBC(){

    }

    public static ShippingDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ShippingDaoJDBC();
        }
        return instance;
    }

    @Override
    public void add(ShippingDetail shippingDetail) {
        String query = "INSERT INTO shipping_details (id, first_name, last_name, email, zip_code, city, address, user_id)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( shippingDetail.getId() ), shippingDetail.getFirstName(),
                shippingDetail.getLastName(), shippingDetail.getEmail(), String.valueOf(shippingDetail.getZipCode()),
                shippingDetail.getCity(), shippingDetail.getAddress(), String.valueOf(shippingDetail.getUserId()) ) );
    }

    @Override
    public ShippingDetail find(int id) {
        logger.debug( "Select a shipping detail with the given id: {}", id );
        String query = "SELECT * FROM shipping_details WHERE id = ?";
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( id ) ) );) {
            if (resultSet.next()) {
                logger.debug( "Found the shipping detail" );
                return new ShippingDetail(  id,
                                            resultSet.getString( "first_name" ),
                                            resultSet.getString( "last_name" ),
                                            resultSet.getString( "email" ),
                                            resultSet.getInt( "zip_code" ),
                                            resultSet.getString( "city"),
                                            resultSet.getString( "address" ),
                                            Globals.userDao.find(resultSet.getInt("user_id")));
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        return null;
    }

    @Override
    public void remove(int id) {
        logger.debug( "Deleting shipping detail with id: {}", id );
        String query = "DELETE FROM shipping_details WHERE id = ?";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( id ) ) );
    }

    @Override
    public List<ShippingDetail> getAll() {
        logger.debug( "Selecting all from the shipping details table." );
        String query = "SELECT * FROM shipping_details";
        List<ShippingDetail> shippingResult = new ArrayList<>();
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, null )) {
            while (resultSet.next()) {
                shippingResult.add( new ShippingDetail(
                        resultSet.getInt( "id" ),
                        resultSet.getString( "first_name" ),
                        resultSet.getString( "last_name" ),
                        resultSet.getString( "email" ),
                        resultSet.getInt( "zip_code" ),
                        resultSet.getString( "city"),
                        resultSet.getString( "address" ),
                        Globals.userDao.find(resultSet.getInt("user_id"))));
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        logger.debug( "Return all of the shipping details." );
        return shippingResult;
    }

    @Override
    public List<ShippingDetail> getBy(int userId) {
        logger.debug( "Selecting the shipping details with the given user id: {}", userId );
        String query = "SELECT * FROM shipping_details WHERE user_id = ?";
        List<ShippingDetail> shippingResult = new ArrayList<>();
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( userId ) ) )) {
            while (resultSet.next()) {
                shippingResult.add( new ShippingDetail(
                        resultSet.getInt( "id" ),
                        resultSet.getString( "first_name" ),
                        resultSet.getString( "last_name" ),
                        resultSet.getString( "email" ),
                        resultSet.getInt( "zip_code" ),
                        resultSet.getString( "city"),
                        resultSet.getString( "address" ),
                        Globals.userDao.find(resultSet.getInt("user_id"))));
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        return shippingResult;
    }

    @Override
    public String getEmailByOrderId(Order order, int userId){
        String query = "SELECT DISTINCT email FROM shipping_details JOIN orders ON (shipping_details.user_id = orders.user_id) " +
                "WHERE shipping_details.id = ? AND shipping_details.user_id = ?;";
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf(Globals.orderDao.getOrdersShippingId(order)), String.valueOf(userId)) )) {
           if (resultSet.next()) {
                    return resultSet.getString("email");
                }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        return null;
    }
}
