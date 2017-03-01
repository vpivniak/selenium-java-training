import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;

/**
 * Created by sparrow on 01.03.2017.
 */
public class Exercise17 {

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseURL;
    private String username = "admin";
    private String password = "admin";
    private int timeout = 5;

    @Before
    public void setUp() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 3);
        baseURL = "http://localhost:4040/litecart";

        performLogin();
    }

    @Test
    public void browserLogTest() {
        driver.navigate().to(baseURL + "/admin/?app=catalog&doc=catalog");

        int tableIterator = 2; //Start from 2 to skip Root <tr> in table

        System.out.println("Available logs: " + driver.manage().logs().getAvailableLogTypes());

        do {
            driver.findElement(By.xpath("//tr[@class='row'][" + tableIterator + "]/td/a")).click();

            if (isElementPresent(By.xpath("//h1[contains(text(),'Edit Product')]"))) {
                driver.navigate().back();
            }

            Assert.assertTrue("Browser Log not empty!", driver.manage().logs().get("browser").getAll().size() == 0);

            tableIterator++;

        } while (isElementPresent(By.xpath("//tr[@class='row'][" + tableIterator + "]")));
    }

    @After
    public void shutDown() {
        driver.quit();
        driver = null;
    }

    private void performLogin() {
        By sidebar = By.id("sidebar");
        driver.navigate().to(baseURL + "/admin");
        if (isElementPresent(sidebar)) return;
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("login")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(sidebar));
    }

    private boolean isElementPresent(By element) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        boolean result = driver.findElements(element).size() > 0;
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        return result;
    }
}
