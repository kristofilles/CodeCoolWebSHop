import com.codecool.shop.dao.ProductCategoryDao;
import static org.junit.jupiter.api.Assertions.*;

import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public abstract class ProductCategoryDaoTest<T extends ProductCategoryDao> {

    private T instance;

    protected abstract T createInstance();

    @BeforeEach
    public void setUp(){
        instance = createInstance();
    }

    @Test
    public void testAdd(){
        int lengthBefore = instance.getAll().size();
        ProductCategory testProductCategory = new ProductCategory(999,"TEST","Test department", "Test category");
        instance.add(testProductCategory);
        assertEquals(lengthBefore + 1, instance.getAll().size());
    }

    @Test
    public void testFind(){
        ProductCategory foundCategory = instance.find(999);
        assertEquals("TEST", foundCategory.getName());
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
