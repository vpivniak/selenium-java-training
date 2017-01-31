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


/**
 * Created by sparrow on 31.01.2017.
 */
public class Exercise07 {

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseURL;
    private String username = "admin";
    private String password = "admin";

    @Before
    public void setUp() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 10);
        baseURL = "http://localhost:4040/litecart";
        performLogin();
    }


    @Test
    public void adminPanelMenuTest() {

        By menuBlock = By.cssSelector("#sidebar ul#box-apps-menu");
        By selectedItem = By.cssSelector("li.selected");

        int menuSize = driver.findElement(menuBlock).findElements(By.xpath("./li")).size();

        for (int menuItem = 1; menuItem <= menuSize; menuItem++) {

            driver.findElement(menuBlock).findElement(By.xpath("./li[" + menuItem + "]")).click();

            int subMenuSize = driver.findElement(menuBlock).findElement(selectedItem).findElements(By.cssSelector("li")).size();

            if (subMenuSize > 0) {

                for (int subMenuItem = 1; subMenuItem <= subMenuSize; subMenuItem++) {

                    driver.findElement(selectedItem).findElement(By.cssSelector("li:nth-of-type(" + subMenuItem + ")")).click();

                    Assert.assertTrue(isElementPresent(By.cssSelector("h1")));

                    /*
                    // This code may verify that h1 text is equal to the subMenuItem text
                    // but it does not work as far as h1 text in application is not always equal to subMenuItem text
                    //
                    // Assert.assertEquals(
                    // driver.findElement(selectedItem).findElement(selectedItem).getText(),
                    // driver.findElement(By.cssSelector("h1")).getText()
                    // );
                    */
                }

            } else {
                Assert.assertEquals(
                        driver.findElement(menuBlock).findElement(By.xpath("./li[" + menuItem + "]")).getText(),
                        driver.findElement(By.cssSelector("h1")).getText()
                );
            }

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
        return driver.findElements(element).size() > 0;
    }
}
