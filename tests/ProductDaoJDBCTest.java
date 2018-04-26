import com.codecool.shop.dao.implementation.JDBC.ProductDaoJDBC;

public class ProductDaoJDBCTest extends ProductDaoTest<ProductDaoJDBC>{
    @Override
    protected ProductDaoJDBC createInstance() {
        return ProductDaoJDBC.getInstance();
    }
}
