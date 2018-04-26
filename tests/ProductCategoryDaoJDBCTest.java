import com.codecool.shop.dao.implementation.JDBC.ProductCategoryJDBC;

public class ProductCategoryDaoJDBCTest extends ProductCategoryDaoTest<ProductCategoryJDBC>{
    @Override
    protected ProductCategoryJDBC createInstance() {
        return ProductCategoryJDBC.getInstance();
    }
}
