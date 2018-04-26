import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductCategoryDao;
import static org.junit.jupiter.api.Assertions.*;

import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.order.Order;
import com.codecool.shop.order.OrderStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public abstract class OrderDaoTest<T extends OrderDao> {

    private T instance;

    protected abstract T createInstance();

    @BeforeEach
    public void setUp(){
        instance = createInstance();
    }

    @Test
    public void testAdd(){
        int lengthBefore = instance.getAll().size();
        Order testOrder = new Order(999,1, "CART");
        instance.add(testOrder);
        assertEquals(lengthBefore + 1, instance.getAll().size());
    }

    @Test
    public void testFind(){
        Order foundOrder = instance.find(999);
        assertEquals("CART", String.valueOf(foundOrder.getStatus()));
    }

    @Test
    public void testRemove(){
        int lengthBefore = instance.getAll().size();
        instance.remove(999);
        assertEquals(lengthBefore - 1, instance.getAll().size());
    }

    @Test
    public void testGetAll(){
        assertTrue(instance.getAll() instanceof List);
    }

}

