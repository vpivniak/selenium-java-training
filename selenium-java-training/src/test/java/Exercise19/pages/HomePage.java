package Exercise19.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Created by ysparrow on 02.03.2017.
 */
public class HomePage extends Page {

    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "div#box-most-popular li.product.column.shadow.hover-light:nth-of-type(1)")
    public WebElement firstItem;

    public void open() {
        driver.get("http://localhost:4040/litecart/");
    }

    public void choseFirsItem() {
        firstItem.click();
    }

    public int getItemsCount() {
        return Integer.parseInt(quantity.getText().split(" ")[0]);
    }
}
