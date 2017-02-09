import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by sparrow on 31.01.2017.
 */

public class Exercise11 {

    private WebDriver driver;
    private String baseURL = "http://localhost:4040/litecart/";
    private String email;
    private String password;
    By accountActionsBlock = By.cssSelector("#box-account");

    @Before
    public void setUp() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.navigate().to(baseURL);
    }

    @Test
    public void customerRegistrationFlow() {

        email = "test" + getTimePrefix() + "@test.com";
        password = "password";

        logoutIfLoggedIn();
        registerNewCustomer();
        logout();
        loginAs(email, password);
        logout();

        Assert.assertFalse("Registration flow failed", isElementPresent(accountActionsBlock));
    }

    private void registerNewCustomer() {

        driver.findElement(By.xpath("//a[text()='New customers click here']")).click();

        driver.findElement(By.cssSelector("input[name=firstname]")).sendKeys("Vasya");
        driver.findElement(By.cssSelector("input[name=lastname]")).sendKeys("Test Pupkin");
        driver.findElement(By.cssSelector("input[name=address1]")).sendKeys("5th Street 11");
        driver.findElement(By.cssSelector("input[name=postcode]")).sendKeys("12345");
        driver.findElement(By.cssSelector("input[name=city]")).sendKeys("New-York");
        driver.findElement(By.cssSelector("input[name=email]")).sendKeys(email);
        driver.findElement(By.cssSelector("input[name=phone]")).sendKeys("+11111111111");
        driver.findElement(By.cssSelector("input[name=newsletter]")).click();

        Select country = new Select(driver.findElement(By.cssSelector("select[name=country_code]")));
        country.selectByValue("US");

        Select state = new Select(driver.findElement(By.cssSelector("select[name=zone_code]")));
        state.selectByValue("NY");

        driver.findElement(By.cssSelector("input[name=password]")).sendKeys(password);
        driver.findElement(By.cssSelector("input[name=confirmed_password]")).sendKeys(password);
        driver.findElement(By.cssSelector("button[name=create_account]")).click();
    }

    private void loginAs(String email, String password) {
        WebElement emailElement = driver.findElement(By.cssSelector("input[name=email]"));
        WebElement passwordElement = driver.findElement(By.cssSelector("input[name=password]"));

        emailElement.clear();
        emailElement.sendKeys(email);

        passwordElement.clear();
        passwordElement.sendKeys(password);

        driver.findElement(By.cssSelector("button[name=login]")).click();
    }

    private void logoutIfLoggedIn() {
        if (isElementPresent(accountActionsBlock)) {
            logout();
        }
    }

    private void logout() {
        driver.findElement(By.xpath("//*[@id='box-account']//a[text()='Logout']")).click();
    }

    private Long getTimePrefix() {
        return new GregorianCalendar().getTimeInMillis();
    }

    private boolean isElementPresent(By element) {
        return driver.findElements(element).size() > 0;
    }

    @After
    public void shutDown() {
        driver.quit();
        driver = null;
    }
}