import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Exercise14 {

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
    public void newWindowForCountryEditTest() {

        driver.navigate().to(baseURL + "/admin/?app=countries&doc=countries");
        driver.findElement(By.cssSelector("div>a.button")).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("h1"), "Add New Country"));

        List<WebElement> externalLinks = driver.findElements(By.cssSelector(".fa.fa-external-link"));

        String initialWindow = driver.getWindowHandle();

        for (WebElement link : externalLinks) {
            Set<String> allWindows = driver.getWindowHandles();
            link.click();
            String newHandle = wait.until(anyNewWindowOtherThan(allWindows));
            driver.switchTo().window(newHandle);
            driver.close();
            driver.switchTo().window(initialWindow);
        }
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

    public ExpectedCondition<String> anyNewWindowOtherThan(Set<String> windows) {
        return new ExpectedCondition<String>() {
            public String apply(WebDriver input) {
                Set<String> handles = driver.getWindowHandles();
                handles.removeAll(windows);
                return handles.size() > 0 ? handles.iterator().next() : null;
            }
        };
    }
}
