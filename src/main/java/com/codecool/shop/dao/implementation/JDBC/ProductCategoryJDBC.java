package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Here you can find JDBC related queries for the ProductCategory table.
 * <p>
 * <p>Queries what we are implemented for  the ProductCategory table are: add,
 * find by id, remove by id and get all of the categories.</p>
 */
public class ProductCategoryJDBC implements ProductCategoryDao {

    private static final Logger logger = LoggerFactory.getLogger( ProductCategoryJDBC.class );
    private static ProductCategoryJDBC instance = null;
    /**
     * This is a ConnectionHandler type {@link ConnectionHandler} for establish connection.
     */
    private ConnectionHandler ch;

    private ProductCategoryJDBC() {
    }

    public static ProductCategoryJDBC getInstance() {
        if (instance == null) {
            instance = new ProductCategoryJDBC();
        }
        return instance;
    }

    /**
     * Add a new category for the product_categories table.
     *
     * @param category Its a new ProductCategory for add.
     */
    @Override
    public void add(ProductCategory category) {
        logger.debug( "Adding {} for the product_categories table", category.getName() );
        String query = "INSERT INTO product_categories (id, name, department, description) VALUES (?, ?, ?, ?)";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( category.getId() ), category.getName(), category.getDepartment(), category.getDescription() ) );
    }

    /**
     * Find a specific product category from the product_categories table with a given id.
     *
     * @param id for a product category
     * @return A specific category if its find the id
     */
    @Override
    public ProductCategory find(int id) {
        logger.debug( "Select a product category with the given id: {}", id );
        String query = "SELECT * FROM product_categories WHERE id = ?";
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( id ) ) );) {
            if (resultSet.next()) {
                logger.debug( "Found the product category" );
                return new ProductCategory( id, resultSet.getString( "name" ), resultSet.getString( "department" ), resultSet.getString( "description" ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        return null;
    }

    /**
     * It removes a product category from the product_categories table with the given id.
     *
     * @param id for a product category
     */
    @Override
    public void remove(int id) {
        logger.debug( "Deleting a category from the product_categories table with the id: {}", id );
        String query = "DELETE FROM product_categories WHERE id = ?";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( id ) ) );
    }

    /**
     * Return all of the categories from the product_categories table
     *
     * @return ProductCategory type List
     */
    @Override
    public List<ProductCategory> getAll() {
        logger.debug( "Selecting all from the product_categories table." );
        String query = "SELECT * FROM product_categories";
        List<ProductCategory> productCategoryResult = new ArrayList<>();
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, null );) {
            while (resultSet.next()) {
                productCategoryResult.add( new ProductCategory( resultSet.getInt( "id" ), resultSet.getString( "name" ), resultSet.getString( "department" ), resultSet.getString( "description" ) ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        logger.debug( "Return all of the product category." );
        return productCategoryResult;
    }
}
