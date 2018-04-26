import com.codecool.shop.dao.implementation.JDBC.LineItemDaoJDBC;

public class LineItemDaoJDBCTest extends LineItemDaoTest<LineItemDaoJDBC> {
    @Override
    protected LineItemDaoJDBC createInstance() {
        return LineItemDaoJDBC.getInstance();
    }
}
