import com.codecool.shop.dao.implementation.JDBC.SupplierDaoJDBC;

public class SupplierDaoJDBCTest extends SupplierDaoTest<SupplierDaoJDBC> {
    @Override
    protected SupplierDaoJDBC createInstance() {
        return SupplierDaoJDBC.getInstance();
    }
}
