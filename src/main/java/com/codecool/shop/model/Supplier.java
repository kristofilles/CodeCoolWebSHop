package com.codecool.shop.model;

import com.codecool.shop.Globals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Class for the Supplier objects, extends BaseModel
 */

public class Supplier extends BaseModel {
    /**
     * Product category specific fields additionally to the Basemodel fields.
     */
    private static final Logger logger = LoggerFactory.getLogger(Supplier.class);
    private ArrayList<Product> products;

    /**
     * Constructor for creating a supplier with automatically generated id.
     * @param name name of the supplier
     * @param description description of the supplier
     */
    public Supplier(String name, String description) {
        super(name, description);
        this.id = Globals.supplierDao.getAll().size();
        this.products = new ArrayList<>();
        logger.debug("Supplier created with name: {}, description: {}", name, description);
    }

    /**
     * Constructor for creating a supplier with a predetermined id.
     * @param id predetermined id of supplier
     * @param name name of the supplier
     * @param description description of the supplier
     */
    public Supplier(int id, String name, String description) {
        super(id, name, description);
        this.products = new ArrayList<>();
        logger.debug("Supplier created with id: {}, name: {}, description: {}", id, name, description);
    }

    /**
     * Getter method for field: products
     * @return Arraylist of product objects with a certain supplier
     */
    public ArrayList getProducts() {
        return this.products;
    }

    /**
     * Setter method for field: products
     * @param products Arraylist of product objects to be added to a certain supplier
     */
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    /**
     * Method for adding a product to the existing products from a certain supplier
     * @param product product object to be added
     */
    public void addProduct(Product product) {
        this.products.add(product);
        logger.debug("Product {} added to supplier {}!", product.getName(), this.getName());
    }

    /**
     * toString method for toString operations
     * @return String with format: id, name, description
     */
    public String toString() {
        return String.format("id: %1$d, " +
                        "name: %2$s, " +
                        "description: %3$s",
                this.id,
                this.name,
                this.description
        );
    }
}