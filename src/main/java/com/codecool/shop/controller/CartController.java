package com.codecool.shop.controller;

import com.codecool.shop.Globals;
import com.codecool.shop.order.LineItem;
import com.codecool.shop.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.jws.WebParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that is responsible for rendering the Cart page on the site.
 */
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    /**
     * RenderCart method is responsible for getting the products for the given user,
     * the gross price of the products in the cart
     * and passing the information to the view that is being returned by the method.
     * @param req HTTP request
     * @param res HTTP response
     * @return View for cart
     */
    public static ModelAndView renderCart(Request req, Response res) {
        Map params = new HashMap<>();
        List<LineItem> orderLineItems = Globals.lineItemDao.getAllByOrderId(Order.getOpenInstance(req.session().attribute("id")).getId());
        params.put("orderLineItems", orderLineItems);
        params.put("grossPrice", Order.getOpenInstance(req.session().attribute("id")).getItemsGrossPriceWithCurrency());
        logger.debug("Cart for current user loaded with {} items and a gross price of {}", orderLineItems.size(),
                Order.getOpenInstance(req.session().attribute("id")).getItemsGrossPriceWithCurrency());

        return new ModelAndView(params, "product/cart");
    }

    public static ModelAndView renderPayment(Request req, Response res){
        Map params = new HashMap<>();
        params.put("itemQuantity", Order.getOpenInstance(req.session().attribute("id")).getItemsQuantity());
        return new ModelAndView(params, "product/pay");
    }

    public static ModelAndView renderPaymentResult(Request req, Response res, Boolean success){
        Map params = new HashMap<>();
        params.put("success", success);
        params.put("itemQuantity", Order.getOpenInstance(req.session().attribute("id")).getItemsQuantity());
        return new ModelAndView(params, "product/paymentResult");
    }
}
