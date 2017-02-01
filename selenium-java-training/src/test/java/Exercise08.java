import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by sparrow on 31.01.2017.
 */

public class Exercise08 {

    private WebDriver driver;
    private String baseURL = "http://localhost:4040/litecart/";

    @Before
    public void setUp() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
    }

    @Test
    public void homePageItemsStickerTest() {

        driver.navigate().to(baseURL);

        List<WebElement> items = driver.findElements(By.cssSelector("li.product.column.shadow"));

        if (items.size() > 0) {
            for (WebElement item : items) {
                Assert.assertTrue("Number of stickers is not equal to 1 for item: " + item.getText(),
                        item.findElements(By.cssSelector(".sticker")).size() == 1);
            }
        }
        else Assert.fail("There is no items on home screen or home screen is not opened.");
    }

    @After
    public void shutDown() {
        driver.quit();
        driver = null;
    }

}