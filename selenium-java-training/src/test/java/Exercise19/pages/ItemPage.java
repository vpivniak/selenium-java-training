package Exercise19.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * Created by ysparrow on 02.03.2017.
 */
public class ItemPage extends Page {

    public ItemPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "button[name=add_cart_product")
    public WebElement addToCartButton;

    @FindBy(css = "select[name='options[Size]'")
    public WebElement sizeSelector;

    public void addItemToTheCart() {

        Integer count = Integer.parseInt(quantity.getText().split(" ")[0]) + 1;

        if (isElementPresent(By.cssSelector("select[name='options[Size]'")))
            new Select(sizeSelector).selectByValue("Small");
        addToCartButton.click();
        wait.until(ExpectedConditions.textToBePresentInElement(quantity, count.toString()));
    }
}
