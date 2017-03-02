package Exercise19.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * Created by ysparrow on 02.03.2017.
 */
public class Page {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private int timeout = 4;

    @FindBy(css = "span.quantity")
    public WebElement quantity;

    public Page(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 3);
        PageFactory.initElements(driver, this);
    }

    protected boolean isElementPresent(By element) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        boolean result = driver.findElements(element).size() > 0;
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        return result;
    }
}
