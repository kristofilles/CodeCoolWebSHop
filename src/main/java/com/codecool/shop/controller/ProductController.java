package com.codecool.shop.controller;

import com.codecool.shop.Globals;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.Mem.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Mem.ProductDaoMem;
import com.codecool.shop.dao.implementation.Mem.SupplierDaoMem;
import com.codecool.shop.model.ShippingDetail;
import com.codecool.shop.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that is responsible for rendering the Product pages on the site.
 */

public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    /**
     * renderProducts method is responsible for rendering the view for the product pages.
     * @param req HTTP request
     * @param res HTTP response
     * @param catId Category id for filtered search
     * @param suppId Supplier id for filtered search
     * @param condition Determines whether the filtering should be based on category or supplier
     * @param isCategoryInPath Boolean that showcases whether the category is in the url
     * @return View with the rendered products
     */
    public static ModelAndView renderProducts(Request req, Response res, String catId, String suppId, String condition, Boolean isCategoryInPath) {
        ProductDao productDataStore = Globals.productDao;
        ProductCategoryDao productCategoryDataStore = Globals.productCategoryDao;
        SupplierDao supplierDataStore = Globals.supplierDao;

        int formattedCatId = Integer.parseInt(catId);
        int formattedSuppId = Integer.parseInt(suppId);

        Map params = new HashMap<>();
        params.put("path", isCategoryInPath);
        params.put("category", productCategoryDataStore.find(formattedCatId));
        params.put("categories", productCategoryDataStore.getAll());
        if (condition.equals("category")) {
            params.put("products", productDataStore.getBy(productCategoryDataStore.find(formattedCatId)));
        } else if (condition.equals("supplier")) {
            params.put("products", productDataStore.getBy(productCategoryDataStore.find(formattedCatId), supplierDataStore.find(formattedSuppId)));
        }
        params.put("suppliers", supplierDataStore.getAll());

        if(req.session().attribute("id") == null){
            params.put("itemQuantity", 0);
            params.put("loggedin", false);
        }else {
            params.put("itemQuantity", Order.getOpenInstance(req.session().attribute("id")).getItemsQuantity());
            params.put("loggedin", true);
        }

        logger.debug("Product page rendered for category {} and supplier {}", productCategoryDataStore.find(formattedCatId).getName(),
                supplierDataStore.find(formattedSuppId).getName());

        return new ModelAndView(params, "product/index");
    }


    /**
     * renderCheckout method is responsible for rendering the view for the Checkout page of the site.
     * @param request HTTP request
     * @param response HTTP response
     * @return View for the Checkout page.
     */
    public static ModelAndView renderCheckout(Request request, Response response) {
        List<ShippingDetail> shippingDetailsByUserId = Globals.shippingDao.getBy(request.session().attribute("id"));
        List<HashMap<String, String>> formattedDetails = new ArrayList<>();
        for (ShippingDetail shippingDetail: shippingDetailsByUserId){
            formattedDetails.add(new HashMap<String, String>(){{
                put("first_name", shippingDetail.getFirstName());
                put("last_name", shippingDetail.getLastName());
                put("email", shippingDetail.getEmail());
                put("zip_code", String.valueOf(shippingDetail.getZipCode()));
                put("city", shippingDetail.getCity());
                put("address", shippingDetail.getAddress());
            }}
            );
        }
        Map params = new HashMap<>();
        params.put("order", Order.getOpenInstance(request.session().attribute("id")));
        params.put("shipping", formattedDetails);
        return new ModelAndView(params, "product/checkout");
    }

}
