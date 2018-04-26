import com.codecool.shop.dao.implementation.Mem.OrderDaoMem;

public class OrderDaoMemTest extends OrderDaoTest<OrderDaoMem> {
    @Override
    protected OrderDaoMem createInstance() {
        return OrderDaoMem.getInstance();
    }
}
