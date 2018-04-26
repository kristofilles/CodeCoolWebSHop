package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Here you can find JDBC related queries for the supplier table.
 * <p>
 * <p>Queries what we are implemented for  the supplier table are: add,
 * find by id, remove by id and get all of the suppliers.</p>
 */
public class SupplierDaoJDBC implements SupplierDao {

    private static final Logger logger = LoggerFactory.getLogger( SupplierDaoJDBC.class );
    private static SupplierDaoJDBC instance = null;
    /**
     * This is a ConnectionHandler type {@link ConnectionHandler} for establish connection.
     */
    private ConnectionHandler ch;

    private SupplierDaoJDBC() {
    }

    public static SupplierDaoJDBC getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJDBC();
        }
        return instance;
    }

    /**
     * Add a supplier to the suppler table with the given name and description.
     *
     * @param supplier Supplier type
     */
    @Override
    public void add(Supplier supplier) {
        logger.debug( "{} adding to the supplier table", supplier.getName() );
        String query = "INSERT INTO suppliers (id, name, description) VALUES (?, ?, ?)";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( supplier.getId() ), supplier.getName(), supplier.getDescription() ) );
    }

    /**
     * Find a supplier with a given id, this method used for sorting the products by a specific supplier.
     *
     * @param id for find a supplier
     * @return new Supplier(id, name, description) if the query find the given id
     */
    @Override
    public Supplier find(int id) {
        logger.debug( "Select a supplier with the given id: {}", id );
        String query = "SELECT * FROM suppliers WHERE id = ?";
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( id ) ) )) {
            if (resultSet.next()) {
                logger.debug( "Found the supplier with the given id" );
                return new Supplier( id, resultSet.getString( "name" ), resultSet.getString( "description" ) );
            }
        } catch (Exception e) {
            logger.error( "Connection failed. Stacktrace: {}", e.getMessage() );
        }
        return null;
    }

    /**
     * Remove a supplier from the supplier table with the given id.
     *
     * @param id for find and remove a supplier from the table
     */
    @Override
    public void remove(int id) {
        logger.debug( "Remove a specific item from the supplier table with the given id: {}", id );
        String query = "DELETE FROM suppliers WHERE id = ?";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( id ) ) );
    }

    /**
     * It return all of the suppliers from the supplier table.
     *
     * @return Supplier type List. It returns all of the suppliers from the table
     */
    @Override
    public List<Supplier> getAll() {
        logger.debug( "Select all suppliers from the supplier table" );
        String query = "SELECT * FROM suppliers";
        List<Supplier> supplierResult = new ArrayList<>();
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, null );) {
            while (resultSet.next()) {
                supplierResult.add( new Supplier( resultSet.getInt( "id" ), resultSet.getString( "name" ), resultSet.getString( "description" ) ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. Stack trace: {}", e.getMessage() );
            e.printStackTrace();
        }
        logger.debug( "Return all suppliers from the supplier table" );
        return supplierResult;
    }
}
