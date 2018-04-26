import static org.junit.jupiter.api.Assertions.*;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public abstract class SupplierDaoTest<T extends SupplierDao> {

    private  T instance;

    protected abstract T createInstance();

    @BeforeEach
    public void setUp(){
        instance = createInstance();
    }

    @Test
    public void testAdd(){
        int lengthBefore = instance.getAll().size();
        Supplier testSupplier = new Supplier(999,"TEST", "Test supplier");
        instance.add(testSupplier);
        assertEquals(lengthBefore + 1, instance.getAll().size());
    }

    @Test
    public void testFind(){
        Supplier foundSupplier = instance.find(999);
        assertEquals("TEST", foundSupplier.getName());
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
