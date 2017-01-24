import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

/**
 * Created by sparrow on 24.01.2017.
 */

public class Exercise03 {

    private WebDriver driver;
    private String baseURL;
    private String username = "admin";
    private String password = "admin";

    @Before
    public void setUp() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        baseURL = "http://localhost:4040/litecart";
    }

    @Test
    public void adminPanelLoginTest() {
        driver.navigate().to(baseURL + "/admin");
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("login")).click();
    }

    @After
    public void shutDown() {
        driver.quit();
        driver = null;
    }

}
