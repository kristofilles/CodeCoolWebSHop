package com.codecool.shop.dao.implementation.JDBC;

import com.codecool.shop.Globals;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Here you can find JDBC related queries for the products table.
 * <p>
 * <p>Queries what we are implemented for  the products table are: add,
 * find by id, remove by id and get all of the products or get by category/category and supplier.</p>
 */
public class ProductDaoJDBC implements ProductDao {

    private final static Logger logger = LoggerFactory.getLogger( ProductDaoJDBC.class );
    private static ProductDaoJDBC instance = null;
    /**
     * This is a ConnectionHandler type {@link ConnectionHandler} for establish connection.
     */
    private ConnectionHandler ch;

    private ProductDaoJDBC() {

    }

    public static ProductDaoJDBC getInstance() {
        if (instance == null) {
            instance = new ProductDaoJDBC();
        }
        return instance;
    }

    /**
     * Its supposed to add a new product to the products.
     *
     * @param product
     */
    @Override
    public void add(Product product) {
        logger.debug( "Inserting {} to the products table.", product.getName() );
        String query = "INSERT INTO products (id, name, image, default_price, currency_string, description," +
                " supplier_id, product_category_id) VALUES (?,?,?,?,?,?,?,?)";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( product.getId() ),
                product.getName(),
                product.getImage(),
                String.valueOf( product.getDefaultPrice() ),
                String.valueOf( product.getDefaultCurrency() ),
                product.getDescription(),
                String.valueOf( product.getSupplier().getId() ),
                String.valueOf( product.getProductCategory().getId() ) ) );

    }

    /**
     * Its supposed to find a specific product with the given id.
     *
     * @param id of the product
     * @return the desired product by id
     */
    @Override
    public Product find(int id) {
        logger.debug( "Selecting a product with the id: {}", id );
        String query = "SELECT * FROM products WHERE id = ?";
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( id ) ) );) {
            if (resultSet.next()) {
                logger.debug( "Successfully selected the item with id: {}", id );
                return new Product( id, resultSet.getString( "name" ),
                        resultSet.getFloat( "default_price" ),
                        resultSet.getString( "currency_string" ),
                        resultSet.getString( "description" ),
                        Globals.productCategoryDao.find( resultSet.getInt( "product_category_id" ) ),
                        Globals.supplierDao.find( resultSet.getInt( "supplier_id" ) ),
                        resultSet.getString( "image" ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        return null;
    }

    /**
     * Its supposed to remove a product with the given id.
     *
     * @param id of the product
     */
    @Override
    public void remove(int id) {
        logger.debug( "Deleting product with id: {}", id );
        String query = "DELETE FROM products WHERE id = ?";
        ch = new ConnectionHandler();
        ch.executeQuery( query, Arrays.asList( String.valueOf( id ) ) );
    }

    /**
     * Its supposed to return all of the products.
     *
     * @return the products
     */
    @Override
    public List<Product> getAll() {
        logger.debug( "Selecting all of the products from the products table." );
        String query = "SELECT * FROM products";
        List<Product> ProductResult = new ArrayList<>();
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, null );) {
            while (resultSet.next()) {
                ProductResult.add( new Product( resultSet.getInt( "id" ), resultSet.getString( "name" ),
                        resultSet.getFloat( "default_price" ),
                        resultSet.getString( "currency_string" ),
                        resultSet.getString( "description" ),
                        Globals.productCategoryDao.find( resultSet.getInt( "product_category_id" ) ),
                        Globals.supplierDao.find( resultSet.getInt( "supplier_id" ) ),
                        resultSet.getString( "image" ) ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        logger.debug( "Successfully selected the products." );
        return ProductResult;
    }

    /**
     * <p>Its supposed to select products from the products table with
     * the given category and supplier.</p>
     *
     * @param prodCat  product category
     * @param supplier supplier
     * @return products with specific category and supplier
     */
    @Override
    public List<Product> getBy(ProductCategory prodCat, Supplier supplier) {
        logger.debug( "Selecting products by category: {}, and supplier: {}", prodCat.getName(), supplier.getName() );
        String query = "SELECT * FROM products WHERE product_category_id = ? AND supplier_id = ?";
        List<Product> result = new ArrayList<>();
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( prodCat.getId() ), String.valueOf( supplier.getId() ) ) );) {
            while (resultSet.next()) {
                result.add( new Product( resultSet.getInt( "id" ), resultSet.getString( "name" ),
                        resultSet.getFloat( "default_price" ),
                        resultSet.getString( "currency_string" ),
                        resultSet.getString( "description" ),
                        Globals.productCategoryDao.find( resultSet.getInt( "product_category_id" ) ),
                        Globals.supplierDao.find( resultSet.getInt( "supplier_id" ) ),
                        resultSet.getString( "image" ) ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        logger.debug( "Products successfully selected." );
        return result;
    }

    /**
     * Its supposed to select the products with the given product category.
     *
     * @param productCategory product category
     * @return products with the given category
     */
    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        logger.debug( "Selecting the products with product category: {}", productCategory.getName() );
        String query = "SELECT * FROM products WHERE product_category_id = ?";
        List<Product> result = new ArrayList<>();
        try (ConnectionHandler ch = new ConnectionHandler();
             ResultSet resultSet = ch.executeQuery( query, Arrays.asList( String.valueOf( productCategory.getId() ) ) );) {
            while (resultSet.next()) {
                result.add( new Product( resultSet.getInt( "id" ), resultSet.getString( "name" ),
                        resultSet.getFloat( "default_price" ),
                        resultSet.getString( "currency_string" ),
                        resultSet.getString( "description" ),
                        Globals.productCategoryDao.find( resultSet.getInt( "product_category_id" ) ),
                        Globals.supplierDao.find( resultSet.getInt( "supplier_id" ) ),
                        resultSet.getString( "image" ) ) );
            }
        } catch (Exception e) {
            logger.warn( "Connection failed. StackTrace: {}", e.getMessage() );
        }
        logger.debug( "Successfully selected the products." );
        return result;
    }
}
