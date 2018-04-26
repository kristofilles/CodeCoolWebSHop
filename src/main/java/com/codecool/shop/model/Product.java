package com.codecool.shop.model;

import com.codecool.shop.Globals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Currency;

/**
 * Product class that extends BaseModel.
 * Its responsible for creating the Products into the WebShop.
 */

public class Product extends BaseModel {

    /**
     * Product specific fields additionally to the Basemodel fields.
     */
    private static final Logger logger = LoggerFactory.getLogger(Product.class);
    private float defaultPrice;
    private Currency defaultCurrency;
    private ProductCategory productCategory;
    private Supplier supplier;
    private String image;


    /**
     * Constructor for creating a product with automatically generated id.
     * @param name name of the product
     * @param defaultPrice default price of the product
     * @param currencyString currency of the product's price
     * @param description description of the product
     * @param productCategory product category of product
     * @param supplier supplier of product
     * @param image image of product
     */
    public Product(String name, float defaultPrice, String currencyString, String description, ProductCategory productCategory, Supplier supplier, String image) {
        super(name, description);
        this.id = Globals.productDao.getAll().size();
        this.setPrice(defaultPrice, currencyString);
        this.setSupplier(supplier);
        this.setProductCategory(productCategory);
        this.setImage(image);
        logger.debug("Product created with name: {}, defaultPrice: {}, currencyString: {}," +
                " description: {}, product category: {}, supplier: {}, image: {}", name, defaultPrice,
                currencyString, description, productCategory.getName(), supplier.getName(), image);

    }

    /**
     * Constructor for creating a product with predetermined id
     * @param id predetermined id for product
     * @param name name of the product
     * @param defaultPrice default price of the product
     * @param currencyString currency of the product's price
     * @param description description of the product
     * @param productCategory product category of product
     * @param supplier supplier of product
     * @param image image of product
     */
    public Product(int id, String name, float defaultPrice, String currencyString, String description, ProductCategory productCategory, Supplier supplier, String image) {
        super(id, name, description);
        this.setPrice(defaultPrice, currencyString);
        this.setSupplier(supplier);
        this.setProductCategory(productCategory);
        this.image = image;
        logger.debug("Product created with id: {}, name: {}, defaultPrice: {}, currencyString: {}," +
                        " description: {}, product category: {}, supplier: {}, image: {}", id, name, defaultPrice,
                currencyString, description, productCategory.getName(), supplier.getName(), image);

    }

    /**
     * Getter method for field: image
     * @return image of the product
     */
    public String getImage() {
        return image;
    }

    /**
     * Setter method for field: image
     * @param image image
     */
    private void setImage(String image) {
        this.image = "/img/" + image;
    }

    /**
     * Getter method for field: defaultPrice
     * @return default price of product
     */
    public float getDefaultPrice() {
        return defaultPrice;
    }

    /**
     * Setter method for field: defaultPrice
     * @param defaultPrice default price
     */
    public void setDefaultPrice(float defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    /**
     * Getter method for field: defaultCurrency
     * @return currency of the products price
     */
    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    /**
     * Setter method for field: defaultCurrency
     * @param defaultCurrency currency of the products price
     */
    public void setDefaultCurrency(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    /**
     * Getter method for price
     * @return price with format: defaultPrice + defaultCurrency
     */
    public String getPrice() {
        return String.valueOf(this.defaultPrice) + " " + this.defaultCurrency.toString();
    }

    /**
     * Setter method for price
     * @param price price of product
     * @param currency currency of product
     */
    private void setPrice(float price, String currency) {
        this.defaultPrice = price;
        this.defaultCurrency = Currency.getInstance(currency);
    }

    /**
     * Getter method for field: productCategory
     * @return product category of product
     */
    public ProductCategory getProductCategory() {
        return productCategory;
    }

    /**
     * Setter method for field: productCategory
     * @param productCategory product category of product
     */
    private void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
        this.productCategory.addProduct(this);
    }

    /**
     * Getter method for field: supplier
     * @return supplier of the product
     */
    public Supplier getSupplier() {
        return supplier;
    }

    /**
     * Setter method for field: supplier
     * @param supplier supplier of the product
     */
    private void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        this.supplier.addProduct(this);
    }

    /**
     * Overriden toString method for toString operations
     * @return String with format: 'id, name, defaultPrice, defaultCurrency,
     *                              productCategory, supplier'.
     */
    @Override
    public String toString() {
        return String.format("id: %1$d, " +
                        "name: %2$s, " +
                        "defaultPrice: %3$f, " +
                        "defaultCurrency: %4$s, " +
                        "productCategory: %5$s, " +
                        "supplier: %6$s",
                this.id,
                this.name,
                this.defaultPrice,
                this.defaultCurrency.toString(),
                this.productCategory.getName(),
                this.supplier.getName());
    }
}
