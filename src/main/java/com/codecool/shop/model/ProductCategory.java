package com.codecool.shop.model;

import com.codecool.shop.Globals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.util.ArrayList;

/**
 * Class for the ProductCategory objects, extends BaseModel
 */

public class ProductCategory extends BaseModel {

    /**
     * Product category specific fields additionally to the Basemodel fields.
     */
    private static final Logger logger = LoggerFactory.getLogger(ProductCategory.class);
    private String department;
    private ArrayList<Product> products;

    /**
     * Constructor for creating a product category with automatically generated id.
     * @param name name of the product category
     * @param department department of the product category
     * @param description description of the product category
     */
    public ProductCategory(String name, String department, String description) {
        super(name);
        this.id = Globals.productCategoryDao.getAll().size();
        this.department = department;
        this.description = description;
        this.products = new ArrayList<>();
        logger.debug("Product Category created with name: {}, department: {}, description: {}", name,
                department, description);
    }

    /**
     * Constructor for creating a product category with predetermined id
     * @param id predetermined id for product category
     * @param name of the product category
     * @param department department of the product category
     * @param description description of the product category
     */
    public ProductCategory(int id, String name, String department, String description) {
        super(id, name);
        this.department = department;
        this.description = description;
        this.products = new ArrayList<>();
        logger.debug("Product Category created with id: {}, name: {}, department: {}, description: {}", id, name,
                department, description);
    }

    /**
     * Getter method for field: department
     * @return department of product category
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Setter method for field: department
     * @param department department of product category
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Getter method for field: products
     * @return Arraylist containing the Product objects in a certain category
     */
    public ArrayList getProducts() {
        return this.products;
    }

    /**
     * Setter method for field: products
     * @param products Arraylist of products to be included into the category
     */
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    /**
     * Method for adding a product to the current products in a certain category
     * @param product Product object to be added
     */
    public void addProduct(Product product) {
        this.products.add(product);
        logger.debug("Product {} added to category {}! ",product.getName(), this.getName());
    }

    /**
     * toString method for toString operations
     * @return String with format: id, name, department, description
     */
    public String toString() {
        return String.format(
                "id: %1$d," +
                        "name: %2$s, " +
                        "department: %3$s, " +
                        "description: %4$s",
                this.id,
                this.name,
                this.department,
                this.description);
    }
}