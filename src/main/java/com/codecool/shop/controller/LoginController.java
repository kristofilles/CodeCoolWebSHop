package com.codecool.shop.controller;

import com.codecool.shop.Globals;
import com.codecool.shop.order.LineItem;
import com.codecool.shop.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Redirect;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class that is responsible for rendering the Login/Registration page on the site.
 */
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * renderLogin method is responsible for rendering the view for the login page.
     * @param request HTTP request
     * @param response HTTP response
     * @param isLogin Boolean that determines whether the login page should be displayed or not.
     * @return View for the Login page.
     */
    public static ModelAndView renderLogin(Request request, Response response, Boolean isLogin) {

        Map params = new HashMap<>();
        params.put( "login", isLogin );
        params.put( "incorrect", Boolean.parseBoolean( request.queryParams( "incorrect" ) ) ? true : false );
        logger.debug("Login page rendered!");

        return new ModelAndView( params, "product/login" );

    }

    /**
     * renderRegister method is responsible for rendering the view for the register page.
     * @param request HTTP request
     * @param response HTTP response
     * @param isRegister Boolean that determines whether the registration page should be displayed or not.
     * @return View for the Registration page.
     */
    public static ModelAndView renderRegister(Request request, Response response, Boolean isRegister) {
        Map params = new HashMap<>();
        params.put( "register", isRegister );
        params.put( "inuse", Boolean.parseBoolean( request.queryParams( "inuse" ) ) ? true : false );
        logger.debug("Registration page rendered!");

        return new ModelAndView( params, "product/login" );
    }

    public static ModelAndView renderUserProfile(Request request, Response response) {
        Map params = new HashMap<>();
        List<Order> orders = Globals.orderDao.getOrdersByUserId((request.session().attribute("id") ) );
        params.put( "user",  Globals.userDao.find( (int)(request.session().attribute("id") )) ) ;
        params.put("orders", orders);
        System.out.println((int)request.session().attribute( "id" ) );
        System.out.println(orders);

        return new ModelAndView( params, "product/profile" );
    }

}
