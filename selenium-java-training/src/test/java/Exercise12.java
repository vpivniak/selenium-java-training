import com.google.common.collect.Ordering;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by sparrow on 04.02.2017.
 */
public class Exercise12 {

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseURL;
    private String username = "admin";
    private String password = "admin";

    private By tabsLocator = By.cssSelector("div.tabs ul.index");

    String productName;

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
    public void addNewProductTest() {
        By menuBlock = By.cssSelector("#sidebar ul#box-apps-menu");
        productName = "Demo Product "+getTimePrefix();
        //Select Catalog menu item
        driver.findElement(menuBlock).findElement(By.xpath("./li[@id='app-']//span[text()='Catalog']")).click();

        driver.findElement(By.partialLinkText("Add New Product")).click();

        fillGeneralTab();
        fillPricesTab();

        driver.findElement(By.cssSelector("button[name='save']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form[name=catalog_form]")));
        Assert.assertTrue(isElementPresent(By.linkText(productName)));
    }

    private void fillGeneralTab() {
        //Navigate to General tab
        driver.findElement(tabsLocator).findElement(By.linkText("General")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name=date_valid_to]")));
        //Click Status-enabled
        driver.findElement(By.xpath("//input[@name='status' and @value='1']")).click();
        //Set product name
        driver.findElement(By.cssSelector("input[name='name[en]']")).sendKeys(productName);
        //Set product code
        driver.findElement(By.cssSelector("input[name=code]")).sendKeys("demoCode");
        //Add one more category to Default Categories list
        driver.findElement(By.cssSelector("input[data-name='Rubber Ducks']")).click();
        //Select Default Category
        Select category = new Select(driver.findElement(By.cssSelector("select[name=default_category_id]")));
        category.selectByValue("1");
        //Set Product group to Unisex
        driver.findElement(By.cssSelector("tr:nth-of-type(4) > td >input[name='product_groups[]']")).click();
        //Set Quantity
        driver.findElement(By.cssSelector("input[name=quantity]")).clear();
        driver.findElement(By.cssSelector("input[name=quantity]")).sendKeys("2");
        //Attach image
        WebElement fileField = driver.findElement(By.cssSelector("input[name='new_images[]']"));
        unhide(driver, fileField);
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Bart_Simpson_200px.png").getFile());
        fileField.sendKeys(file.getAbsolutePath());
        //Set date valid from (incorrect way, but works for my Locale :))
        driver.findElement(By.cssSelector("input[name=date_valid_from]")).sendKeys("02.02.2017");
        //Set date valid to (incorrect way, but works for my Locale :) )
        driver.findElement(By.cssSelector("input[name=date_valid_to]")).sendKeys("02.02.2018");
    }

    private void fillPricesTab() {
        driver.findElement(tabsLocator).findElement(By.linkText("Prices")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name=purchase_price]")));

        Select purchase_price = new Select(driver.findElement(By.cssSelector("select[name=purchase_price_currency_code]")));
        purchase_price.selectByValue("USD");

        driver.findElement(By.cssSelector("input[name=purchase_price]")).clear();
        driver.findElement(By.cssSelector("input[name=purchase_price]")).sendKeys("11.23");

        driver.findElement(By.cssSelector("input[name='prices[USD]'")).sendKeys("25.34");
        driver.findElement(By.cssSelector("input[name='prices[EUR]'")).sendKeys("18.34");
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

    private void unhide(WebDriver driver, WebElement element) {
        String script = "arguments[0].style.opacity=1;"
                + "arguments[0].style['transform']='translate(0px, 0px) scale(1)';"
                + "arguments[0].style['MozTransform']='translate(0px, 0px) scale(1)';"
                + "arguments[0].style['WebkitTransform']='translate(0px, 0px) scale(1)';"
                + "arguments[0].style['msTransform']='translate(0px, 0px) scale(1)';"
                + "arguments[0].style['OTransform']='translate(0px, 0px) scale(1)';"
                + "return true;";
        ((JavascriptExecutor) driver).executeScript(script, element);
    }
    private Long getTimePrefix() {
        return new GregorianCalendar().getTimeInMillis();
    }
}
