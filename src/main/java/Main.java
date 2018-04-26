import com.codecool.shop.Globals;
import com.codecool.shop.controller.CartController;
import com.codecool.shop.controller.LoginController;
import com.codecool.shop.controller.ProductController;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.Mem.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.Mem.ProductDaoMem;
import com.codecool.shop.dao.implementation.Mem.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.ShippingDetail;
import com.codecool.shop.model.Supplier;
import com.codecool.shop.order.EmailSender;
import com.codecool.shop.order.LineItem;
import com.codecool.shop.order.Order;
import com.codecool.shop.user.User;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.apache.commons.lang3.StringUtils;
import spark.Request;
import spark.Response;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class Main {

    public static void main(String[] args) {

        // default server settings
        exception( Exception.class, (e, req, res) -> e.printStackTrace() );
        staticFileLocation( "/public" );
        port( 8888 );

        // populate some data for the memory storage
        //populateData();

        before("/order/*", ((request, response) -> {
            if(request.session().attribute("id") == null){
                response.redirect("/user/login",302);
                halt("Unauthorized access");
            }
        }));
        before("/order/*/*", ((request, response) -> {
            if(request.session().attribute("id") == null){
                response.redirect("/user/login",302);
                halt("Unauthorized access");
            }
        }));

        before("/user/*", ((request, response) -> {
            if(request.session().attribute("id") != null){
                response.redirect("/index",302);
                halt("Already logged in");
            }
        }));

        // Always add generic routes to the end
        get( "/", (Request req, Response res) -> new ThymeleafTemplateEngine().render( ProductController.renderProducts( req, res, "1", "0", "category", false ) ) );
        // Equivalent with above
        get( "/index", (Request req, Response res) -> new ThymeleafTemplateEngine().render( ProductController.renderProducts( req, res, "1", "0", "category", false ) ) );

        get( "/categories/:catId", (request, response) -> new ThymeleafTemplateEngine().render( ProductController.renderProducts( request, response, request.params( ":catId" ), "0", "category", true ) ) );

        get( "/categories/:catId/suppliers/:suppId", (request, response) -> new ThymeleafTemplateEngine().render( ProductController.renderProducts( request, response, request.params( ":catId" ), request.params( ":suppId" ), "supplier", true ) ) );

        get( "/order/addtocart/:itemId", (Request req, Response res) -> {
            String itemId = req.params( ":itemId" );
            if (StringUtils.isNumeric( itemId )) {
                Order order = Order.getOpenInstance(req.session().attribute("id"));
                LineItem item = order.getLineItem(Integer.parseInt( itemId ));
                order.addToCart( item );
            }
            res.redirect( req.headers( "referer" ) );
            return null;
        } );

        get( "/order/checkout", (request, response) -> {
            if(Order.getOpenInstance(request.session().attribute("id")).getItemsGrossPrice() == 0) {
                response.redirect("/index");
                return null;
            }
            return new ThymeleafTemplateEngine().render( ProductController.renderCheckout( request, response ) );
        } );

        get("/order/mycart", (Request req, Response res) ->{
            return new ThymeleafTemplateEngine().render( CartController.renderCart( req, res) );
        });

        get("/user/login", (request, response) -> new ThymeleafTemplateEngine().render( LoginController.renderLogin( request, response, true ) ));

        get("/user/register", (request, response) -> {
            return new ThymeleafTemplateEngine().render( LoginController.renderRegister( request, response, true ) );
        } );

        get("/order/logout", (Request req, Response res) -> {
            req.session().removeAttribute("id");
            res.redirect("/index");
            return null;
        });

        get("/order/payment", (Request req, Response res) -> {
            if(Order.getOpenInstance(req.session().attribute("id")).getItemsGrossPrice() == 0) {
                res.redirect("/index");
                return null;
            }
            return new ThymeleafTemplateEngine().render(CartController.renderPayment( req, res));
        });

        post("/user/registering", (Request req, Response res) -> {
            User existingUser = Globals.userDao.find(req.queryParams("username"));
            if(existingUser == null){
                existingUser = new User(req.queryParams("username"), req.queryParams("password"));

                Globals.userDao.add(existingUser);
                req.session().attribute("id",existingUser.getId());
                res.redirect("/index");
            }else{
                res.redirect("/user/register?inuse=true");
            }
            return null;
                });

        post("/user/loggingin", (Request req, Response res) -> {
            User existingUser = Globals.userDao.find(req.queryParams("username"));
            if(existingUser != null){
                if(existingUser.checkPassword(req.queryParams("password"))) {
                    req.session().attribute("id", existingUser.getId());
                    res.redirect("/index");
                }else{
                    res.redirect("/user/login?incorrect=true");
                }
            }else{
                res.redirect("/user/login?incorrect=true");
            }
            return null;
        });

        get("/profile", (Request req, Response res) -> new ThymeleafTemplateEngine().render(LoginController.renderUserProfile( req, res )) );

        post("/profile/saveprofile", (Request req, Response res) -> {
            String firstname = req.queryParams("firstname");
            String lastname = req.queryParams( "lastname" );
            String email = req.queryParams( "email" );
            String zip = req.queryParams( "zip" );
            String city = req.queryParams( "city" );
            String street = req.queryParams( "street" );
            Globals.shippingDao.add( new ShippingDetail( firstname, lastname, email, Integer.parseInt( zip ), city, street, Globals.userDao.find((int) req.session().attribute( "id" ) ) ));
            res.redirect( req.headers( "referer" ) );
            return null;
        });

        post("/order/updatecart",(Request req, Response res) -> {
            // quantity in "quantity-X" form where X is an integer
            // key is productId, value is quantity
            Order order = Order.getOpenInstance(req.session().attribute("id"));
            Map<Integer, Integer> productQuantity = req.queryParams()
                    .stream()
                    .filter(t -> t.contains("quantity-"))
                    .map(t-> t.substring(t.indexOf("-")+1))
                    .collect(Collectors.toMap(Integer::parseInt, key-> Integer.parseInt(req.queryParams("quantity-" + key))));

            productQuantity.forEach((k, v) -> {
                if (v > 0) {
                    order.setLineItemQuantity(k, v);
                } else {
                    order.deleteLineItem(k);
                }
            });
            if(req.queryParams("action").equals("Update")){
                res.redirect(req.headers("referer"));
            }else{
                res.redirect("/order/checkout");
            }
            return null;

        });

        post("/order/charge", (Request req, Response res) -> {
            Order currentOrder = Order.getOpenInstance(req.session().attribute("id"));
            RequestOptions requestOptions = (new RequestOptions.RequestOptionsBuilder()).setApiKey("sk_test_K68nbSaJDqMpmzrbOvonRoiy").build();
            Map<String, Object> chargeMap = new HashMap<String, Object>();
            chargeMap.put("amount", Math.round(currentOrder.getItemsGrossPrice()));
            chargeMap.put("currency", "usd");
            chargeMap.put("source", req.queryParams("stripeToken"));
            try {
                Charge.create(chargeMap, requestOptions);
                currentOrder.pay();
                List<Order> orders = Globals.orderDao.getOrdersByUserId(req.session().attribute("id"));
                String emailTo = Globals.shippingDao.getEmailByOrderId(orders.get(orders.size() - 2), req.session().attribute("id"));
                System.out.println(emailTo);
                EmailSender sender = new EmailSender(emailTo);
                sender.sendMail();
                return new ThymeleafTemplateEngine().render(CartController.renderPaymentResult(req, res, true));
            } catch (StripeException e) {
                e.printStackTrace();
                return new ThymeleafTemplateEngine().render(CartController.renderPaymentResult(req, res, false));
            }

        });

        post("/order/checkout", (Request req, Response res) -> {
            String first_name = req.queryParams("first_name");
            String last_name = req.queryParams("last_name");
            String email = req.queryParams("email");
            int zip_code = Integer.valueOf(req.queryParams("zip_code"));
            String city = req.queryParams("city");
            String address = req.queryParams("address");

            ShippingDetail temp = new ShippingDetail(first_name, last_name, email,
                    zip_code, city, address, Globals.userDao.find((int)req.session().attribute("id")));
            int tempId = temp.checkDetailIfAlreadyExists();
            if (tempId == -1){
                Globals.shippingDao.add(temp);
                tempId = temp.getId();
            }
            Globals.orderDao.updateOrderShippingId(Order.getOpenInstance(req.session().attribute("id")).getId(), tempId);
            res.redirect("/order/payment");
            return null;
        });

        // Add this line to your project to enable the debug screen
        enableDebugScreen();
    }

    private static void populateData() {

        ProductDao productDataStore = Globals.productDao;
        ProductCategoryDao productCategoryDataStore = Globals.productCategoryDao;
        SupplierDao supplierDataStore = Globals.supplierDao;

        //setting up a new supplier
        Supplier kozvilagitas = new Supplier( "Dj Közwilágításh", "digital content creator" );
        supplierDataStore.add( kozvilagitas );
        Supplier totottcigi = new Supplier( "Dj Tötöttcigi", "digital content creator" );
        supplierDataStore.add( totottcigi );
        Supplier totalkar = new Supplier( "Dj Totálkár", "digital content creator" );
        supplierDataStore.add( totalkar );
        Supplier villanyboresz = new Supplier( "Dj Villanyborotva", "digital content creator" );
        supplierDataStore.add( villanyboresz );
        Supplier felmosonyel = new Supplier( "Dj Felmosónyél", "digital content creator" );
        supplierDataStore.add( felmosonyel );
        Supplier tizeskulcs = new Supplier( "Dj Tize$kulc$", "digital content creator" );
        supplierDataStore.add( tizeskulcs );
        Supplier wasbeton = new Supplier( "Dj wa$betoN", "digital content creator" );
        supplierDataStore.add( wasbeton );



        //setting up a new product category
        ProductCategory tshirt = new ProductCategory( "T-shirt", "Clothing", "Best clothing from ur best Dj-z." );
        productCategoryDataStore.add( tshirt );
        ProductCategory books = new ProductCategory( "Books", "Education", "Words on papers" );
        productCategoryDataStore.add( books );
        ProductCategory vinyl = new ProductCategory( "Vinyl Discs", "Music", "Pure eargasm" );
        productCategoryDataStore.add(vinyl);
        ProductCategory tools = new ProductCategory( "Tools", "Housechores", "Handy little tools" );
        productCategoryDataStore.add(tools);

        //setting up products and printing it
        productDataStore.add( new Product( "White Közwilágításh T-shirt", 49, "USD", "Premium quality T-shirt.", tshirt, kozvilagitas, "shirt_kozvilagitash.png" ) );
        productDataStore.add( new Product( "Black Közwilágításh T-shirt", 47, "USD", "Premium quality T-shirt.", tshirt, kozvilagitas, "shirt_kozvilagitash_2.png" ) );
        productDataStore.add( new Product( "Black Tőtöttcigi T-shirt", 89, "USD", "Premium quality T-shirt", tshirt, totottcigi, "shirt_totottcigi.png" ) );
        productDataStore.add( new Product( "Holy Lesson", 420, "USD", "A book that contains all the Holy informations u could ever need", books, totalkar, "book_totalkar.jpg" ) );
        productDataStore.add( new Product( "Evangélium of Megborítás", 1337, "USD", "A basic guideline for the art of megborítás", books, villanyboresz, "book_villanyborotva.png" ) );
        productDataStore.add( new Product( "Dj Felmosónyél", 6001, "USD", "Premium quality broom for messy jobs", tools, felmosonyel, "tool_felmoso.jpg" ) );
        productDataStore.add( new Product( "Dj Tizeskulcs set", 10, "USD", "Toolkit only for the handiest of men", tools, tizeskulcs, "tool_tizes.jpg" ) );
        productDataStore.add( new Product( "Dj Közwilagitash disc", 14000, "USD", "The disc that follows the price of Bitcoin", vinyl, kozvilagitas, "vinyl_kozvilagitash.png" ) );
        productDataStore.add( new Product( "Dj wa$betoN disc", 123, "USD", "Quality disc full of bangers", vinyl, wasbeton, "vinyl_wasbeton.png" ) );

    }


}
