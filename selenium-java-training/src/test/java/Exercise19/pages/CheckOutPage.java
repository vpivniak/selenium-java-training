package Exercise19.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created by ysparrow on 02.03.2017.
 */
public class CheckOutPage extends Page{

    public CheckOutPage(WebDriver driver) {
        super(driver);
    }

    private By removeFromCartButton = By.cssSelector("button[name=remove_cart_item]");
    private By tableRow = By.cssSelector("table.dataTable.rounded-corners tr:nth-of-type(2)");

    public void removeAllItems() {
        do {
            WebElement tableRowElement = driver.findElement(tableRow);
            driver.findElement(removeFromCartButton).click();
            wait.until(ExpectedConditions.stalenessOf(tableRowElement));
        } while (isElementPresent(removeFromCartButton));
    }

    public void open() {
        driver.findElement(By.partialLinkText("Checkout")).click();
    }
}

