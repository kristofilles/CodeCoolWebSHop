import com.codecool.shop.Globals;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ProductDaoTest<T extends ProductDao> {

    private T instance;

    protected abstract T createInstance();

    @BeforeEach
    public void setUp(){
        instance = createInstance();
    }

    @Test
    public void testAdd(){
        int lengthBefore = instance.getAll().size();
        Product testProduct = new Product(999,"TEST", 1, "USD", "Test", Globals.productCategoryDao.find(0), Globals.supplierDao.find(0), "kek.png");
        instance.add(testProduct);
        assertEquals(lengthBefore + 1, instance.getAll().size());
    }

    @Test
    public void testFind(){
        Product foundProduct = instance.find(999);
        assertEquals("TEST", foundProduct.getName());
    }

    @Test
    public void testRemove(){
        int lengthBefore = instance.getAll().size();
        instance.remove(999);
        assertEquals(lengthBefore - 1, instance.getAll().size());
    }

    @Test
    public void testGetAll(){
        assertTrue(instance.getAll() instanceof ArrayList);
    }


}
