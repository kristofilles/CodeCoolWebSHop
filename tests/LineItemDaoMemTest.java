import com.codecool.shop.dao.implementation.Mem.LineItemDaoMem;

public class LineItemDaoMemTest extends LineItemDaoTest<LineItemDaoMem>{
    @Override
    protected LineItemDaoMem createInstance() {
        return LineItemDaoMem.getInstance();
    }
}
