package Exercise19.tests;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ysparrow on 02.03.2017.
 */
public class CartTest extends TestBase {

    @Test
    public void addAndRemoveItemsFlowTest() {

        app.addItemToTheCart();
        Assert.assertEquals(1, app.getItemsCount());

        app.addItemToTheCart();
        Assert.assertEquals(2, app.getItemsCount());

        app.addItemToTheCart();
        Assert.assertEquals(3, app.getItemsCount());

        app.removeAllItemsFromTheCart();
        Assert.assertEquals(0, app.getItemsCount());
    }
}
