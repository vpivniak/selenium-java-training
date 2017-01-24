import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.ChromeDriverManager;

/**
 * Created by sparrow on 24.01.2017.
 */
public class Exercise01 {

    private WebDriver driver;

    @Before
    public void startUp()
    {
        ChromeDriverManager.getInstance().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void firstTest()
    {
        driver.get("http://software-testing.ru/");
    }

    @After
    public void shutDown()
    {
        driver.quit();
    }

}
