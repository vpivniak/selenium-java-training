package Exercise19.app;

import Exercise19.pages.CheckOutPage;
import Exercise19.pages.HomePage;
import Exercise19.pages.ItemPage;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by ysparrow on 02.03.2017.
 */
public class Application {

    private WebDriver driver;

    private HomePage homePage;
    private ItemPage itemPage;
    private CheckOutPage checkOutPage;

    public Application() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        homePage = new HomePage(driver);
        itemPage = new ItemPage(driver);
        checkOutPage = new CheckOutPage(driver);
    }

    public void quit() {
        driver.quit();
    }

    public void removeAllItemsFromTheCart() {
        checkOutPage.open();
        checkOutPage.removeAllItems();
        homePage.open();
    }

    public void addItemToTheCart() {
        homePage.open();
        homePage.choseFirsItem();
        itemPage.addItemToTheChart();
        homePage.open();
    }

    public int getItemsCount() {
        return homePage.getItemsCount();
    }
}
