import com.codecool.shop.dao.implementation.Mem.ProductDaoMem;

public class ProductDaoMemTest extends ProductDaoTest<ProductDaoMem>{
    @Override
    protected ProductDaoMem createInstance() {
        return ProductDaoMem.getInstance();
    }
}
