import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Exercise13 {

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseURL = "http://localhost:4040/litecart/";
    private int timeout = 5;

    @Before
    public void setUp() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, timeout);
    }

    @Test
    public void cartAddRemoveTest() {

        By firstItem = By.cssSelector("div#box-most-popular li.product.column.shadow.hover-light:nth-of-type(1)");
        By addToChartButton = By.cssSelector("button[name=add_cart_product]");
        By removeFromChartButton = By.cssSelector("button[name=remove_cart_item]");
        By quantity = By.cssSelector("span.quantity");
        By sizeSelector = By.cssSelector("select[name='options[Size]'");
        By tableRow = By.cssSelector("table.dataTable.rounded-corners tr:nth-of-type(2)");

        WebElement quantityElement;

        // 1) открыть главную страницу
        driver.navigate().to(baseURL);

        for (Integer i = 1; i <= 3; i++) {
            // 2) открыть первый товар из списка
            driver.findElement(firstItem).click();
            quantityElement = driver.findElement(quantity);
            // 3) добавить его в корзину (при этом может случайно добавиться товар, который там уже есть, ничего страшного)
            if (isElementPresent(sizeSelector))
                new Select(driver.findElement(sizeSelector)).selectByValue("Small");
            driver.findElement(addToChartButton).click();
            // 4) подождать, пока счётчик товаров в корзине обновится
            wait.until(ExpectedConditions.textToBePresentInElement(quantityElement, i.toString()));
            // 5) вернуться на главную страницу, повторить предыдущие шаги ещё два раза, чтобы в общей сложности в корзине было 3 единицы товара
            driver.navigate().to(baseURL);
        }

        // 5) открыть корзину (в правом верхнем углу кликнуть по ссылке Checkout)
        driver.findElement(By.partialLinkText("Checkout")).click();

        // 6) удалить все товары из корзины один за другим, после каждого удаления подождать, пока внизу обновится таблица
        do {
            WebElement tableRowElement = driver.findElement(tableRow);
            driver.findElement(removeFromChartButton).click();
            wait.until(ExpectedConditions.stalenessOf(tableRowElement));
        } while (isElementPresent(removeFromChartButton));
    }

    private boolean isElementPresent(By element) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        boolean result = driver.findElements(element).size() > 0;
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        return result;
    }

    @After
    public void shutDown() {
        driver.quit();
        driver = null;
    }
}