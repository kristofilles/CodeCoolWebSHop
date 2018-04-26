package com.codecool.shop.model;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.lang.reflect.Field;


/**
 * This class serves as a template for the following subclasses: Product, ProductCategory, Supplier
 * and provides base constructors and methods for them.
 */

public class BaseModel {

    /**
     * The fields initialized here are common amongst all the subclasses
     */
    private static final Logger logger = LoggerFactory.getLogger(BaseModel.class);
    protected int id;
    String name;
    String description;

    /**
     * Constructor if only name is known
     * @param name name of the subclass object
     */
    BaseModel(String name) {
        this.name = name;
        logger.debug("BaseModel Object created with name: {}", name);
    }

    /**
     * Constructor for known name and description
     * @param name name of the subclass object
     * @param description description of the subclass object
     */
    BaseModel(String name, String description) {
        this.name = name;
        this.description = description;
        logger.debug("BaseModel Object created with name: {} and description: {}", name, description);
    }

    /**
     * Constructor for predetermined id cases
     * @param id desired id for the subclass object
     * @param name name of the subclass object
     */
    BaseModel(int id, String name) {
        this.id = id;
        this.name = name;
        logger.debug("BaseModel Object created with id: {} and name: {}", id, name);
    }

    /**
     * Constructor for known fields: id, name, description
     * @param id desired id for the subclass object
     * @param name name of the subclass object
     * @param description description of the subclass object
     */
    BaseModel(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        logger.debug("BaseModel Object created with id: {} , name: {} and description: {}", id, name, description);
    }

    /**
     * Getter method for field: id
     * @return id of subclass object
     */
    public int getId() {
        return id;
    }

    /**
     * Setter method for field: id
     * @param id id to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method for field: name
     * @return name of the subclass object
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for field: name
     * @param name name of the subclass object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for field: description
     * @return description of subclass object
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for field: description
     * @param description description to be set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Overriden toString method for toString operations
     * @return String with format: 'field name: field value, field name: field value'...
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(this);
                if (value != null) {
                    sb.append(field.getName() + ":" + value + ",");
                }
            } catch (IllegalAccessException e) {

            }
        }
        return sb.toString();
    }

}
