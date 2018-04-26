import static org.junit.jupiter.api.Assertions.*;

import com.codecool.shop.Globals;
import com.codecool.shop.dao.LineItemDao;
import com.codecool.shop.order.LineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public abstract class LineItemDaoTest<T extends LineItemDao> {

    private T instance;

    protected abstract T createInstance();

    @BeforeEach
    public void setUp(){
        instance = createInstance();
    }

    @Test
    public void testAdd(){
        int lengthBefore = instance.getAll().size();
        LineItem testItem = new LineItem(999, Globals.productDao.find(0), 0, 123 , 1);
        instance.add(testItem);
        assertEquals(lengthBefore + 1, instance.getAll().size());
    }

    @Test
    public void testFind(){
        LineItem foundItem = instance.find(999);
        assertEquals(123, foundItem.getQuantity());
    }


    @Test
    public void testGetAll(){
        assertTrue(instance.getAll() instanceof ArrayList);
    }

    @Test
    public void testgetAllByOrderId(){
        assertTrue(instance.getAllByOrderId(1) instanceof ArrayList);
    }


    @Test
    public void testRemove(){
        int lengthBefore = instance.getAll().size();
        LineItem foundItem = instance.find(999);
        instance.remove(foundItem);
        assertEquals(lengthBefore - 1, instance.getAll().size());
    }


}


