import com.codecool.shop.dao.implementation.JDBC.OrderDaoJDBC;

public class OrderDaoJDBCTest extends OrderDaoTest<OrderDaoJDBC> {
    @Override
    protected OrderDaoJDBC createInstance() {
        return OrderDaoJDBC.getInstance();
    }
}
